using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;

public class Major : MonoBehaviour {

	// Use this for initialization
	void Start () {


		GameObject go = GameObject.Find("SocketIO");
		SocketIOComponent socket = go.GetComponent<SocketIOComponent>();
		socket.url = "ws://127.0.0.1:2020/socket.io/?EIO=4&transport=websocket";
		socket.autoConnect = true;
		socket.On ("connected", OnConnect);
		socket.Start();

	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OnConnect(SocketIOEvent socketIoEvent){

		JSONObject data = socketIoEvent.data;
		data.GetField ("data");

		Debug.Log (socketIoEvent.name);
	}
}
