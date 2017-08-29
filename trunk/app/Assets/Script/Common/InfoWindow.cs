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

		m_TextList.Add("[00FF00]" +
			"欢迎登陆仗剑天涯！[-]");
		m_TextList.Add("[00FF00]" +
			"本服管理员QQ：407496032，有问题请联系管理员！[-]");

		if (m_TextList != null)
		{
			for (int i = 0; i < 30; ++i)
			{
				m_TextList.Add("[00FF00]" +
					"This is an example paragraph for the text list, testing line " + i + "[-]");
			}
		}
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
