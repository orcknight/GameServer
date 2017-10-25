using Newtonsoft.Json.Linq;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TaskBar : MonoBehaviour {

	private Transform m_TableTrans = null;
	private GameObject m_Selected = null;

    public delegate void taskButtonClickCallback(string cmd, string target);

    public taskButtonClickCallback clickCallBack = null;

    // Use this for initialization
    void Start () {

		m_TableTrans = this.transform.Find ("Table");
		m_TableTrans.localPosition = new Vector3(-403f, 494f,0f);



		GameObject[] objs = GameObject.FindGameObjectsWithTag ("TaskItemButton");

		foreach(GameObject obj in objs){

			UIEventListener.Get(obj).onClick = OnTaskButtonClick;  
		}

		//get task data
	}

	void InitTaskData(){

	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OnClose(){

		this.transform.localScale = new Vector3 (1f, 1f, 1f);
		this.transform.localPosition = new Vector3 (5000f, 0f, 0f);
		this.gameObject.SetActive(false);
	}

	public void OnTaskButtonClick(GameObject obj){
		if (obj.Equals (m_Selected)) {
			return;
		}

		if (m_Selected != null) {
			m_Selected.GetComponent<UISprite> ().spriteName = "Transparent-Background";
		}
		m_Selected = obj;
		obj.GetComponent<UISprite> ().spriteName = "Orange-Button";
		Debug.Log("我是按钮2被点击了");
	}

    public void UpdateTaskDetail(string msg){

        JArray jlist = JArray.Parse(msg);

    }
}
