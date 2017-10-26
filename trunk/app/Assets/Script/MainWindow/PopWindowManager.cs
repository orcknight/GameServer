using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Newtonsoft.Json.Linq;
using SocketIO;

public class PopWindowManager : MonoBehaviour {

	private GameObject m_TaskBar = null;
	private GameObject m_PopDialog = null;
	private GameObject m_Bag = null;
	private Transform m_InfoWindowTrans = null;
	private GameObject m_SocketIo = null;
	private SocketIOComponent m_Socket = null;

	// Use this for initialization
	void Start () {

		m_InfoWindowTrans = this.transform.Find ("InfoWindow");
		m_SocketIo = GameObject.Find("SocketIO");
		m_Socket = m_SocketIo.GetComponent<SocketIOComponent>();
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OpenByName(string name, string param = ""){

		if (name.Equals ("taskbar")) {

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

		} else if (name.Equals ("bag")) {

			if (m_Bag == null) {

				GameObject taskBarPerfab = Resources.Load ("MainWindow/Bag") as GameObject; 
				m_Bag = GameObject.Instantiate (taskBarPerfab) as GameObject;
				m_Bag.transform.parent = this.transform;
				m_Bag.transform.localScale = new Vector3 (1f, 1f, 1f);
				m_Bag.transform.localPosition = new Vector3 (126f, 80f, 0f);

			}

			JObject jObject = JObject.Parse (param);
			string money = jObject ["money"].ToString ();
			string load = jObject ["load"].ToString ();
			string ticket = jObject ["ticket"].ToString ();
			JArray items = JArray.Parse (jObject ["items"].ToString ());
			List<CmdButtonItem> itemList = new List<CmdButtonItem> ();
			for (int i = 0; i < items.Count; ++i) {

				CmdButtonItem cmdButton = new CmdButtonItem ();
				JObject item = JObject.Parse (items [i].ToString ()); 
				cmdButton.m_Cmd = item ["cmd"].ToString ();
				cmdButton.m_ObjId = item ["objId"].ToString ();
				cmdButton.m_DisplayName = item ["displayName"].ToString ();
				itemList.Add (cmdButton);
			}

			if (m_Bag.activeSelf == false) {

				m_Bag.transform.localScale = new Vector3 (1f, 1f, 1f);
				m_Bag.transform.localPosition = new Vector3 (126f, 80f, 0f);
				m_Bag.SetActive (true);
			}

			BagManager bagManager = m_Bag.GetComponent<BagManager> ();
			bagManager.init ();
			bagManager.setMoneyControl (money);
			bagManager.setLoadControl (load);
			bagManager.setTicketControl (ticket);
			bagManager.setBagItems (itemList);
		} else if (name.Equals ("popdialog")) {

			if (m_PopDialog == null) {

				GameObject objectInfoPopPerfab = Resources.Load ("MainWindow/PopDialog") as GameObject; 
				m_PopDialog = GameObject.Instantiate (objectInfoPopPerfab) as GameObject;
				m_PopDialog.GetComponent<ObjectInfoPopManger> ().closeCallback = OnObjectInfoPopQuitEventHandler;
				m_PopDialog.transform.parent = this.transform;
			}


			JObject jsonObject = JObject.Parse (param);
			string descs = jsonObject ["desc"].ToString();
			descs = descs.Replace ("\\n", "\n");
			string buttons = jsonObject ["buttons"].ToString();
			JArray jArray = JArray.Parse (buttons);

			//获取PopScrollView
			Transform popScrollView = m_PopDialog.transform.Find ("PopScrollView");
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
			m_PopDialog.transform.GetComponent<UIWidget> ().height = popHeight;
			m_PopDialog.transform.localPosition = new Vector3 (128f, popY, 0f);
			m_PopDialog.transform.localScale = new Vector3 (1f, 1f, 1f);
			m_PopDialog.SetActive (true);

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



	}

	//处理物品信息对话框的关闭事件
	protected void OnObjectInfoPopQuitEventHandler(){

		if (m_PopDialog == null) {

			return;
		}

		m_PopDialog.transform.localPosition = new Vector3 (5000f, 0f, 0f);
		m_PopDialog.SetActive (false);
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


	public void OnPopCmdButtonClick(GameObject obj){

		CmdButtonItem cmdButtonItem = obj.transform.GetComponent<CmdButtonItem> ();

		JSONObject sendData = new JSONObject ();
		sendData.AddField ("target", cmdButtonItem.m_ObjId);
		JSONObject jsonObject = new JSONObject ();
		jsonObject.AddField ("cmd", cmdButtonItem.m_Cmd);
		jsonObject.AddField ("data", sendData);
		m_Socket.Emit ("unity_stream", jsonObject);

		//关掉窗口
		if (m_PopDialog == null) {

			return;
		}

		m_PopDialog.transform.localPosition = new Vector3 (5000f, 0f, 0f);
		m_PopDialog.SetActive (false);
	}
}
