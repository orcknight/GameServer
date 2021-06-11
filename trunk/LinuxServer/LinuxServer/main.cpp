#include <iostream>
#include <string>
#include <vector>
#include <cstring>
#include <memory>
#include <list>

#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/epoll.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <error.h>
#include <pthread.h>
#include <hiredis/hiredis.h>


#include "GameServer.h"


using std::string;
using std::vector;
using std::list;


#define MAX_EVENTS 1024 /*��������*/
#define BUFLEN  4096    /*��������С*/
#define SERV_PORT 6666  /*�˿ں�*/


int g_efd;      //ȫ�ֱ�������Ϊ�������
struct myevent_s g_events[MAX_EVENTS + 1];    //�Զ���ṹ����������. +1-->listen fd

struct ProtobufModel
{
	unsigned char	type;
	int				length;
	string			json_data;
};

int g_epoll_fd = 0;

/*
 * ��װһ���Զ����¼�������fd�����fd�Ļص�����������һ������Ĳ�����
 * ע�⣺�ڷ�װ����¼���ʱ��Ϊ����¼�ָ���˻ص�������һ����˵��һ��fdֻ��һ���ض����¼�
 * ����Ȥ��������¼�������ʱ�򣬾͵�������ص�����
 */
void evnetset(myevent_s *my_events, int fd, std::function<void(int, int, void*)> call_back_function, void* arg)
{
	my_events->fd = fd;
	my_events->call_back_function = call_back_function;
	my_events->events = 0;
	my_events->arg = arg;
	my_events->status = 0;
	if (0 >= my_events->len)
	{
		memset(my_events->buf, 0, sizeof(my_events->buf));
		my_events->len = 0;
	}

	my_events->last_active = time(NULL);
}

/* �� epoll�����ĺ���� ���һ���ļ������� */
void eventadd(int efd, int events, struct myevent_s* my_events)
{
	struct epoll_event epv = { 0 , { 0 } };
	int op = 0;
	epv.data.ptr = my_events;			// ptrָ��һ���ṹ�壨֮ǰ��epollģ�ͺ�����Ϲ��ص����ļ�������cfd��lfd��������ptrָ�룩
	epv.events = my_events->events;		// EPOLLIN �� EPOLLOUT
	if (0 == my_events->status)
	{
		op = EPOLL_CTL_ADD;
		my_events->status = 1;
	}
}


int main()
{
	std::shared_ptr<GameServer> game_server_ptr = std::make_shared<GameServer>();
	game_server_ptr->Start();

	/*redisContext* conn = redisConnect("localhost", 6379);
	if (conn->err)
	{
		std::cout << "connection error: " << conn->errstr << std::endl;
	}

	redisReply* reply = (redisReply*)redisCommand(conn, "incr name");
	freeReplyObject(reply);
	*/


	//reply = (redisReply*)redisCommand(conn, "get foo");
	//std::cout << reply->str << std::endl;
	//freeReplyObject(reply);

	//redisFree(conn);

	return 0;
}