using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Net;
using System;
using System.IO;
using System.Text;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

public class ServerListContext : BaseContext {
    public ServerListContext()
        : base(UIType.ServerList) {

    }
}

public class ServerListView : BaseView {

	private CommonUIManager m_CommonUIManager = null;
	private GameObject m_Selected = null;
	private GameObject m_Recommand = null;
	private GameObject m_Last = null;

	// Use this for initialization
	void Start () {

		m_CommonUIManager = Singleton.getInstance ("CommonUIManager") as CommonUIManager;
		Transform mainTrans = this.transform.Find ("MainList");
		Transform subTrans = this.transform.Find ("OtherList");

		string lastServerName = PlayerPrefs.GetString ("lastServerName");
		GameObject buttonPerfab = Resources.Load("Common/CustomButton") as GameObject; 

		JArray jlist = getServerList ();
		if (jlist == null) {

			return;
		}
		  
		for(int i = 0; i < jlist.Count ; ++i)  //遍历JArray  
		{  

			GameObject signupObject = GameObject.Instantiate(buttonPerfab) as GameObject;  
			signupObject.transform.parent = subTrans;
			signupObject.transform.localScale = new Vector3 (1f, 1f, 1f);
			signupObject.transform.localPosition = Vector3.zero; 
			signupObject.tag = "ServerButton";
			signupObject.SetActive (true);

			JObject tempo = JObject.Parse(jlist[i].ToString());  
			ServerItem serverItem = signupObject.GetComponent<ServerItem> ();
			serverItem.m_Id = int.Parse(tempo["id"].ToString());
			serverItem.m_ServerName = tempo ["name"].ToString ();
			serverItem.m_Value = tempo ["value"].ToString ();
			serverItem.m_Url = tempo ["url"].ToString ();
			serverItem.m_IsHot = byte.Parse(tempo ["ishot"].ToString ());

			Transform label = signupObject.transform.Find ("Label");
			label.GetComponent<UILabel> ().text = serverItem.m_Value;

			if (serverItem.m_IsHot == 1 && serverItem.m_ServerName.Equals (lastServerName)) {

				m_Recommand = GameObject.Instantiate (signupObject);
				m_Last = GameObject.Instantiate (signupObject);
			} else if (serverItem.m_IsHot == 1) {

				m_Recommand = GameObject.Instantiate (signupObject);
			} else if (serverItem.m_ServerName.Equals (lastServerName)) {

				m_Last = GameObject.Instantiate (signupObject);
			}

			signupObject.transform.parent = subTrans;
			signupObject.SetActive (true);
		}

		if (m_Recommand != null) {

			m_Recommand.transform.parent = mainTrans;
			m_Recommand.transform.localScale = new Vector3 (1f, 1f, 1f);
			m_Recommand.transform.localPosition = Vector3.zero; 
			m_Recommand.SetActive (true);
		}

		if (m_Last != null) {

			m_Last.transform.parent = mainTrans;
			m_Last.transform.localScale = new Vector3 (1f, 1f, 1f);
			m_Last.transform.localPosition = Vector3.zero; 
			m_Last.SetActive (true);
		}

		GameObject[] objs = GameObject.FindGameObjectsWithTag ("ServerButton");

		foreach(GameObject obj in objs){

			UIEventListener.Get(obj).onClick = OnButtonClick;  
			UIEventListener.Get(obj).onHover = OnButtonHover;  
		}
		//设置这个按钮的监听，指向本类的ButtonClick方法中。  

	}
	
	// Update is called once per frame
	void Update () {
		
	}


 	public void OnButtonClick(GameObject obj)
	{
		if (obj.Equals (m_Selected)) {
			return;
		}

		if (m_Selected != null) {
			m_Selected.GetComponent<UISprite> ().spriteName = "Dialog-Background";
		}
		m_Selected = obj;
		obj.GetComponent<UISprite> ().spriteName = "Red-Button-Outline";
		Debug.Log("我是按钮2被点击了");
	}

	public void OnButtonHover(GameObject obj, bool state){

		if (obj.Equals (m_Selected)) {

			return;
		}

		if (state) {
			obj.GetComponent<UISprite> ().spriteName = "Red-Button-Outline";
		} else {
			obj.GetComponent<UISprite> ().spriteName = "Dialog-Background";	
		}
	}

