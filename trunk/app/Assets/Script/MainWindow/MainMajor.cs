using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;
using Newtonsoft.Json.Linq;
using System.Threading;

public class MainMajor : MonoBehaviour {

	private GameObject m_SocketIo = null;
	private SocketIOComponent m_Socket = null;
	private Transform m_InfoWindowTrans = null;
	private CommonUIManager m_CommonUIManager = null;
	private GameObject m_CreateRoleWindow = null;
	private GameObject m_ObjectInfoPop = null;
	public GameObject[] m_StatusBars;

	// Use this for initialization
	void Start () {

		m_CommonUIManager = Singleton.getInstance ("CommonUIManager") as CommonUIManager;
		m_InfoWindowTrans = this.transform.Find ("InfoWindow");

		m_SocketIo = GameObject.Find("SocketIO");
		m_Socket = m_SocketIo.GetComponent<SocketIOComponent>();
		m_Socket.url = "ws://127.0.0.1:2020/socket.io/?EIO=4&transport=websocket";
		m_Socket.Awake ();
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
		JArray jlist = JArray.Parse(data.ToString()); //将pois部分视为一个JObject，JArray解析这个JObject的字符串 

		for (int i = 0; i < jlist.Count; ++i) {  //遍历JArray  

			HpBar tempBar = m_StatusBars [i].GetComponent<HpBar> ();

			JObject item = JObject.Parse (jlist [i].ToString ()); 
			tempBar.m_Max = int.Parse (item ["max"].ToString ());
			tempBar.m_Eff = int.Parse (item ["eff"].ToString ());
			tempBar.m_Cur = int.Parse(item["cur"].ToString());
			tempBar.m_Desc = item ["value"].ToString ();

			if (item ["color"] != null) {
				
				tempBar.m_ForeColor = int.Parse (item ["color"].ToString ());
			}
			tempBar.RedrawBar ();

		}

		Debug.Log (socketIoEvent.name);
	}

	public void OnStream(SocketIOEvent socketIoEvent){

		JSONObject data = socketIoEvent.data;
		JArray jlist = JArray.Parse(data.ToString()); //将pois部分视为一个JObject，JArray解析这个JObject的字符串

		for (int i = 0; i < jlist.Count; ++i) {  //遍历JArray  

			JObject item = JObject.Parse (jlist [i].ToString ()); 
			string code = item ["code"].ToString ();
			string msg = item ["msg"].ToString ();

			switch(code){

			case ProtoCode.CHECK_VERSION_CODE:
				string userName = PlayerPrefs.GetString ("userName");
				string password = PlayerPrefs.GetString ("password");
				JSONObject sendData = new JSONObject ();
				sendData.AddField ("userName", userName);
				sendData.AddField("password", password);

				JSONObject jsonObject = new JSONObject ();
				jsonObject.AddField ("cmd", "login");
				jsonObject.AddField ("data", sendData);
				m_Socket.Emit ("unity_stream", jsonObject);
				break;
			case ProtoCode.INFO_WINDOW_CODE:
				AddMsgToInfoWindow (msg);
				break;
			case ProtoCode.CREATE_ROLE_CODE:
				this.transform.localPosition = new Vector3 (-3000f, 0f, 0f);
				GameObject createRolePerfab = Resources.Load("CreateRole") as GameObject;  
				m_CreateRoleWindow = GameObject.Instantiate(createRolePerfab) as GameObject;
				m_CreateRoleWindow.transform.parent = this.transform.parent;
				m_CreateRoleWindow.transform.localScale = new Vector3 (1f, 1f, 1f);
				m_CreateRoleWindow.transform.localPosition = Vector3.zero; 
				m_CreateRoleWindow.SetActive (true);
				break;
			case ProtoCode.POP_WINDOW_CODE:
				m_CommonUIManager.ShowMessageBox(  
					"错误",   
					msg,  
					MessageBox.Style.OKAndCancel,  
					OnReceiveQuitConfirmResult); 
				break;
			case ProtoCode.ROLE_CREATED_CODE:
				Destroy (m_CreateRoleWindow);
				this.transform.localPosition = new Vector3 (0f, 0f, 0f);
				break;
			case ProtoCode.ROOM_SHORT_CODE:
				SetRoomShort (msg);
				break;
			case ProtoCode.ROOM_LONG_CODE:
				SetRoomLong (msg);
				break;
			case ProtoCode.ROOM_ROAD_CODE:
				setRoomRoad (msg);
				break;
			case ProtoCode.OBJECT_ENTER_CODE:
				AddObjectToRoom (msg);
				break;
			case ProtoCode.OBJECT_LEAVE_CODE:
				RemoveObjectFromRoom (msg);
				break;
			case ProtoCode.OBJECT_CLEAR_CODE:
				ClearNpcBar ();
				break;
			case ProtoCode.OBJECT_INFO_POP_CODE:
				PopObjectInfoWindow (msg);
				break;
			default:
				break;
			}
				

		}


		Debug.Log (socketIoEvent.name);
	}

