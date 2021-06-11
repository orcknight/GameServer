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



#define MAX_EVENTS 1024 /*监听上限*/
#define BUFLEN  4096    /*缓存区大小*/
#define SERV_PORT 6666  /*端口号*/

/*描述就绪文件描述符的相关信息*/
struct myevent_s
{
	int fd;             //要监听的文件描述符
	int events;         //对应的监听事件，EPOLLIN和EPLLOUT
	void* arg;          //指向自己结构体指针
	std::function<void(int, int, void*)> call_back_function; //回调函数
	int status;         //是否在监听:1->在红黑树上(监听), 0->不在(不监听)
	char buf[BUFLEN];
	int len;
	long last_active;   //记录每次加入红黑树 g_efd 的时间值
};


class GameServer
{
private:
	int m_epoll_fd; // epoll使用的文件描述符

	std::map<int, epoll_event*> m_epoll_events;						// epoll的事件树
	std::map<int, myevent_s*>	m_my_events;						// 自定义的事件树
	std::map<int, std::string>	m_user_buffer;
	epoll_event					m_ready_events[MAX_EVENTS + 1];		// epoll_wait使用的返回数据列表

	std::shared_ptr<spdlog::logger> m_logger;

private:
	void listen_call_back(int fd, int events, void* arg);
	void read_call_back(int fd, int events, void* arg);
	void addfd(int epoolfd, int fd, bool enable_et);
	int setnonblocking(int fd);


public:
	void Start();
};

