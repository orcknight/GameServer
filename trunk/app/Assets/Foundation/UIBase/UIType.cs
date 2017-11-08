using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class UIType {

	public string Path { get; private set; }

	public string Name { get; private set; }

	public UIType(string path)
	{
		Path = path;
		Name = path.Substring(path.LastIndexOf('/') + 1);
	}

	public override string ToString()
	{
		return string.Format("path : {0} name : {1}", Path, Name);
	}

	public static readonly UIType CreateRole = new UIType("View/CreateRole");
	public static readonly UIType Login = new UIType("View/Login");
	public static readonly UIType MainWindow = new UIType("View/MainWindow");
	public static readonly UIType ServerList = new UIType("View/ServerList");
    public static readonly UIType Signup = new UIType("View/Signup");
    public static readonly UIType MessageBox = new UIType("View/MessageBox");
    public static readonly UIType Combat = new UIType("View/Combat");
}