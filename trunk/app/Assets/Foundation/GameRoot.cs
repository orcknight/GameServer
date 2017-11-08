using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameRoot : MonoBehaviour {

    public void Start() {
        Singleton<UIManager>.Create();
        Singleton<ContextManager>.Create();
    }

}
