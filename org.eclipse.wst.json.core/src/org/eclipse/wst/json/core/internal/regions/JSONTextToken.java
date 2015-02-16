package org.eclipse.wst.json.core.internal.regions;

public class JSONTextToken {

	public String kind;
	public String image;
	public int start;
	public int length;

	public String toString() {
		return (image != null) ? image : ""; //$NON-NLS-1$
	}
}
