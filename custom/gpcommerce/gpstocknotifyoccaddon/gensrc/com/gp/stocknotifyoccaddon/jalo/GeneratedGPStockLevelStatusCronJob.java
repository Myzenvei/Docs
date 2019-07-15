/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.stocknotifyoccaddon.jalo;

import com.gp.stocknotifyoccaddon.constants.GpstocknotifyoccaddonConstants;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob GPStockLevelStatusCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPStockLevelStatusCronJob extends CronJob
{
	/** Qualifier of the <code>GPStockLevelStatusCronJob.site</code> attribute **/
	public static final String SITE = "site";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SITE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPStockLevelStatusCronJob.site</code> attribute.
	 * @return the site - Set specific site for this job
	 */
	public CMSSite getSite(final SessionContext ctx)
	{
		return (CMSSite)getProperty( ctx, SITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPStockLevelStatusCronJob.site</code> attribute.
	 * @return the site - Set specific site for this job
	 */
	public CMSSite getSite()
	{
		return getSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPStockLevelStatusCronJob.site</code> attribute. 
	 * @param value the site - Set specific site for this job
	 */
	public void setSite(final SessionContext ctx, final CMSSite value)
	{
		setProperty(ctx, SITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPStockLevelStatusCronJob.site</code> attribute. 
	 * @param value the site - Set specific site for this job
	 */
	public void setSite(final CMSSite value)
	{
		setSite( getSession().getSessionContext(), value );
	}
	
}
