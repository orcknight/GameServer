using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MessageBox {  
	public delegate void OnReceiveMessageBoxResult(MessageBox.Result result);  

	public enum Style {  
		OnlyOK,  
		OKAndCancel,  
		eNumCount  
	}  

	public enum Result {  
		OK,  
		Cancel,  
		eNumCount  
	}  
}  

public class CommonUIManager : MonoBehaviour {  

	public GameObject commonUIPrefab = null;  

	public GameObject root;  
	public TweenPosition messageBoxTween;  
	public UILabel messageBoxTitle;  
	public UILabel messageBoxContent;  
	public GameObject[] buttonGroups = new GameObject[(int)MessageBox.Style.eNumCount];  

	private MessageBox.OnReceiveMessageBoxResult messageBoxCallback = null;  

	// Use this for initialization  
	void Start () {

	}  

	// Update is called once per frame  
	void Update () {  

	}  

	public void ShowMessageBox(string title, string content, MessageBox.Style style,   
		MessageBox.OnReceiveMessageBoxResult callback) {  
		if (root == null) {  
			commonUIPrefab = Resources.Load("MessageBox") as GameObject;  
			root = GameObject.Instantiate(commonUIPrefab) as GameObject;  
			root.transform.parent = this.transform;  


			CommonUIDetail uiDetail = root.GetComponent<CommonUIDetail>();  
			messageBoxTween = uiDetail.messageBoxTween;  
			messageBoxTitle = uiDetail.messageBoxTitle;  
			messageBoxContent = uiDetail.messageBoxContent;  
			buttonGroups[(int)MessageBox.Style.OnlyOK] = uiDetail.buttonGroups[(int)MessageBox.Style.OnlyOK];  
			buttonGroups[(int)MessageBox.Style.OKAndCancel] = uiDetail.buttonGroups[(int)MessageBox.Style.OKAndCancel];  

			uiDetail.messageBoxConfirmCallback = OnConfirm;  
			uiDetail.messageBoxCancelCallback = OnCancel;  
		}  

		root.transform.localPosition = Vector3.zero;
		messageBoxTitle.text = title;  
		messageBoxContent.text = content;  
		messageBoxCallback = callback;  

		switch ((int)style) {  
		case (int)MessageBox.Style.OnlyOK:  
			buttonGroups[(int)MessageBox.Style.OnlyOK].SetActive(true);  
			buttonGroups[(int)MessageBox.Style.OKAndCancel].SetActive(false);  
			break;  
		case (int)MessageBox.Style.OKAndCancel:  
			buttonGroups[(int)MessageBox.Style.OnlyOK].SetActive(true);  
			buttonGroups[(int)MessageBox.Style.OKAndCancel].SetActive(true);  
			break;  
		}  

		root.SetActive (true);
	}  

	void OnConfirm() {  
		if (messageBoxCallback != null) {  
			messageBoxCallback(MessageBox.Result.OK);  
			messageBoxCallback = null;  
		}  

		//messageBoxTween.Play(false);  
		root.SetActive (false);
	}  

	void OnCancel() {  
		if (messageBoxCallback != null) {  
			messageBoxCallback(MessageBox.Result.Cancel);  
			messageBoxCallback = null;  
		}  

		//messageBoxTween.Play(false);  

		root.SetActive (false);
	}  
}  

