#include "GameServer.h"
#include "spdlog/spdlog.h"
#include "spdlog/sinks/daily_file_sink.h"







void GameServer::Start()
{

    // ��ʼ����־ģ��
	m_logger = spdlog::daily_logger_mt("game_server", "logs/game_server.log", 0, 0);
	m_logger->set_level(spdlog::level::debug); // Set global log level to info



	std::function<void(int,int, void*)> listen_function = std::bind(&GameServer::listen_call_back, 
		this, std::placeholders::_1, std::placeholders::_2, std::placeholders::_3);
	std::function<void(int,int, void*)> read_function = std::bind(&GameServer::read_call_back, 
		this, std::placeholders::_1, std::placeholders::_2, std::placeholders::_3);



	short listen_port = 5150;
	int listen_socket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

	// ��socket�ĳ�
	int32_t flags = fcntl(listen_socket, F_GETFL, 0);
	if (-1 == flags || -1 == fcntl(listen_socket, F_SETFL, flags & (~O_NONBLOCK)))
	{
		m_logger->error("set listen socket to nonblock error!");
		return;
	}

	struct sockaddr_in sin;
	sin.sin_family = AF_INET;
	sin.sin_port = htons(listen_port);
	sin.sin_addr.s_addr = htonl(INADDR_ANY);


	if (0 != bind(listen_socket, (sockaddr*)&sin, sizeof(sin)))
	{
		m_logger->error("bind failed!%d\n", errno);
		return;
	}

	listen(listen_socket, 5);


	//1)��ʼ��һ���׽��ּ���fdSocket�����������׽��ַ���  
	fd_set socket_set = { 0 };
	FD_ZERO(&socket_set);
	FD_SET(listen_socket, &socket_set);

	struct timeval time = { 1,0 };
	char buf[4096];


	fd_set    readSet;
	FD_ZERO(&readSet);

	fd_set    writeSet;
	FD_ZERO(&readSet);

	int max_fd = listen_socket;

	m_epoll_fd = epoll_create(MAX_EVENTS + 1);
	if (m_epoll_fd <= 0)
	{
		m_logger->error("create efd error : %s ", strerror(errno));
		return;
	}

	struct epoll_event* listen_epoll_event = new epoll_event();
	myevent_s* listen_my_event = new myevent_s();

	listen_my_event->call_back_function = listen_function;
	listen_my_event->fd = listen_socket;
	listen_my_event->events = EPOLLIN;

	listen_epoll_event->events = EPOLLIN;
	listen_epoll_event->data.fd = listen_socket;
	listen_epoll_event->data.ptr = (void*)listen_my_event;

	epoll_ctl(m_epoll_fd, EPOLL_CTL_ADD, listen_socket, listen_epoll_event);

	// epoll event��B+��
	m_epoll_events.insert(std::make_pair(listen_socket, listen_epoll_event));
	m_my_events.insert(std::make_pair(listen_socket, listen_my_event));


	int checkpos = 0;
	int i;
	while (1)
	{
		//����eppoll_wait�ȴ�����Ŀͻ����¼�,epoll_wait�����������������������Щfd��struct epoll_event����
		int nfd = epoll_wait(m_epoll_fd, m_ready_events, MAX_EVENTS + 1, 1000);
		if (nfd < 0)
		{
			printf("epoll_wait error, exit\n");
			exit(-1);
		}

		if (nfd == 0)
		{
			continue;
		}
		for (i = 0; i < nfd; i++)
		{
			//evtAdd()�����У���ӵ��������м����¼���ʱ��myevents_t�ṹ�����͸���ptrָ��
			//����epoll_wait���ص�ʱ��ͬ���᷵�ض�Ӧfd��myevents_t���͵�ָ��
			struct myevent_s* ev = (struct myevent_s*)m_ready_events[i].data.ptr;
			//����������Ƕ��¼��������ص��Ƕ��¼�
			if ((m_ready_events[i].events & EPOLLIN) && (ev->events & EPOLLIN))
			{
				ev->call_back_function(ev->fd, m_ready_events[i].events, ev->arg);
			}
			//�����������д�¼��������ص���д�¼�
			if ((m_ready_events[i].events & EPOLLOUT) && (ev->events & EPOLLOUT))
			{
				ev->call_back_function(ev->fd, m_ready_events[i].events, ev->arg);
			}
		}
	}
}


void GameServer::listen_call_back(int fd, int events, void* arg)
{
	/*while ((conn_sock = accept(listenfd, (struct sockaddr*) & remote,
		(size_t*)&addrlen)) > 0) {
		handle_client(conn_sock);
	}
	if (conn_sock == -1) {
		if (errno != EAGAIN && errno != ECONNABORTED
			&& errno != EPROTO && errno != EINTR)
			perror("accept");
	}*/
	struct sockaddr_in client_address = { 0 };
	socklen_t client_addrlength = sizeof(client_address);
	int connfd = accept(fd, (struct sockaddr*) & client_address, &client_addrlength);
	addfd(fd, connfd, false);
}

void GameServer::read_call_back(int fd, int events, void* arg)
{
	char read_buffer[_G_BUFSIZ] = { 0 };
	int n = 0;
	while (1)
	{
		int nread = read(fd, read_buffer + n, BUFSIZ - 1);//��ʱ���û�����ָ���Ľ������ݻ�������С�̶���һ��Ҫ�����ݴ�
		if (nread < 0)
		{
			if (errno == EAGAIN || errno == EWOULDBLOCK)
			{
				// �����û�������ˣ�������
				break;
			}
			else
			{
				// ���󣬹ر��������
				break;//or return;
			}
		}
		else if (nread == 0)
		{
			// �������ӶϿ�����������
			epoll_event del_event = { 0 };
			epoll_ctl(m_epoll_fd, EPOLL_CTL_DEL, fd, &del_event);
			close(fd);
			break;//or return. because read the EOF
		}
		else
		{
			// �����ݷŵ���Ӧ�����ݴ��������
			n += nread;
		}
	}
}

void GameServer::addfd(int epoolfd, int fd, bool enable_et)
{
	std::function<void(int, int, void*)> read_function = std::bind(&GameServer::read_call_back, 
		this, std::placeholders::_1, std::placeholders::_2, std::placeholders::_3);

	struct epoll_event* read_epoll_event = new epoll_event();
	myevent_s* read_my_event = new myevent_s();

	read_my_event->call_back_function = read_function;
	read_my_event->fd = fd;
	read_my_event->events = EPOLLIN;

	read_epoll_event->events = EPOLLIN;
	read_epoll_event->data.fd = fd;
	read_epoll_event->data.ptr = (void*)read_my_event;



	epoll_ctl(m_epoll_fd, EPOLL_CTL_ADD, fd, read_epoll_event);
	setnonblocking(fd);

	// epoll event��B+��
	m_epoll_events.insert(std::make_pair(fd, read_epoll_event));
	m_my_events.insert(std::make_pair(fd, read_my_event));
}


int GameServer::setnonblocking(int fd)
{
	int old_option = fcntl(fd, F_GETFL);
	int new_option = old_option | O_NONBLOCK;
	fcntl(fd, F_SETFL, new_option);
	return old_option;
}
