using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Net;
using System;
using System.IO;
using System.Text;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using SocketIO;

public class Login : MonoBehaviour {

	private CommonUIManager m_CommonUIManager = null;
	public UIInput m_UserName;
	public UIInput m_Password;

	// Use this for initialization
	void Start () {
		
		m_CommonUIManager = Singleton.getInstance ("CommonUIManager") as CommonUIManager;
		string userName = PlayerPrefs.GetString ("userName");
		string password = PlayerPrefs.GetString ("password");

		if (!string.IsNullOrEmpty (userName)) {
			m_UserName.value = userName;
		}
		if (!string.IsNullOrEmpty (password)) {
			m_Password.value = password;
		}
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public bool DoLogin(){

		if (m_UserName == null || m_Password == null) {
			m_CommonUIManager.ShowMessageBox(  
				"登陆错误",   
				"用户名、密码不能为空！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return false;
		}
			
		if (m_UserName.value.Length < 1 || m_Password.value.Length < 1) {
			m_CommonUIManager.ShowMessageBox(  
				"登陆错误",   
				"用户名、密码不能为空！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return false;
		}

		PlayerPrefs.SetString("userName", m_UserName.value);
		PlayerPrefs.SetString("password", m_Password.value);

		string uri = "http://game.gate.com/index.php/Home/User/login";
		string param = "id=" + m_UserName.value + "&pass=" + m_Password.value;
		string backMsg = "";
		HttpWebRequest request = HttpWebRequest.Create(uri + "?" + param) as HttpWebRequest;
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

		if (backMsg == null || backMsg.Length < 1) {

			m_CommonUIManager.ShowMessageBox(  
				"登陆错误",   
				"用户名或者密码错误！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return false;
		}

		int start = backMsg.IndexOf ("{");
		int end = backMsg.IndexOf ("}");
		backMsg = backMsg.Substring (start, end-start+1);

		JObject jo = (JObject)JsonConvert.DeserializeObject(backMsg);
		int code = int.Parse(jo["cod"].ToString());
		string sta = jo ["sta"].ToString();
		if (code == 0) {

			if (sta.Equals ("iderr")) {

				m_CommonUIManager.ShowMessageBox (
					"登陆错误",   
					"用户名错误！",  
					MessageBox.Style.OKAndCancel,  
					OnReceiveQuitConfirmResult); 

			} else if (sta.Equals ("passerr")) {

				m_CommonUIManager.ShowMessageBox (
					"登陆错误",   
					"密码错误！",  
					MessageBox.Style.OKAndCancel,  
					OnReceiveQuitConfirmResult); 

			} else {

				m_CommonUIManager.ShowMessageBox (
					"登陆错误",   
					"登陆出错！",  
					MessageBox.Style.OKAndCancel,  
					OnReceiveQuitConfirmResult);
			}

			return false;
		}

		//Todo:登陆成功
		//跳转到选择服务器列表
		LoadServerWindow();
		Destroy (this.gameObject);

		return true;
	}

	protected void OnReceiveQuitConfirmResult(MessageBox.Result result) {  
		//Singleton.RemoveInstance ("CommonUIManager");
		//m_CommonUIManager.root.transform.localPosition = new Vector3(0f, 0f, -3f);

	}   

	public void OnRegister(){

		GameObject signupPerfab = Resources.Load("Login/Signup") as GameObject;  
		GameObject signupObject = GameObject.Instantiate(signupPerfab) as GameObject;  
		signupObject.transform.parent = this.transform.parent;
		signupObject.transform.localScale = new Vector3 (1f, 1f, 1f);
		signupObject.transform.localPosition = Vector3.zero; 
		signupObject.SetActive (true);

		Destroy (this.gameObject);
	}

	private void LoadServerWindow(){

		GameObject serverListPerfab = Resources.Load("ServerList") as GameObject; 
		GameObject serverList = GameObject.Instantiate(serverListPerfab) as GameObject;  
		serverList.transform.parent = this.transform.parent;
		serverList.transform.localScale = new Vector3 (1f, 1f, 1f);
		serverList.transform.localPosition = Vector3.zero; 
		serverList.SetActive (true);
	}

}
