using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InfoWindow : MonoBehaviour {

	private UITextList m_TextList = null;

	// Use this for initialization
	void Start () {

		m_TextList = GetComponentInChildren<UITextList> ();
		if (m_TextList == null) {

			return;
		}
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
