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


#define MAX_EVENTS 1024 /*监听上限*/
#define BUFLEN  4096    /*缓存区大小*/
#define SERV_PORT 6666  /*端口号*/


int g_efd;      //全局变量，作为红黑树根
struct myevent_s g_events[MAX_EVENTS + 1];    //自定义结构体类型数组. +1-->listen fd

struct ProtobufModel
{
	unsigned char	type;
	int				length;
	string			json_data;
};

int g_epoll_fd = 0;

/*
 * 封装一个自定义事件，包括fd，这个fd的回调函数，还有一个额外的参数项
 * 注意：在封装这个事件的时候，为这个事件指明了回调函数，一般来说，一个fd只对一个特定的事件
 * 感兴趣，当这个事件发生的时候，就调用这个回调函数
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

/* 向 epoll监听的红黑树 添加一个文件描述符 */
void eventadd(int efd, int events, struct myevent_s* my_events)
{
	struct epoll_event epv = { 0 , { 0 } };
	int op = 0;
	epv.data.ptr = my_events;			// ptr指向一个结构体（之前的epoll模型红黑树上挂载的是文件描述符cfd和lfd，现在是ptr指针）
	epv.events = my_events->events;		// EPOLLIN 或 EPOLLOUT
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