	public void OnMoveClick(GameObject obj){
		
		string cmd = "";
		switch (obj.name) {
		case "North":
			cmd = "north";
			break;
		case "South":
			cmd = "south";
			break;
		case "East":
			cmd = "east";
			break;
		case "West":
			cmd = "west";
			break;
		}

		if (string.IsNullOrEmpty (cmd)) {
			return;
		}

		JSONObject sendData = new JSONObject ();
		JSONObject jsonObject = new JSONObject ();
		jsonObject.AddField ("cmd", cmd);
		jsonObject.AddField ("data", sendData);
		m_Socket.Emit ("unity_stream", jsonObject);
	}

	public void OnNpcClick(GameObject obj){

		CmdButtonItem cmdButtonItem = obj.transform.GetComponent<CmdButtonItem> ();

		JSONObject sendData = new JSONObject ();
		sendData.AddField ("target", cmdButtonItem.m_ObjId);
		JSONObject jsonObject = new JSONObject ();
		jsonObject.AddField ("cmd", cmdButtonItem.m_Cmd);
		jsonObject.AddField ("data", sendData);
		m_Socket.Emit ("unity_stream", jsonObject);
	}
		
	protected void OnReceiveQuitConfirmResult(MessageBox.Result result) {  
		//Singleton.RemoveInstance ("CommonUIManager");
		//m_CommonUIManager.root.transform.localPosition = new Vector3(0f, 0f, -3f);

	}

	//处理物品信息对话框的关闭事件
	protected void OnObjectInfoPopQuitEventHandler(){

		if (m_ObjectInfoPop == null) {
		
			return;
		}

		m_ObjectInfoPop.transform.localPosition = new Vector3 (5000f, 0f, 0f);
		m_ObjectInfoPop.SetActive (false);
	}
		
	private void AddMsgToInfoWindow(string msg){

		msg = msg.Replace ("\\n", "\n");
		Transform chatArea = m_InfoWindowTrans.Find ("ChatArea");
		if (chatArea == null) {
			return;
		}

		UITextList uiTextList = chatArea.GetComponent<UITextList> ();
		uiTextList.Add (msg);
	}

	private void SetRoomShort(string msg){
		msg = msg.Replace ("\\n", "\n");

		Transform mapDesc = this.transform.Find ("MapDesc");
		if (mapDesc == null) {
			return;
		}

		Transform shortTrans = mapDesc.Find ("Short");
		if (shortTrans == null) {
			return;
		}

		Transform labelTrans = shortTrans.Find ("Label");
		if (labelTrans == null) {
			return;
		}

		labelTrans.GetComponent<UILabel> ().text = "㊣ " + msg;

		int start = msg.IndexOf ("[");
		int end = msg.IndexOf ("]");

		Transform mapTrans = this.transform.Find ("Map");
		if (mapTrans == null) {
			return;
		}

		Transform posTrans = mapTrans.Find ("Pos");
		if (posTrans == null) {
			return;
		}

		Transform posLabelTrans = posTrans.Find ("Label");
		if (posLabelTrans == null) {
			return;
		}

		string roomName = msg.Substring (0, start-1).Trim();
		posLabelTrans.GetComponent<UILabel>().text = roomName;
	}

	private void SetRoomLong(string msg){

		msg = msg.Replace ("\\n", "\n");

		Transform mapDesc = this.transform.Find ("MapDesc");
		if (mapDesc == null) {
			return;
		}

		Transform longTrans = mapDesc.Find ("Long");
		if (longTrans == null) {
			return;
		}

		Transform descTrans = longTrans.Find ("Desc");
		if (descTrans == null) {
			return;
		}
		descTrans.GetComponent<UITextList> ().Clear ();
		descTrans.GetComponent<UITextList> ().Add(msg);
	}

