using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CommonUIDetail : MonoBehaviour {  

	public TweenPosition messageBoxTween;  
	public UILabel messageBoxTitle;  
	public UILabel messageBoxContent;  
	public GameObject[] buttonGroups = new GameObject[(int)MessageBox.Style.eNumCount];  

	public System.Action messageBoxConfirmCallback = null;  
	public System.Action messageBoxCancelCallback = null;  

	void OnConfirm() {  
		if (messageBoxConfirmCallback != null) {  
			messageBoxConfirmCallback();  
		}  
	}  

	void OnCancel() {  
		if (messageBoxCancelCallback != null) {  
			messageBoxCancelCallback();  
		}  
	}  
}  
