using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UIManager {
    public Dictionary<UIType, GameObject> _UIDict = new Dictionary<UIType, GameObject>();

    private Transform _canvas;

    private UIManager() {
        _canvas = GameObject.Find("UI Root").transform;
        foreach (Transform item in _canvas) {

            if(item.name.Equals("Camera") || item.name.Equals("Background") || item.name.Equals("Logo")
                || item.name.Equals("SocketIO") || item.name.Equals("FingerGestures") || item.name.Equals("RightCenterAnchor")) {
                continue;
            }
            GameObject.Destroy(item.gameObject);
        }
    }

    public GameObject GetSingleUI(UIType uiType) {
        if (_UIDict.ContainsKey(uiType) == false || _UIDict[uiType] == null) {
            GameObject go = GameObject.Instantiate(Resources.Load<GameObject>(uiType.Path)) as GameObject;
            go.transform.SetParent(_canvas, false);
            go.name = uiType.Name;
            _UIDict[uiType] = go;
            return go;
        }
        return _UIDict[uiType];
    }

    public void DestroySingleUI(UIType uiType) {
        if (!_UIDict.ContainsKey(uiType)) {
            return;
        }

        if (_UIDict[uiType] == null) {
            _UIDict.Remove(uiType);
            return;
        }

        GameObject.Destroy(_UIDict[uiType]);
        _UIDict.Remove(uiType);
    }
}