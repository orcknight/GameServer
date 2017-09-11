using SocketIO;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NpcTaskWindow : MonoBehaviour {

	public List<Story> stories = new List<Story>();

    public int CurrentId { get; set; } //当前story的id
    public int RewardId { get; set; } //reward id 奖励id
    public int TrackId { get; set; } //任务id
    public int TrackActionId { get; set; } //任务条目id

    private Transform nameTrans = null;
    private Transform descTrans = null;

	// Use this for initialization
	void Start () {

        nameTrans = this.transform.Find("Background").Find("Name");
        descTrans = this.transform.Find("Background").Find("Desc");
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void DispalyContent() {

        if(string.IsNullOrEmpty(stories[CurrentId - 1].Type)) {
            return;
        }

        if (stories[CurrentId - 1].Type.Equals("player")) {

            nameTrans.GetComponent<UILabel>().text = PlayerPrefs.GetString("playerName");
        }else {

            nameTrans.GetComponent<UILabel>().text = stories[CurrentId - 1].Name;
        }

        
        descTrans.GetComponent<UILabel>().text = stories[CurrentId - 1].Said;
    }


    public void OnClick() {

        CurrentId++;

        if(CurrentId <= stories.Count) {

            if (stories[CurrentId - 1].Type.Equals("player")) {

                nameTrans.GetComponent<UILabel>().text = PlayerPrefs.GetString("playerName");
            } else {

                nameTrans.GetComponent<UILabel>().text = stories[CurrentId - 1].Name;
            }
            descTrans.GetComponent<UILabel>().text = stories[CurrentId - 1].Said;
        } else {

            this.transform.localPosition = new Vector3(5000f, 65f, 0f);
            this.gameObject.SetActive(false);

            if(RewardId > 0) {

                GameObject socketIo = GameObject.Find("SocketIO");

                JSONObject sendData = new JSONObject();
                sendData.AddField("trackId", TrackId);
                sendData.AddField("trackActionId", TrackActionId);
                sendData.AddField("rewardId", RewardId);

                JSONObject jsonObject = new JSONObject();
                jsonObject.AddField("cmd", "reward");
                jsonObject.AddField("data", sendData);

                socketIo.GetComponent<SocketIOComponent>().Emit("unity_stream", jsonObject);
            }
        }

    }
}
