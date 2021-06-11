#ifndef _INI_FILE_HELPER_H_
#define _INI_FILE_HELPER_H_

#include <string.h>
#define  MAX_PATH 260
 
#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>

class IniFileHelper
{
public:
	IniFileHelper();
	~IniFileHelper();

	//从INI文件读取字符串类型数据
	static int getIniKeyString(const char *title,const char *key,char *szRtn,const char *file); 
	//从INI文件读取整类型数据
	static int getIniKeyInt(const char *title,const char *key,const char *file);

};


#endif
