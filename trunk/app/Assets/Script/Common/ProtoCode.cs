using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ProtoCode {

	public const string CHECK_VERSION_CODE = "001"; //验证版本成功
	public const string INFO_WINDOW_CODE = "015"; //发送消息到信息主窗口
	public const string CREATE_ROLE_CODE = "0000008"; //打开创建角色窗口
	public const string POP_WINDOW_CODE = "000100"; //弹出对话框
	public const string ROLE_CREATED_CODE = "0000007"; //角色创建成功
	public const string ROOM_SHORT_CODE = "002"; //房间名字
	public const string ROOM_ROAD_CODE = "003"; //房间东西南北的地图
	public const string ROOM_LONG_CODE = "004"; //房间的描述
	public const string OBJECT_ENTER_CODE = "005"; //物品进入房间
	public const string OBJECT_LEAVE_CODE = "905"; //物品离开房间
	public const string OBJECT_CLEAR_CODE = "906"; //清空NPC列表命令
	public const string OBJECT_INFO_POP_CODE = "009"; //弹出查看物品的对话框
}
