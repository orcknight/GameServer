#pragma once

#include <iostream>
#include <string>
#include <vector>
#include <cstring>
#include <memory>
#include <list>
#include <map>
#include <functional>

#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/epoll.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <error.h>
#include <pthread.h>
#include <hiredis/hiredis.h>
#include <spdlog/logger.h>



#define MAX_EVENTS 1024 /*��������*/
#define BUFLEN  4096    /*��������С*/
#define SERV_PORT 6666  /*�˿ں�*/

/*���������ļ��������������Ϣ*/
struct myevent_s
{
	int fd;             //Ҫ�������ļ�������
	int events;         //��Ӧ�ļ����¼���EPOLLIN��EPLLOUT
	void* arg;          //ָ���Լ��ṹ��ָ��
	std::function<void(int, int, void*)> call_back_function; //�ص�����
	int status;         //�Ƿ��ڼ���:1->�ں������(����), 0->����(������)
	char buf[BUFLEN];
	int len;
	long last_active;   //��¼ÿ�μ������� g_efd ��ʱ��ֵ
};


class GameServer
{
private:
	int m_epoll_fd; // epollʹ�õ��ļ�������

	std::map<int, epoll_event*> m_epoll_events;						// epoll���¼���
	std::map<int, myevent_s*>	m_my_events;						// �Զ�����¼���
	std::map<int, std::string>	m_user_buffer;
	epoll_event					m_ready_events[MAX_EVENTS + 1];		// epoll_waitʹ�õķ��������б�

	std::shared_ptr<spdlog::logger> m_logger;

private:
	void listen_call_back(int fd, int events, void* arg);
	void read_call_back(int fd, int events, void* arg);
	void addfd(int epoolfd, int fd, bool enable_et);
	int setnonblocking(int fd);


public:
	void Start();
};

