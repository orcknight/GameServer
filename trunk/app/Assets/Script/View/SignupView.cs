using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Net;
using System.IO;
using System.Text;
using System;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

public class SignupContext : BaseContext {
    public SignupContext()
        : base(UIType.Signup) {

    }
}

public class SignupView : BaseView {

	private CommonUIManager m_CommonUIManager = null;
	public UIInput userName;
	public UIInput phone;
	public UIInput email;
	public UIInput password;
	public UIInput confirmPassword;

	// Use this for initialization
	void Start () {
		m_CommonUIManager = Singleton.getInstance("CommonUIManager") as CommonUIManager;  	
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OnRegister(){

		if (userName == null || phone == null || email == null || password == null || confirmPassword == null) {
			m_CommonUIManager.ShowMessageBox(  
				"注册错误",   
				"所有内容不能为空！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;
		}

		if (userName.value.Length < 1|| phone.value.Length <1 || email.value.Length < 1 || 
			password.value.Length < 1 || confirmPassword.value.Length < 1) {
			m_CommonUIManager.ShowMessageBox(  
				"注册错误",   
				"所有内容不能为空！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;
		}

		if (!password.value.Equals (confirmPassword.value)) {

			m_CommonUIManager.ShowMessageBox(  
				"注册错误",   
				"两次输入的密码不同！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;
		}

		string uri = "http://game.gate.com/index.php/Home/User/signUp";
		string param = "id=" + userName.value + "&pass=" + password.value +
			"&pass2=" + confirmPassword.value + "&phone=" + phone.value + 
			"&email=" + email.value;
		string backMsg = "";
		HttpWebRequest request = HttpWebRequest.Create(uri) as HttpWebRequest;
		request.ContentType = "application/x-www-form-urlencoded;charset=UTF-8";
		byte[] paramArray = Encoding.UTF8.GetBytes (param);
		request.Method = "POST";                            //请求方法
		request.ProtocolVersion = new Version(1, 1);   //Http/1.1版本

		Stream requestStream = null;
		if (!string.IsNullOrEmpty (param)) {
			requestStream = request.GetRequestStream ();
			requestStream.Write (paramArray, 0, param.Length);
			requestStream.Close ();
		}

		WebResponse response = request.GetResponse();  
		Stream responseStream = response.GetResponseStream();  
		StreamReader reader = new System.IO.StreamReader(responseStream, Encoding.UTF8);  
		backMsg = reader.ReadToEnd(); 

		reader.Close();  
		reader.Dispose();  

		requestStream.Dispose ();
		responseStream.Close();  
		responseStream.Dispose(); 


		if (backMsg == null || backMsg.Length < 1) {

			m_CommonUIManager.ShowMessageBox(  
				"登陆错误",   
				"用户名或者密码错误！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;
		}

		int start = backMsg.IndexOf ("{");
		int end = backMsg.IndexOf ("}");
		backMsg = backMsg.Substring (start, end-start+1);

		JObject jo = (JObject)JsonConvert.DeserializeObject(backMsg);
		int code = int.Parse(jo["code"].ToString());
		string msg = jo ["msg"].ToString();
		if (code == 0) {
			m_CommonUIManager.ShowMessageBox (
				"注册错误",   
				msg,  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;
		}


	}
		
	public void OnRegistered(){

        Singleton<ContextManager>.Instance.Pop();
        return;

        GameObject loginPrefab = Resources.Load("Login/Login") as GameObject;  
		GameObject loginObject = GameObject.Instantiate(loginPrefab) as GameObject;  
		loginObject.transform.parent = this.transform.parent;
		loginObject.transform.localScale = new Vector3 (1f, 1f, 1f);
		loginObject.transform.localPosition = Vector3.zero;
		loginObject.SetActive (true);

		Destroy (this.gameObject);
	}

	protected void OnReceiveQuitConfirmResult(MessageBox.Result result) {  
		if (result == MessageBox.Result.OK) {  
			Application.Quit();  
		}  
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
