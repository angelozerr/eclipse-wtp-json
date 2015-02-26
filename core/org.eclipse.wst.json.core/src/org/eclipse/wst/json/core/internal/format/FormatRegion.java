/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.json.core.internal.format;



import org.eclipse.jface.text.IRegion;

/**
 * 
 */
class FormatRegion implements IRegion {

	private int fOffset, fLength;

	/**
	 * 
	 */
	FormatRegion(int offset, int length) {
		super();
		set(offset, length);
	}

	/**
	 * Returns the length of the region.
	 * 
	 * @return the length of the region
	 */
	public int getLength() {
		return fLength;
	}

	/**
	 * Returns the offset of the region.
	 * 
	 * @return the offset of the region
	 */
	public int getOffset() {
		return fOffset;
	}

	/**
	 * 
	 */
	void set(int offset, int length) {
		this.fOffset = offset;
		this.fLength = length;
	}

	/**
	 * 
	 */
	void setLength(int newLength) {
		fLength = newLength;
	}

	/**
	 * 
	 */
	void setOffset(int newOffset) {
		fOffset = newOffset;
	}
}