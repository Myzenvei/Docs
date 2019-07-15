/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gpsapreturnorder.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated
@SuppressWarnings({"unused","cast","PMD"})
public class GeneratedGpsapreturnorderConstants
{
	public static final String EXTENSIONNAME = "gpsapreturnorder";
	public static class TC
	{
		public static final String GPREFUNDENTRY = "GPRefundEntry".intern();
	}
	public static class Attributes
	{
		public static class Consignment
		{
			public static final String RETURNREQUESTS = "returnRequests".intern();
		}
		public static class ReturnRequest
		{
			public static final String CONSIGNMENT = "consignment".intern();
			public static final String CONSIGNMENTPOS = "consignmentPOS".intern();
			public static final String RETRYCOUNT = "retryCount".intern();
		}
	}
	public static class Relations
	{
		public static final String CONSIGNMENT2RETURNREQUEST = "Consignment2ReturnRequest".intern();
	}
	
	protected GeneratedGpsapreturnorderConstants()
	{
		// private constructor
	}
	
	
}
