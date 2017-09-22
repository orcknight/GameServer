using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;

public class BagManager : MonoBehaviour {

	private Transform m_Grid = null;
	private Transform m_Money = null;
	private Transform m_Ticket = null;
	private Transform m_Load = null;

	// Use this for initialization
	void Start () {

		init ();
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void init(){

		if (m_Grid == null) {

			Transform itemsTrans = this.transform.Find ("Items");
			if (itemsTrans != null) {

				m_Grid = itemsTrans.Find ("ItemGrid");
			}
		}

		if (m_Money == null || m_Ticket == null || m_Load == null) {

			Transform moneyBarTrans = this.transform.Find ("MoneyBar");
			if (moneyBarTrans == null) {
				return;
			}

			Transform gridTrans = moneyBarTrans.Find ("Grid");
			if (gridTrans == null) {
				return;
			}

			m_Money = gridTrans.Find ("Money");

			Transform secondTrans = gridTrans.Find ("Second");
			m_Ticket = secondTrans.Find ("Ticket");
			m_Load = secondTrans.Find ("Load");
		}

	}
		
	public void setMoneyControl(string moneyStr){
		
		m_Money.Find ("Content").GetComponent<UILabel> ().text = moneyStr;

	}

	public void setLoadControl(string loadStr){
		
		m_Load.Find ("Content").GetComponent<UILabel> ().text = loadStr;

	}

	public void setTicketControl(string ticketStr){
		
		m_Ticket.Find ("Content").GetComponent<UILabel> ().text = ticketStr;

	}

	public void setBagItems(List<CmdButtonItem> cmdItems){

		if (m_Grid == null) {

			return;
		}

		m_Grid.DestroyChildren ();

		GameObject objectInfoPopPerfab = Resources.Load ("Common/BagItem") as GameObject; 
		for(int i = 0; i <cmdItems.Count; i++){

			CmdButtonItem cmdItem = cmdItems [i];

			GameObject item = GameObject.Instantiate (objectInfoPopPerfab) as GameObject;
			item.transform.SetParent (m_Grid.transform);
			item.transform.localScale = new Vector3 (1f, 1f, 1f);
			item.transform.localPosition = Vector3.zero; 
			CmdButtonItem itemButton = item.GetComponent<CmdButtonItem> ();
			itemButton.m_Cmd = cmdItem.m_Cmd;
			itemButton.m_ObjId = cmdItem.m_ObjId;
			itemButton.m_DisplayName = cmdItem.m_DisplayName;
			item.transform.Find ("Label").GetComponent<UILabel> ().text = cmdItem.m_DisplayName;
			item.SetActive (true);

			UIEventListener.Get(item).onClick = OnItemClick;  

		}

		m_Grid.GetComponent<UIGrid> ().repositionNow = true;

	}

	public void OnItemClick(GameObject obj){

		CmdButtonItem cmdButtonItem = obj.GetComponent<CmdButtonItem> ();
		JSONObject sendData = new JSONObject ();
		sendData.AddField ("target", cmdButtonItem.m_ObjId);
		JSONObject jsonObject = new JSONObject ();
		jsonObject.AddField ("cmd", cmdButtonItem.m_Cmd);
		jsonObject.AddField ("data", sendData);

		GameObject socketIo = GameObject.Find("SocketIO");
		socketIo.GetComponent<SocketIOComponent>().Emit("unity_stream", jsonObject);

	}

	public void OnClose(){

		this.transform.localScale = new Vector3 (1f, 1f, 1f);
		this.transform.localPosition = new Vector3 (5000f, 0f, 0f);
		this.gameObject.SetActive(false);
	}
}
