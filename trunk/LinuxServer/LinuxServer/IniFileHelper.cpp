#include "IniFileHelper.h"

IniFileHelper::IniFileHelper()
{

}

IniFileHelper::~IniFileHelper()
{

}

int IniFileHelper::getIniKeyString(const char *title,const char *key,char *szRtn,const char *file)
{ 
    
    FILE *fp_ = 0;

    if((fp_ = fopen(file, "r")) == NULL) 
    {
    	//LOG_INFO << "can`t find ini file!!"; 
        return 0; 
    }
	char szLine[1024] = {0};
    char tmpstr[1024] = {0};
    char rtnval = 0;
    int i = 0; 
    int flag = 0; 
    char *tmp = NULL;

    while(!feof(fp_)) 
    { 
        rtnval = fgetc(fp_); 
        if(rtnval == EOF) 
        { 
        	szLine[i] = '\0';
            //LOG_INFO<<"szline:"<<szLine;
            tmp = strchr(szLine, '='); 
 
            if(( tmp != NULL )&&(flag == 1)) 
            { 
                if(strstr(szLine,key)!=NULL) 
                { 
                    //注释行
                    if ('#' != szLine[0])
                    {
                    	strcpy(szRtn,tmp+1); 
                        fclose(fp_);
                        //LOG_INFO<<"find:"<<tmpstr;
                        return 1; 
                    }
                } 
            }
            break; 
        } 
        else
        { 
            szLine[i++] = rtnval; 
        } 
        if(rtnval == '\n') 
        { 
            i --;
            szLine[--i] = '\0';
            i = 0; 
            tmp = strchr(szLine, '='); 
 
            if(( tmp != NULL )&&(flag == 1)) 
            { 
                if(strstr(szLine,key)!=NULL) 
                { 
                    //注释行
                    if ('#' != szLine[0])
                    {
                    	strcpy(szRtn,tmp+1); 
                        fclose(fp_);
                        //LOG_INFO<<"find:"<<tmpstr;
                        return 1; 
                    }
                } 
            }
            else
            { 
                strcpy(tmpstr,"["); 
                strcat(tmpstr,title); 
                strcat(tmpstr,"]");
                if( strncmp(tmpstr,szLine,strlen(tmpstr)) == 0 ) 
                {
                    //找到title
                    flag = 1; 
                    //LOG_INFO<<"find:"<<tmpstr;
                }
            }
        }
    }
    //LOG_INFO << "can`t find key"; 
	fclose(fp_);
    return 0; 
}

//从INI文件读取整类型数据
int IniFileHelper::getIniKeyInt(const char *title,const char *key,const char *file)
{
	char szRtn[32] = {0};
	if (!IniFileHelper::getIniKeyString(title,key,szRtn,file))
	{
		return 0;
	}
    return atoi(szRtn);
}