	private void setRoomRoad(string msg){

		Transform mapTrans = this.transform.Find ("Map");
		if (mapTrans == null) {
			return;
		}

		Transform northTrans = mapTrans.Find ("North");
		Transform southTrans = mapTrans.Find ("South");
		Transform westTrans = mapTrans.Find ("West");
		Transform eastTrans = mapTrans.Find ("East");

		northTrans.gameObject.SetActive (false);
		southTrans.gameObject.SetActive (false);
		westTrans.gameObject.SetActive (false);
		eastTrans.gameObject.SetActive (false);

		Transform northLineTrans = mapTrans.Find ("NorthLine");
		Transform southLineTrans = mapTrans.Find ("SouthLine");
		Transform westLineTrans = mapTrans.Find ("WestLine");
		Transform eastLineTrans = mapTrans.Find ("EastLine");

		northLineTrans.gameObject.SetActive (false);
		southLineTrans.gameObject.SetActive (false);
		westLineTrans.gameObject.SetActive (false);
		eastLineTrans.gameObject.SetActive (false);

		//north:心狠手辣$zj#south:狡黠多变$zj#east:光明磊落$zj#west:阴险奸诈
		string[] msgArray = msg.Split(new char[] { '$', 'z', 'j', '#' });

		for (int i = 0; i < msgArray.Length; i++) {

			string[] directArray = msgArray [i].Split (':');

			string direct = directArray [0];
			string text = directArray [1];

			switch (direct) {
			case "north":
				northTrans.Find ("Label").GetComponent<UILabel> ().text = text;
				northTrans.gameObject.SetActive (true);
				northLineTrans.gameObject.SetActive (true);
				break;
			case "south":
				southTrans.Find ("Label").GetComponent<UILabel> ().text = text;
				southTrans.gameObject.SetActive (true);
				southLineTrans.gameObject.SetActive (true);
				break;
			case "east":
				eastTrans.Find ("Label").GetComponent<UILabel> ().text = text;
				eastTrans.gameObject.SetActive (true);
				eastLineTrans.gameObject.SetActive (true);
				break;
			case "west":
				westTrans.Find ("Label").GetComponent<UILabel> ().text = text;
				westTrans.gameObject.SetActive (true);
				westLineTrans.gameObject.SetActive (true);
				break;
			}

		}
	}

	private void AddObjectToRoom(string msg){

		JArray jlist = JArray.Parse (msg); 
		GameObject buttonPerfab = Resources.Load("Common/NpcButton") as GameObject; 

		Transform npcBarTrans = this.transform.Find ("NpcBar");
		if (npcBarTrans == null) {
			return;
		}

		Transform scrollViewTrans = npcBarTrans.Find ("Scroll View");
		if (scrollViewTrans == null) {
			return;
		}

		Transform gridTrans = scrollViewTrans.Find ("UIGrid");
		if (gridTrans == null) {
			return;
		}

		for (int i = 0; i < jlist.Count; ++i) {

			JObject item = JObject.Parse (jlist [i].ToString ()); 

			string cmd = item ["cmd"].ToString();
			string objId = item ["objId"].ToString();
			string displayName = item ["displayName"].ToString();

			GameObject npcButton = GameObject.Instantiate(buttonPerfab) as GameObject;
			Transform label = npcButton.transform.Find ("Label");
			label.GetComponent<UILabel> ().text = displayName;
			npcButton.GetComponent<CmdButtonItem> ().m_Cmd = cmd;
			npcButton.GetComponent<CmdButtonItem> ().m_ObjId = objId;
			npcButton.GetComponent<CmdButtonItem> ().m_DisplayName = displayName;
			npcButton.transform.parent = gridTrans;
			npcButton.transform.localScale = new Vector3 (1f, 1f, 1f);
			npcButton.transform.localPosition = Vector3.zero; 
			npcButton.SetActive (true);


			UIEventListener.Get(npcButton).onClick = OnNpcClick; 
		}

		gridTrans.GetComponent<UIGrid> ().repositionNow = true;
		gridTrans.GetComponent<UIGrid> ().Reposition ();
		scrollViewTrans.GetComponent<UIScrollView> ().contentPivot = UIWidget.Pivot.TopLeft;
		scrollViewTrans.GetComponent<UIScrollView> ().ResetPosition ();
	}

	private void RemoveObjectFromRoom(string msg){

		JObject item = JObject.Parse (msg); 

		Transform npcBarTrans = this.transform.Find ("NpcBar");
		if (npcBarTrans == null) {
			return;
		}

		Transform scrollViewTrans = npcBarTrans.Find ("Scroll View");
		if (scrollViewTrans == null) {
			return;
		}

		Transform gridTrans = scrollViewTrans.Find ("UIGrid");
		if (gridTrans == null) {
			return;
		}

		List<Transform> gridList = gridTrans.GetComponent<UIGrid> ().GetChildList ();
		if (gridList == null) {

			return;
		}

		string cmd = item ["cmd"].ToString();
		string objId = item ["objId"].ToString();
		string displayName = item ["displayName"].ToString();

		Transform targetTrans = null;
		foreach(Transform gridItem in gridList){

			if (gridItem.GetComponent<CmdButtonItem> ().m_ObjId.Equals (objId)) {

				targetTrans = gridItem;
				break;
			}
		}

		if (transform == null) {

			return;
		}

		gridList.Remove (targetTrans);
		Destroy (targetTrans.gameObject);
		gridTrans.GetComponent<UIGrid> ().pivot = UIWidget.Pivot.TopLeft;
		gridTrans.GetComponent<UIGrid> ().repositionNow = true;
		gridTrans.GetComponent<UIGrid> ().Reposition ();
	}

