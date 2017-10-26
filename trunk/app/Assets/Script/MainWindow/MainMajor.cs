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
	private GameObject m_TaskBar = null;
	private GameObject m_Bag = null;
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
		m_Socket.On ("close", OnDisconnect);

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

    public void OnDisconnect(SocketIOEvent socketIoEvent) {

        Debug.Log(socketIoEvent.name);
		m_Socket.Close ();
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

            if (i == 0) {

                PlayerPrefs.SetString("playerName", item["value"].ToString());
            }

			if (item ["color"] != null) {
				
				tempBar.m_ForeColor = int.Parse (item ["color"].ToString ());
			}
			tempBar.RedrawBar ();

		}

		//Debug.Log (socketIoEvent.name);
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
            case ProtoCode.CLEAR_SCREEN_CODE:
                break;
            case ProtoCode.EMPTY_CODE:
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
			case ProtoCode.GAME_STORY_CODE:
				EnterStroyMode (msg);
				break;
			case ProtoCode.BAG_POP_CODE:
				OpenBag (msg);
				break;
			case ProtoCode.TASK_LIST_CODE:
				LoadTaskList (msg);
				break;
			default:
				break;
			}
				

		}


		//Debug.Log (socketIoEvent.name);
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

	public void OnPopCmdButtonClick(GameObject obj){

		CmdButtonItem cmdButtonItem = obj.transform.GetComponent<CmdButtonItem> ();

		JSONObject sendData = new JSONObject ();
		sendData.AddField ("target", cmdButtonItem.m_ObjId);
		JSONObject jsonObject = new JSONObject ();
		jsonObject.AddField ("cmd", cmdButtonItem.m_Cmd);
		jsonObject.AddField ("data", sendData);
		m_Socket.Emit ("unity_stream", jsonObject);

		//关掉窗口
		if (m_ObjectInfoPop == null) {

			return;
		}

		m_ObjectInfoPop.transform.localPosition = new Vector3 (5000f, 0f, 0f);
		m_ObjectInfoPop.SetActive (false);
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

	//监听手势信息
	void OnSwipe( SwipeGesture gesture ) 
	{
		// Total swipe vector (from start to end position)
		Vector2 move = gesture.Move;

		// Instant gesture velocity in screen units per second
		float velocity = gesture.Velocity;
		if (gesture.Direction == FingerGestures.SwipeDirection.Left) {

			if (Mathf.Abs (move.x) > 100) {
				Debug.Log ("left swipe");

				//发送tasklist消息
				JSONObject sendData = new JSONObject ();
				JSONObject jsonObject = new JSONObject ();
				jsonObject.AddField ("cmd", "tasklist");
				jsonObject.AddField ("data", sendData);
				m_Socket.Emit ("unity_stream", jsonObject);

				if (m_TaskBar == null) {

					GameObject taskBarPerfab = Resources.Load ("MainWindow/TaskBar") as GameObject; 
					m_TaskBar = GameObject.Instantiate (taskBarPerfab) as GameObject;
					m_TaskBar.transform.parent = this.transform;
					m_TaskBar.SetActive (false);
				}

				if (m_TaskBar.activeSelf == false) {

					m_TaskBar.transform.localScale = new Vector3 (1f, 1f, 1f);
					m_TaskBar.GetComponent<TweenTransform> ().from = GameObject.Find ("RightCenterAnchor").transform;
					m_TaskBar.GetComponent<TweenTransform> ().to = m_InfoWindowTrans;
					m_TaskBar.GetComponent<TweenTransform> ().PlayForward();
					m_TaskBar.SetActive (true);
				}
			}
		} else if (gesture.Direction == FingerGestures.SwipeDirection.Right) {

			if (m_TaskBar == null) {

				GameObject taskBarPerfab = Resources.Load ("MainWindow/TaskBar") as GameObject; 
				m_TaskBar = GameObject.Instantiate (taskBarPerfab) as GameObject;
				m_TaskBar.transform.parent = this.transform;
			}

			if (m_TaskBar.activeSelf == true) {

				m_TaskBar.transform.localScale = new Vector3 (1f, 1f, 1f);
				m_TaskBar.transform.localPosition = new Vector3 (5000f, 0f, 0f);
				m_TaskBar.SetActive (false);
			}
		}
	}

	public void OnBottomBarClick(GameObject obj){

		if (obj.name.Equals ("BtnTaskList")) {

			//发送tasklist消息
			JSONObject sendData = new JSONObject ();
			JSONObject jsonObject = new JSONObject ();
			jsonObject.AddField ("cmd", "tasklist");
			jsonObject.AddField ("data", sendData);
			m_Socket.Emit ("unity_stream", jsonObject);

			if (m_TaskBar == null) {

				GameObject taskBarPerfab = Resources.Load ("MainWindow/TaskBar") as GameObject; 
				m_TaskBar = GameObject.Instantiate (taskBarPerfab) as GameObject;
				m_TaskBar.transform.parent = this.transform;
				m_TaskBar.SetActive (false);
			}

			if (m_TaskBar.activeSelf == false) {

				m_TaskBar.transform.localScale = new Vector3 (1f, 1f, 1f);
				m_TaskBar.GetComponent<TweenTransform> ().from = GameObject.Find ("RightCenterAnchor").transform;
				m_TaskBar.GetComponent<TweenTransform> ().to = m_InfoWindowTrans;
				m_TaskBar.GetComponent<TweenTransform> ().PlayForward ();
				m_TaskBar.SetActive (true);
			}
		} else if (obj.name.Equals ("BtnBag")) {
			
			JSONObject sendData = new JSONObject ();
			JSONObject jsonObject = new JSONObject ();
			jsonObject.AddField ("cmd", "bag");
			jsonObject.AddField ("data", sendData);
			m_Socket.Emit ("unity_stream", jsonObject);
		}
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
			npcButton.name = objId;
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

		if (targetTrans == null) {

			return;
		}

		gridTrans.gameObject.SetActive (false);
		gridTrans.GetComponent<UIGrid> ().RemoveChild (targetTrans);
		Destroy (targetTrans.gameObject);
		gridTrans.GetComponent<UIGrid> ().pivot = UIWidget.Pivot.TopLeft;
		gridTrans.GetComponent<UIGrid> ().repositionNow = true;
		gridTrans.gameObject.SetActive (true);
		//scrollViewTrans.GetComponent<UIScrollView> ().contentPivot = UIWidget.Pivot.TopLeft;
		//scrollViewTrans.GetComponent<UIScrollView> ().ResetPosition ();
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

			GameObject objectInfoPopPerfab = Resources.Load ("MainWindow/PopDialog") as GameObject; 
			m_ObjectInfoPop = GameObject.Instantiate (objectInfoPopPerfab) as GameObject;
			m_ObjectInfoPop.GetComponent<ObjectInfoPopManger> ().closeCallback = OnObjectInfoPopQuitEventHandler;
			m_ObjectInfoPop.transform.parent = this.transform;
		}

	
		JObject jsonObject = JObject.Parse (msg);
		string descs = jsonObject ["desc"].ToString();
		descs = descs.Replace ("\\n", "\n");
		string buttons = jsonObject ["buttons"].ToString();
		JArray jArray = JArray.Parse (buttons);

		//获取PopScrollView
		Transform popScrollView = m_ObjectInfoPop.transform.Find ("PopScrollView");
		Transform labelTrans = popScrollView.Find ("Label");
		Transform gridTrans = popScrollView.Find ("Grid");
		ClearGridChilds (gridTrans);
		GameObject popCmdButtonPerfab = Resources.Load("Common/PopCmdButton") as GameObject; 

		//获取下面的label赋值
		labelTrans.GetComponent<UILabel> ().text = descs;
		float labelHeight = labelTrans.GetComponent<UIWidget> ().localSize.y;
		float gridHeight = 80f * Mathf.Ceil(((float)jArray.Count) / 4f);

		//计算弹出窗口的高度
		int popHeight = (int)(labelHeight + gridHeight + 100f);
		if (popHeight > 625) {

			popHeight = 625;
		}

		float popY = (688f - (float)popHeight / 2f);
		m_ObjectInfoPop.transform.GetComponent<UIWidget> ().height = popHeight;
		m_ObjectInfoPop.transform.localPosition = new Vector3 (128f, popY, 0f);
		m_ObjectInfoPop.transform.localScale = new Vector3 (1f, 1f, 1f);
		m_ObjectInfoPop.SetActive (true);

		float labelY = ((float)popHeight/2f - (float)labelHeight / 2f - 20f);
		labelTrans.localPosition = new Vector3 (0f, labelY, 0f);

		float gridY = ((float)popHeight/2f - (float)labelHeight - 40f - gridHeight/2f);

		//获取grid列表添加按钮
		for (int i = 0; i < jArray.Count; ++i) {

			JObject item = JObject.Parse (jArray [i].ToString ()); 
			GameObject popCmdButton = GameObject.Instantiate(popCmdButtonPerfab) as GameObject;
			
			popCmdButton.GetComponent<CmdButtonItem> ().m_Cmd = item["cmd"].ToString();
			popCmdButton.GetComponent<CmdButtonItem> ().m_ObjId = item["objId"].ToString();
			popCmdButton.GetComponent<CmdButtonItem> ().m_DisplayName = item["displayName"].ToString();
			popCmdButton.transform.Find ("Label").GetComponent<UILabel> ().text = item ["displayName"].ToString ();
			popCmdButton.transform.parent = gridTrans;
			popCmdButton.transform.localScale = new Vector3 (1f, 1f, 1f);
			popCmdButton.transform.localPosition = Vector3.zero; 
			popCmdButton.SetActive (true);

			UIEventListener.Get(popCmdButton).onClick = OnPopCmdButtonClick;  
		}
			
		gridTrans.GetComponent<UIGrid> ().repositionNow = true;
		gridTrans.GetComponent<UIGrid> ().Reposition ();
		gridTrans.transform.localPosition = new Vector3 (gridTrans.transform.localPosition.x, gridY, 0f);
		gridTrans.gameObject.SetActive (true);
	}

	private void ClearGridChilds(Transform gridTrans){

		UIGrid uiGrid = gridTrans.GetComponent<UIGrid> ();
		for(int k = 0;k<uiGrid.transform.childCount;k++)
		{
			GameObject go = uiGrid.transform.GetChild(k).gameObject;
			Destroy(go);
		}

		// 这个标记会让元素立即重新排列。
		uiGrid.repositionNow = true;
		uiGrid.Reposition();
	}

	private void EnterStroyMode(string msg){

        JObject taskStory = JObject.Parse(msg);

        int trackId = int.Parse(taskStory["trackId"].ToString());
        int trackActionId = int.Parse(taskStory["trackActionId"].ToString());
        int rewardId = int.Parse(taskStory["rewardId"].ToString());

        List<Story> stories = new List<Story> ();
		JArray jlist = JArray.Parse (taskStory["stories"].ToString()); 
		for (int i = 0; i < jlist.Count; ++i) {

			Story story = new Story ();
			JObject item = JObject.Parse (jlist [i].ToString ()); 

			JToken idToken = null;
			JToken typeToken = null;
			JToken nameToken = null;
			JToken saidToken = null;
			if (item.TryGetValue ("id", out idToken)) {
				story.Id = int.Parse(idToken.ToString ());
			}
			if (item.TryGetValue ("type", out typeToken)) {
				story.Type = typeToken.ToString ();
			}
			if (item.TryGetValue ("name", out nameToken)) {
				story.Name = nameToken.ToString ();
			}
			if (item.TryGetValue ("said", out saidToken)) {
				story.Said = saidToken.ToString ();
			}
			stories.Add (story);
		}

		Transform npcTalkWindow = this.transform.Find ("NpcTalkWindow");
        npcTalkWindow.GetComponent<NpcTaskWindow>().stories = stories;
        npcTalkWindow.GetComponent<NpcTaskWindow>().CurrentId = 1;
        npcTalkWindow.GetComponent<NpcTaskWindow>().TrackId = trackId;
        npcTalkWindow.GetComponent<NpcTaskWindow>().TrackActionId = trackActionId;
        npcTalkWindow.GetComponent<NpcTaskWindow>().RewardId = rewardId;
        npcTalkWindow.GetComponent<NpcTaskWindow>().DispalyContent();
        npcTalkWindow.localPosition = new Vector3(0f, 65f, 0f);
        npcTalkWindow.localScale = new Vector3(1f, 1f, 1f);
        if (npcTalkWindow.gameObject.activeSelf == false) {
            npcTalkWindow.gameObject.SetActive(true);
        }

        return;

	}

	private void OpenBag(string msg){

		if (m_Bag == null) {

			GameObject taskBarPerfab = Resources.Load ("MainWindow/Bag") as GameObject; 
			m_Bag = GameObject.Instantiate (taskBarPerfab) as GameObject;
			m_Bag.transform.parent = this.transform;
			m_Bag.transform.localScale = new Vector3 (1f, 1f, 1f);
			m_Bag.transform.localPosition = new Vector3 (126f, 80f, 0f);

		}

		JObject jObject = JObject.Parse (msg);
		string money = jObject ["money"].ToString ();
		string load = jObject ["load"].ToString ();
		string ticket = jObject ["ticket"].ToString ();
		JArray items = JArray.Parse (jObject ["items"].ToString ());
		List<CmdButtonItem> itemList = new List<CmdButtonItem> ();
		for (int i = 0; i < items.Count; ++i) {

			CmdButtonItem cmdButton = new CmdButtonItem ();
			JObject item = JObject.Parse (items [i].ToString ()); 
			cmdButton.m_Cmd = item ["cmd"].ToString();
			cmdButton.m_ObjId = item ["objId"].ToString();
			cmdButton.m_DisplayName = item ["displayName"].ToString();
			itemList.Add (cmdButton);
		}

		if (m_Bag.activeSelf == false) {

			m_Bag.transform.localScale = new Vector3 (1f, 1f, 1f);
			m_Bag.transform.localPosition = new Vector3 (126f, 80f, 0f);
			m_Bag.SetActive (true);
		}

		BagManager bagManager = m_Bag.GetComponent<BagManager> ();
		bagManager.init ();
		bagManager.setMoneyControl(money);
		bagManager.setLoadControl (load);
		bagManager.setTicketControl (ticket);
		bagManager.setBagItems (itemList);
	}

	private void LoadTaskList(string msg){

		JArray tracks = JArray.Parse (msg);
		List<Track> trackList = new List<Track> ();
		for (int i = 0; i < tracks.Count; ++i) {

			Track track = new Track ();
			JObject item = JObject.Parse (tracks [i].ToString ()); 
			track.Id = int.Parse( item ["id"].ToString());
			track.Desc = item ["desc"].ToString();

			TrackAction trackAction = new TrackAction ();

			JObject actionObject = JObject.Parse (item ["action"].ToString ());
			trackAction.Desc = actionObject ["desc"].ToString ();
			trackAction.Id = int.Parse (actionObject ["id"].ToString ());
			track.Action = trackAction;

			trackList.Add (track);
		}

		m_TaskBar.GetComponent<TaskBar> ().InitTaskData (trackList);

	}
}
