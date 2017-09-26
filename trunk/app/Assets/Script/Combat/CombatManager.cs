using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CombatManager : MonoBehaviour {

	// Use this for initialization
	void Start () {

		Transform combatInfo = this.transform.Find ("CombatInfo");
		Transform combatScroll = combatInfo.Find ("CombatScroll");
		UITextList uiTextList = combatScroll.GetComponent<UITextList> ();

		for (int i = 0; i < 100; i++) {
			uiTextList.Add ("this is a test " + i);
		}


		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
