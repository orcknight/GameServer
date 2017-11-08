using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Text.RegularExpressions;
using SocketIO;

public class CreateRoleContext : BaseContext {
    public CreateRoleContext()
        : base(UIType.CreateRole) {

    }
}

public class CreateRoleView : BaseView {

	// Use this for initialization
	void Start () {

		string lastServerName = PlayerPrefs.GetString ("lastServerName");
		Transform title = this.transform.Find ("Title");
		title.GetComponent<UILabel> ().text = "欢迎登陆 [" + lastServerName + "]";
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OnButtonClick(){

		CommonUIManager commonUIManager = Singleton.getInstance ("CommonUIManager") as CommonUIManager;

		Transform content = this.transform.Find ("NickName").Find ("content");

		string nickname = content.GetComponent<UIInput>().value;

		if (string.IsNullOrEmpty (nickname)) {

			commonUIManager.ShowMessageBox(  
				"创建错误",   
				"昵称不能为空！",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;
		}

		if (nickname.Length < 2 || nickname.Length > 6) {

			commonUIManager.ShowMessageBox(  
				"创建错误",   
				"对不起，请您用「中文」取名字(2-6个字)。",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;
		}

		if (!IsChinese (nickname)) {

			commonUIManager.ShowMessageBox(  
				"创建错误",   
				"对不起，请您用「中文」取名字(2-6个字)。",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return;

		}

		string gender = GetGenderString ();
		if (string.IsNullOrEmpty (gender)) {

			commonUIManager.ShowMessageBox(  
				"创建错误",   
				"请选择性别。",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult); 
			return; 
		}

		GameObject socketIo = GameObject.Find("SocketIO");

		SocketIOComponent socket = socketIo.GetComponent<SocketIOComponent>();
		JSONObject data = new JSONObject ();
		data.AddField ("name", nickname);
		data.AddField ("gender", gender);
		JSONObject jsonObject = new JSONObject ();
		jsonObject.AddField ("cmd", "createrole");
		jsonObject.AddField ("data", data);
		socket.Emit("unity_stream", jsonObject);

	}
		
	public bool IsChinese(string strChinese)
	{
		bool b = true;
		for (int i = 0; i < strChinese.Length; i++)
		{
			Regex reg = new Regex(@"[\u4e00-\u9fa5]");
			if (!reg.IsMatch(strChinese[i].ToString()))
			{

				b = false;
				break;
			}
		}

		return b;

	}

	protected void OnReceiveQuitConfirmResult(MessageBox.Result result) {  

	}   


	private string GetGenderString(){

		Transform gender = this.transform.Find ("Gender");

		if (gender == null) {


			return "";
		}

		Transform male = gender.Find ("Male");
		Transform female = gender.Find ("Female");


		if (male.GetComponent<UIToggle> ().value) {

			Transform label = male.Find ("Label");
			return label.GetComponent<UILabel> ().text;
		} else {

			Transform label = female.Find ("Label");
			return label.GetComponent<UILabel> ().text;
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
