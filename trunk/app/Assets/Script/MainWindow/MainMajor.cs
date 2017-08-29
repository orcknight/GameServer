using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;
using Newtonsoft.Json.Linq;

public class MainMajor : MonoBehaviour {

	private GameObject m_SocketIo = null;

	public GameObject[] m_StatusBars;


	// Use this for initialization
	void Start () {

		m_SocketIo = GameObject.Find("SocketIO");
		SocketIOComponent socket = m_SocketIo.GetComponent<SocketIOComponent>();
		socket.url = "ws://192.168.0.103:2020/socket.io/?EIO=4&transport=websocket";
		socket.autoConnect = true;
		socket.On ("connected", OnConnect);
		socket.On ("status", OnStatus);
		socket.On ("stream", OnStream);
		socket.Start();
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OnConnect(SocketIOEvent socketIoEvent){

		JSONObject data = socketIoEvent.data;

		JArray jlist = JArray.Parse(data.ToString()); //将pois部分视为一个JObject，JArray解析这个JObject的字符串 

		for (int i = 0; i < jlist.Count; ++i) {  //遍历JArray  

			HpBar tempBar = m_StatusBars [i].GetComponent<HpBar> ();

			JObject item = JObject.Parse (jlist [i].ToString ()); 
			tempBar.m_Max = int.Parse (item ["max"].ToString ());
			tempBar.m_Eff = int.Parse (item ["eff"].ToString ());
			tempBar.m_Cur = int.Parse(item["cur"].ToString());
			tempBar.m_Desc = item ["value"].ToString ();
			tempBar.m_ForeColor = 0xff0000;
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
