using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class KeyDetecter : MonoBehaviour {

	private CommonUIManager m_CommonUIManager = null;  

	void Start() {  
		m_CommonUIManager = Singleton.getInstance("CommonUIManager") as CommonUIManager;  
	}  

	// Update is called once per frame  
	void Update () {  
		if (Input.GetKey(KeyCode.Escape) && m_CommonUIManager != null) {          
			m_CommonUIManager.ShowMessageBox(  
				"退出确认",   
				"继续将退出游戏。\n确定退出？",  
				MessageBox.Style.OKAndCancel,  
				OnReceiveQuitConfirmResult);  
		}  

	}  

	void OnReceiveQuitConfirmResult(MessageBox.Result result) {  
		if (result == MessageBox.Result.OK) {  
			Application.Quit();  
		}  
	}  
}