	public void OnEnter(){

		if (m_Selected == null && m_Last == null && m_Recommand == null) {

			m_CommonUIManager.ShowMessageBox(  
				"选择服务器错误",   
				"请选择要登录的服务器！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;

		}

		GameObject usedServer = null;
		if (m_Selected != null) {

			usedServer = m_Selected;
		} else if (m_Last != null) {

			usedServer = m_Last;
		} else if (m_Recommand != null) {

			usedServer = m_Recommand;
		}

		//保存这次登录的服务器
		ServerItem serverItem = usedServer.GetComponent<ServerItem> ();
		PlayerPrefs.SetString ("lastServerName", serverItem.m_ServerName);

		//打开主窗口，销毁服务器选择窗口
		LoadMainWindow();
		Destroy (this.gameObject);
	}


	public void OnBack(){

        Singleton<ContextManager>.Instance.Pop();
        return;

        GameObject loginPerfab = Resources.Load("Login/Login") as GameObject; 
		GameObject login = GameObject.Instantiate(loginPerfab) as GameObject;  
		login.transform.parent = this.transform.parent;
		login.transform.localScale = new Vector3 (1f, 1f, 1f);
		login.transform.localPosition = Vector3.zero; 
		login.SetActive (true);

		Destroy (this.gameObject);
	}

	protected void OnReceiveQuitConfirmResult(MessageBox.Result result) {  
		//Singleton.RemoveInstance ("CommonUIManager");
		//m_CommonUIManager.root.transform.localPosition = new Vector3(0f, 0f, -3f);

	}  

	private JArray getServerList(){

		string uri = "http://bxu2359290536.my3w.com/index.php/Home/ServerList/getServerList";
		string backMsg = "";
		HttpWebRequest request = HttpWebRequest.Create(uri) as HttpWebRequest;
		request.ContentType = "application/x-www-form-urlencoded;charset=UTF-8";
		request.Method = "GET";                            //请求方法
		request.ProtocolVersion = new Version(1, 1);   //Http/1.1版本

		WebResponse response = request.GetResponse();  
		Stream responseStream = response.GetResponseStream();  
		StreamReader reader = new System.IO.StreamReader(responseStream, Encoding.UTF8);  
		backMsg = reader.ReadToEnd(); 

		reader.Close();  
		reader.Dispose();  
		responseStream.Close();  
		responseStream.Dispose(); 

		List<ServerItem> list = new List<ServerItem> ();
		JObject jo = (JObject)JsonConvert.DeserializeObject(backMsg);
		string data = jo ["data"].ToString();

		if (string.IsNullOrEmpty (data)) {
			return null;
		}

		JArray jlist = JArray.Parse(data); //将pois部分视为一个JObject，JArray解析这个JObject的字符串  

		return jlist;
	}

	private void LoadMainWindow(){

        Singleton<ContextManager>.Instance.Push(new MainWindowContext());
        return;

        GameObject mainWindowPerfab = Resources.Load("MainWindow") as GameObject; 
		GameObject mainWindow = GameObject.Instantiate(mainWindowPerfab) as GameObject;  
		mainWindow.transform.parent = this.transform.parent;
		mainWindow.transform.localScale = new Vector3 (1f, 1f, 1f);
		mainWindow.transform.localPosition = Vector3.zero; 
		mainWindow.SetActive (true);
	}

    public override void OnEnter(BaseContext context) {
        base.OnEnter(context);

        this.transform.localScale = new Vector3(1f, 1f, 1f);
        this.transform.localPosition = new Vector3(0f, 0f, 0f);
    }

    public override void OnExit(BaseContext context) {
        base.OnExit(context);

        this.transform.localScale = new Vector3(1f, 1f, 1f);
        this.transform.localPosition = new Vector3(5000f, 0f, 0f);
    }

    public override void OnPause(BaseContext context) {
        base.OnPause(context);

        this.transform.localScale = new Vector3(1f, 1f, 1f);
        this.transform.localPosition = new Vector3(5000f, 0f, 0f);
    }

    public override void OnResume(BaseContext context) {
        base.OnResume(context);

        this.transform.localScale = new Vector3(1f, 1f, 1f);
        this.transform.localPosition = new Vector3(0f, 0f, 0f);
    }
}
