using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AttrProgressBarManger : MonoBehaviour {

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
		
	public void setThumbText(string text){

		UILabel uiLabel = this.transform.Find ("Thumb").Find("Label").GetComponent<UILabel>();
		uiLabel.text = text;
	}

}
