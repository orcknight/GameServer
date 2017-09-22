using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CombatStatusBar : MonoBehaviour {

	public int Qi { get; set; }
	public int MaxQi { get; set; }
	public int Neili {get;set;}
	public int MaxNeili {get;set;}

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void updateDisplay(){

		float qiPercent = 0f;
		float neiPercent = 0f;
		if (MaxQi > 0) {
			qiPercent = (float)Qi / (float)MaxQi;
		}

		if (MaxNeili > 0) {
			neiPercent = (float)Neili / (float)MaxNeili;
		}

		Transform hp = this.transform.Find ("Hp");
		Transform mp = this.transform.Find ("Mp");

		Transform hpLabel = hp.Find ("Thumb").Find ("Label");
		Transform mpLael = mp.Find ("Thumb").Find ("Label");

		string hpStr = Qi + "/" + MaxQi;
		string mpStr = Neili + "/" + MaxNeili;

		hp.GetComponent<UISlider> ().value = qiPercent;
		mp.GetComponent<UISlider> ().value = neiPercent;
		hpLabel.GetComponent<UILabel> ().text = hpStr;
		mpLael.GetComponent<UILabel> ().text = mpStr;
	}
}
