using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ObjectInfoPopManger : MonoBehaviour {

	public System.Action closeCallback = null;  

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}


	public void OnClose(){

		if (closeCallback != null) {
			
			closeCallback ();
		}
	}
}
