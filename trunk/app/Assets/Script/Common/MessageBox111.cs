using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MessageBox111 : MonoBehaviour {


	public string title;
	public string content;

	// Use this for initialization
	void Start () {

		UILabel titleLabel = this.transform.Find ("Title").GetComponent<UILabel>();
		UILabel contentLabel = this.transform.Find ("Content").GetComponent<UILabel>();
		titleLabel.text = title;
		contentLabel.text = content;
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
