using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class test : MonoBehaviour {

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void MakeItRed()
	{
		GetComponent<UIWidget> ().color = Color.red;
	}

	public void MakeItGreen()
	{
		GetComponent<UIWidget> ().color = Color.green;
	}

	public void MakeItBlue()
	{
		GetComponent<UIWidget> ().color = Color.blue;
	}
}