	private void ClearNpcBar(){

		Transform npcBarTrans = this.transform.Find ("NpcBar");
		if (npcBarTrans == null) {
			return;
		}

		Transform scrollViewTrans = npcBarTrans.Find ("Scroll View");
		if (scrollViewTrans == null) {
			return;
		}

		Transform gridTrans = scrollViewTrans.Find ("UIGrid");
		if (gridTrans == null) {
			return;
		}

		UIGrid uiGrid = gridTrans.GetComponent<UIGrid> ();
		if (uiGrid == null) {

			return;
		}

		for(int k = 0;k<uiGrid.transform.childCount;k++)
		{
			GameObject go = uiGrid.transform.GetChild(k).gameObject;
			Destroy(go);
		}

		// 这个标记会让元素立即重新排列。
		uiGrid.repositionNow = true;
		uiGrid.Reposition();

		Transform mapUpDownTrans = this.transform.Find ("MapUpDown");
		if (mapUpDownTrans == null) {
			return;
		}

		Transform mapGridTrans = mapUpDownTrans.Find ("Grid");
		if (mapGridTrans == null) {
			return;
		}
			
		UIGrid mapUIGrid = mapGridTrans.GetComponent<UIGrid> ();
		if (mapUIGrid == null) {

			return;
		}

		for(int k = 0;k<mapUIGrid.transform.childCount;k++)
		{
			GameObject go = mapUIGrid.transform.GetChild(k).gameObject;
			Destroy(go);
		}

		// 这个标记会让元素立即重新排列。
		mapUIGrid.repositionNow = true;
		mapUIGrid.Reposition();
	}

	private void PopObjectInfoWindow(string msg){

		if (m_ObjectInfoPop == null) {

			GameObject objectInfoPopPerfab = Resources.Load("PopDialog") as GameObject; 
			m_ObjectInfoPop = GameObject.Instantiate(objectInfoPopPerfab) as GameObject;
			m_ObjectInfoPop.transform.parent = this.transform;
		}

		JObject jsonObject = JObject.Parse (msg);
		string descs = jsonObject ["descs"].ToString();
		string buttons = jsonObject ["buttons"].ToString();
		JArray jArray = JArray.Parse (buttons);

		//获取PopScrollView
		Transform popScrollView = m_ObjectInfoPop.transform.Find ("PopScrollView");
		Transform labelTrans = popScrollView.Find ("Label");
		Transform gridTrans = popScrollView.Find ("Grid");
		GameObject popCmdButtonPerfab = Resources.Load("PopCmdButton") as GameObject; 

		//获取下面的label赋值
		labelTrans.GetComponent<UILabel> ().text = descs;

		float x = labelTrans.GetComponent<UIWidget> ().localSize.y / 2f;
		labelTrans.localPosition = new Vector3 (0f, x, 0f);

		//获取grid列表添加按钮
		foreach (JObject item in jArray) {

			GameObject npcButton = GameObject.Instantiate(popCmdButtonPerfab) as GameObject;
			
			npcButton.GetComponent<CmdButtonItem> ().m_Cmd = item["cmd"].ToString();
			npcButton.GetComponent<CmdButtonItem> ().m_ObjId = item["objId"].ToString();
			npcButton.GetComponent<CmdButtonItem> ().m_DisplayName = item["displayName"].ToString();
			npcButton.transform.parent = gridTrans;
			npcButton.transform.localScale = new Vector3 (1f, 1f, 1f);
			npcButton.transform.localPosition = Vector3.zero; 
			npcButton.SetActive (true);
		}

		gridTrans.GetComponent<UIGrid> ().repositionNow = true;
		gridTrans.GetComponent<UIGrid> ().Reposition ();

		m_ObjectInfoPop.transform.localScale = new Vector3 (1f, 1f, 1f);
		m_ObjectInfoPop.transform.localPosition = new Vector3 (0f, 0f, 0f);
		m_ObjectInfoPop.SetActive (true);	
	}
}
