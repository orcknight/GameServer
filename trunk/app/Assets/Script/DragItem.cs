using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class DragItem : UIDragDropItem {

	protected override void OnDragDropRelease (GameObject surface)
	{
		base.OnDragDropRelease (surface);

		if (surface == null || surface.tag.Equals ("UIRoot")) {
			
			transform.localPosition = Vector3.zero;
		} else if (surface.tag.Equals ("Cell")) {

			Transform meParent = this.transform.parent;
			Transform targetCell = surface.transform.Find ("Label");

			this.transform.parent = surface.transform;
			this.transform.localPosition = Vector3.zero;

			if (targetCell != null) {

				targetCell.parent = meParent;
				targetCell.localPosition = Vector3.zero;

			}

		} else if (surface.name.Equals ("Label")) {

			Transform targetParent = surface.transform.parent;

			surface.transform.parent = this.transform.parent;
			this.transform.parent = targetParent;

			surface.transform.localPosition = Vector3.zero;
			this.transform.localPosition = Vector3.zero;
		}
			


	}

}
