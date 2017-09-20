using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameCamera : MonoBehaviour {

	float devHeight = 19.2f;
	float devWidth = 10.8f;

	// Use this for initialization
	void Start () {
		
		/*float screenHeight = Screen.height;

		Debug.Log ("screenHeight = " + screenHeight);

		//this.GetComponent<Camera>().orthographicSize = screenHeight / 200.0f;

		float orthographicSize = this.GetComponent<Camera>().orthographicSize;

		float aspectRatio = Screen.width * 1.0f / Screen.height;

		float cameraWidth = orthographicSize * 2 * aspectRatio;

		Debug.Log ("cameraWidth = " + cameraWidth);

		if (cameraWidth < devWidth)
		{
			orthographicSize = devWidth / (2 * aspectRatio);
			Debug.Log ("new orthographicSize = " + orthographicSize);
			this.GetComponent<Camera>().orthographicSize = orthographicSize;
		}

		int ManualWidth = 1080;
		int ManualHeight = 1920;
		int manualHeight;
		if (System.Convert.ToSingle(Screen.height) / Screen.width > System.Convert.ToSingle(ManualHeight) / ManualWidth)
			manualHeight = Mathf.RoundToInt(System.Convert.ToSingle(ManualWidth) / Screen.width * Screen.height);
		else
			manualHeight = ManualHeight;
		Camera camera = GetComponent<Camera>();
		float scale =System.Convert.ToSingle(manualHeight / 1920f);
		camera.fieldOfView *= scale;*/
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
