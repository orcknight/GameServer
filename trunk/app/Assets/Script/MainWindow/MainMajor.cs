using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;
using Newtonsoft.Json.Linq;
using System.Threading;

public class MainMajor : MonoBehaviour {

	private GameObject m_SocketIo = null;
	private SocketIOComponent m_Socket = null;

	public GameObject[] m_StatusBars;


	// Use this for initialization
	void Start () {

		m_SocketIo = GameObject.Find("SocketIO");
		m_Socket = m_SocketIo.GetComponent<SocketIOComponent>();
		m_Socket.url = "ws://192.168.0.103:2020/socket.io/?EIO=4&transport=websocket";
		m_Socket.autoConnect = true;
		m_Socket.On ("connected", OnConnect);
		m_Socket.On ("status", OnStatus);
		m_Socket.On ("stream", OnStream);
		m_Socket.Start();
	
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OnConnect(SocketIOEvent socketIoEvent){

		//连接以后发送版本验证请求
		if (m_Socket == null) {
			return;
		}
			
		JSONObject jsonObject = new JSONObject ();
		jsonObject.AddField ("cmd", "checkversion");
		jsonObject.AddField ("data", "");
		m_Socket.Emit ("unity_stream", jsonObject);
		return;




		JSONObject data = socketIoEvent.data;

		JArray jlist = JArray.Parse(data.ToString()); //将pois部分视为一个JObject，JArray解析这个JObject的字符串 

		for (int i = 0; i < jlist.Count; ++i) {  //遍历JArray  

			HpBar tempBar = m_StatusBars [i].GetComponent<HpBar> ();

			JObject item = JObject.Parse (jlist [i].ToString ()); 
			tempBar.m_Max = 100;//int.Parse (item ["max"].ToString ());
			tempBar.m_Eff = 50;//int.Parse (item ["eff"].ToString ());
			tempBar.m_Cur = 40;//int.Parse(item["cur"].ToString());
			tempBar.m_Desc = item ["value"].ToString ();
			tempBar.m_ForeColor = 0xffff00;
			tempBar.RedrawBar ();

		}

		Debug.Log (socketIoEvent.name);
	}

	public void OnStatus(SocketIOEvent socketIoEvent){

		JSONObject data = socketIoEvent.data;
		data.GetField ("data");

		Debug.Log (socketIoEvent.name);
	}

	public void OnStream(SocketIOEvent socketIoEvent){

		JSONObject data = socketIoEvent.data;
		data.GetField ("data");

		Debug.Log (socketIoEvent.name);
	}
		
}
