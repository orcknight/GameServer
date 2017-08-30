using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HpBar : MonoBehaviour {

	public int m_Max = 0; //最大值
	public int m_Eff = 0; //有效值
	public int m_Cur = 0; //当前值
	public string m_Desc = "杨威"; //描述字串
	public int m_ForeColor = 0xD5D5D5; //前景颜色

	// Use this for initialization
	void Start () {

		RedrawBar ();
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}


	public void RedrawBar(){

		Transform fore = this.transform.Find ("Fore");
		Transform eff = this.transform.Find ("Eff");
		Transform content = this.transform.Find ("Content");

		//设置血条文字
		content.GetComponent<UILabel> ().text = m_Desc;

		float alpha = ((m_ForeColor & 0xff000000) >> 24) / 255;
		float red   = ((m_ForeColor & 0x00ff0000) >> 16) / 255;
		float green = ((m_ForeColor & 0x0000ff00) >> 8) /255;
		float blue  = (m_ForeColor & 0x000000ff) / 255;

		int localSize = this.GetComponent<UIWidget> ().width;
		float effPercent = 0;
		float curPercent = 0;
		if (m_Max > 0) {

			effPercent = (float)m_Eff / (float)m_Max;
			curPercent = (float)m_Cur / (float)m_Max;
		}

		int maxWidth = this.GetComponent<UIWidget>().width;
		int maxHeight = this.GetComponent<UIWidget> ().height;

		int effSize = Mathf.CeilToInt(effPercent * maxWidth);
		int curSize = Mathf.CeilToInt(curPercent * maxWidth);
		int effHeight = maxHeight;
		int curHeight = maxHeight;
		if (effSize == 0) {
			effHeight = 0;
		}
		if (curSize == 0) {
			curHeight = 0;
		}

		float effX = (float)(effSize - localSize) / 2f;
		float curX = (float)(curSize - localSize) / 2f;

		if (m_Eff == m_Max) {

			eff.GetComponent<UIWidget> ().height = 0;
			eff.GetComponent<UIWidget> ().width = 0;
			eff.localScale = new Vector3 (1f, 1f, 1f);
			eff.localPosition = Vector3.zero;
		} else {

			eff.GetComponent<UIWidget> ().height = effHeight;
			eff.GetComponent<UIWidget> ().width = effSize;
			eff.localScale = new Vector3 (1f, 1f, 1f);
			eff.localPosition = new Vector3(effX, 0f, 0f);
		}

		fore.GetComponent<UIWidget> ().height = curHeight;
		fore.GetComponent<UIWidget> ().width = curSize;
		fore.localScale = new Vector3 (1f, 1f, 1f);
		fore.localPosition = new Vector3(curX, 0f, 0f);


		Color colorFore = new Color (red, green ,blue);
		fore.GetComponent<UI2DSprite> ().color = colorFore;
	}



}
