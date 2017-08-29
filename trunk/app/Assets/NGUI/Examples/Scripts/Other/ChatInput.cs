using UnityEngine;

/// <summary>
/// Very simple example of how to use a TextList with a UIInput for chat.
/// </summary>

[RequireComponent(typeof(UIInput))]
[AddComponentMenu("NGUI/Examples/Chat Input")]
public class ChatInput : MonoBehaviour
{
	public UITextList textList;
	public bool fillWithDummyData = false;

	UIInput mInput;

	/// <summary>
	/// Add some dummy text to the text list.
	/// </summary>

	void Start ()
	{
		mInput = GetComponent<UIInput>();
		mInput.label.maxLineCount = 1;


		textList.Add("[00FF00]" +
			"欢迎登陆仗剑天涯！[-]");
		textList.Add("[00FF00]" +
			" 本服管理员QQ：407496032，有问题请联系管理员！[-]");

		if (fillWithDummyData && textList != null)
		{
			for (int i = 0; i < 30; ++i)
			{
				textList.Add("[00FF00]" +
					"This is an example paragraph for the text list, testing line " + i + "[-]");
			}
		}
	}

	/// <summary>
	/// Submit notification is sent by UIInput when 'enter' is pressed or iOS/Android keyboard finalizes input.
	/// </summary>

	public void OnSubmit ()
	{
		if (textList != null)
		{
			// It's a good idea to strip out all symbols as we don't want user input to alter colors, add new lines, etc
			string text = NGUIText.StripSymbols(mInput.value);

			if (!string.IsNullOrEmpty(text))
			{
				textList.Add(text);
				mInput.value = "";
				mInput.isSelected = false;
			}
		}
	}
}
