using Newtonsoft.Json.Linq;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TaskBar : MonoBehaviour {

	private GameObject m_Selected = null;
	private Transform m_TaskListGrid = null;
	private Transform m_TaskDetail = null;

    public delegate void taskButtonClickCallback(string cmd, string target);

    public taskButtonClickCallback clickCallBack = null;

    // Use this for initialization
    void Start () {

		init ();

		//get task data
	}

	public void init(){

		Transform taskListContainer = this.transform.Find ("TaskListContainer");
		Transform taskListTrans = taskListContainer.Find ("TaskList");
		m_TaskListGrid = taskListTrans.Find ("Grid");
		Transform taskDetailContainer = this.transform.Find ("TaskDetailContainer");
		m_TaskDetail = taskDetailContainer.Find ("TaskDetail");
	}

	public void InitTaskData(List<Track> tracks){

		if (m_TaskListGrid == null) {
			init ();
		}
			
		ClearTaskList ();

		GameObject taskItemPerfab = Resources.Load ("MainWindow/TaskItemButton") as GameObject; 
		for (int i = 0; i < tracks.Count; i++) {

			GameObject item = GameObject.Instantiate (taskItemPerfab) as GameObject;
			item.transform.parent = m_TaskListGrid;
			item.transform.localScale = new Vector3 (1f, 1f, 1f);
			item.transform.localPosition = new Vector3 (1f, 1f, 1f);
			item.transform.Find ("Label").GetComponent<UILabel> ().text = (i+1) + ":" + tracks [i].Desc;
			item.transform.GetComponent<TaskItem> ().Track = tracks [i];
			item.SetActive (true);

			if (i == 0) {
				OnTaskButtonClick (item);
			}

			//绑定单击事件
			UIEventListener.Get(item).onClick = OnTaskButtonClick;  
		}

		m_TaskListGrid.GetComponent<UIGrid> ().repositionNow = true;
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

		//更新任务详情
		string msg = obj.GetComponent<TaskItem>().Track.Action.Desc;
		m_TaskDetail.transform.Find("Desc").GetComponent<UILabel>().text = msg;

	}

    public void UpdateTaskDetail(string msg){

        JArray jlist = JArray.Parse(msg);

    }

	private void ClearTaskList(){

		UIGrid uiGrid = m_TaskListGrid.GetComponent<UIGrid> ();
		if (uiGrid == null) {

			return;
		}

		for(int k = 0;k<uiGrid.transform.childCount;k++)
		{
			GameObject go = uiGrid.transform.GetChild(k).gameObject;
			Destroy(go);
		}
	}
}
