/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Apr 18, 2019 3:07:06 PM                     ---
 * ----------------------------------------------------------------
 */
package com.deloitte.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated
@SuppressWarnings({"unused","cast","PMD"})
public class GeneratedSubscriptionaddonConstants
{
	public static final String EXTENSIONNAME = "subscriptionaddon";
	public static class TC
	{
		public static final String SUBSCRIPTIONCART = "SubscriptionCart".intern();
		public static final String SUBSCRIPTIONFREQUENCYENUM = "SubscriptionFrequencyEnum".intern();
		public static final String SUBSCRIPTIONSCHEDULE = "SubscriptionSchedule".intern();
		public static final String SUBSCRIPTIONSTATUSENUM = "SubscriptionStatusEnum".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrderEntry
		{
			public static final String FREQUENCY = "frequency".intern();
		}
		public static class PriceRow
		{
			public static final String FREQUENCY = "frequency".intern();
		}
		public static class Product
		{
			public static final String SUBSCRIBABLE = "subscribable".intern();
		}
	}
	public static class Enumerations
	{
		public static class SubscriptionFrequencyEnum
		{
			public static final String DAILY = "DAILY".intern();
			public static final String WEEKLY = "WEEKLY".intern();
			public static final String MONTHLY = "MONTHLY".intern();
			public static final String YEARLY = "YEARLY".intern();
		}
		public static class SubscriptionStatusEnum
		{
			public static final String PROCESSED = "PROCESSED".intern();
			public static final String PENDING = "PENDING".intern();
			public static final String CANCELLED = "CANCELLED".intern();
		}
	}
	public static class Relations
	{
		public static final String SUBSCRIPTIONCART2SUBSCRIPTIONSCHEDULEREL = "SubscriptionCart2SubscriptionScheduleRel".intern();
	}
	
	protected GeneratedSubscriptionaddonConstants()
	{
		// private constructor
	}
	
	
}
