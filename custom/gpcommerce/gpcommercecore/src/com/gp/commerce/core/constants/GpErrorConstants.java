/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.constants;

/**
 * Constant class for GP error codes and error messages
 * @author megverma
 *
 */
public class GpErrorConstants {

	public static final String EXTENSIONNAME = "gpcommercecore";

	private GpErrorConstants()
	{
		//empty
	}

	// implement here constants used by this extension
	public static final String SORT_FAILED_MSG = "Sorting failed";
	public static final String SORT_FAILED_CODE = "3321";
	public static final String GET_WISHLIST_FAILED_MSG = "You Don't have any wishlist";
	public static final String GET_WISHLIST_FAILED_CODE = "3322";
	public static final String CREATE_WISHLIST_FAILED_CODE = "3324";
	public static final String CREATE_WISHLISTS_FAILED_MSG = "That List Name already exist, please try a different List Name.";
	public static final String REMOVE_WISHLISTS_FAILED_MSG = "These UID's could not be removed ";
	public static final String REMOVE_WISHLISTS_FAILED_CODE = "3323";
	public static final String ERROR_SUBMIT_TAX_EXEMPTION_DOCS = "3312";
	public static final String ERROR_195 = "195";


	public static final String GPBUNDLE_NO_BUNDLE_FOR_CODE = "9001";
	public static final String GPBUNDLE_NO_BUNDLE_FOR_MSG = "No Bundle for Product Code";

	public static final String GPBUNDLE_NO_ROOT_BUNDLE_CODE = "9002";
	public static final String GPBUNDLE_NO_ROOT_BUNDLE_MSG = "No root bundle found";

	public static final String GPBUNDLE_NO_CHILD_BUNDLE_CODE = "9003";
	public static final String GPBUNDLE_NO_CHILD_BUNDLE_MSG = "Root bundle has no children";

	public static final String GPBUNDLE_GRANDCHILD_BUNDLE_CODE = "9004";
	public static final String GPBUNDLE_GRANDCHILD_BUNDLE_MSG = "Only one level of bundles are allowed";

	public static final String GPBUNDLE_GRANDCHILD_NOPRODUCT_CODE = "9005";
	public static final String GPBUNDLE_GRANDCHILD_NOPRODUCT_MSG = "Child bundles must have products";

	public static final String GPBUNDLE_GRANDCHILD_NOSELECTCRITERIA_CODE = "9006";
	public static final String GPBUNDLE_GRANDCHILD_NOSELECTCRITERIA_MSG = "Child bundles must have a selection Criteria";

}
