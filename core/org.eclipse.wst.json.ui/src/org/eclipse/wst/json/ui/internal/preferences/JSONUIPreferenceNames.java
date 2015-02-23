/**
 *  Copyright (c) 2015-present Angelo ZERR.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.ui.internal.preferences;

public class JSONUIPreferenceNames {

	/**
	 * <p>
	 * preference key used for saving which categories should not display on the
	 * default page
	 * </p>
	 * 
	 * <p>
	 * Value is of type {@link String} consisting of
	 * <tt>org.eclipse.wst.sse.ui.completionProposal/proposalCategory/@id</tt>s
	 * separated by the null character (<tt>\0</tt>), ordered is ignored
	 * </p>
	 */
	public static final String CONTENT_ASSIST_DO_NOT_DISPLAY_ON_DEFAULT_PAGE = "json_content_assist_display_on_default_page"; //$NON-NLS-1$

	/**
	 * <p>
	 * preference key used for saving which categories should not display on
	 * their own page
	 * </p>
	 * 
	 * <p>
	 * Value is of type {@link String} consisting of
	 * <tt>org.eclipse.wst.sse.ui.completionProposal/proposalCategory/@id</tt>s
	 * separated by the null character (<tt>\0</tt>), order is ignored
	 * </p>
	 */
	public static final String CONTENT_ASSIST_DO_NOT_DISPLAY_ON_OWN_PAGE = "json_content_assist_display_on_own_page"; //$NON-NLS-1$

	/**
	 * <p>
	 * preference key for saving the sort order of the categories when
	 * displaying them on their own page
	 * </p>
	 * 
	 * <p>
	 * Value is of type {@link String} consisting of
	 * <tt>org.eclipse.wst.sse.ui.completionProposal/proposalCategory/@id</tt>s
	 * separated by the null character (<tt>\0</tt>) in the desired sort order.
	 * </p>
	 */
	public static final String CONTENT_ASSIST_OWN_PAGE_SORT_ORDER = "json_content_assist_own_page_sort_order"; //$NON-NLS-1$

	/**
	 * <p>
	 * preference key for saving the sort order of the categories when
	 * displaying them on the default page
	 * </p>
	 * 
	 * <p>
	 * Value is of type {@link String} consisting of
	 * <tt>org.eclipse.wst.sse.ui.completionProposal/proposalCategory/@id</tt>s
	 * separated by the null character (<tt>\0</tt>) in the desired sort order.
	 * </p>
	 */
	public static final String CONTENT_ASSIST_DEFAULT_PAGE_SORT_ORDER = "json_content_assist_default_page_sort_order"; //$NON-NLS-1$
}
