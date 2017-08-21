using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class background : MonoBehaviour {

	private UISprite backImg;
	private UIRoot root;

	// Use this for initialization
	void Start () {

		backImg = this.GetComponent<UISprite> ();
		root = GameObject.FindGameObjectWithTag ("UIRoot").GetComponent<UIRoot>();
		Adjust ();
	}
	
	// Update is called once per frame
	void Update () {
		
	}


	private void Adjust(){

		float adjHeight = (float)root.activeHeight / Screen.height;
		int height = Mathf.CeilToInt (Screen.height * adjHeight);
		int width = Mathf.CeilToInt (Screen.width * adjHeight);

		backImg.height = height;
		backImg.width = width;
	}

}
