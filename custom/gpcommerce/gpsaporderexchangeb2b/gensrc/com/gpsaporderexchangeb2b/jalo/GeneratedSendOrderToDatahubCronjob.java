/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gpsaporderexchangeb2b.jalo;

import com.gpsaporderexchangeb2b.constants.Gpsaporderexchangeb2bConstants;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob SendOrderToDatahubCronjob}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSendOrderToDatahubCronjob extends CronJob
{
	/** Qualifier of the <code>SendOrderToDatahubCronjob.baseSite</code> attribute **/
	public static final String BASESITE = "baseSite";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(BASESITE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SendOrderToDatahubCronjob.baseSite</code> attribute.
	 * @return the baseSite - CMS Site
	 */
	public CMSSite getBaseSite(final SessionContext ctx)
	{
		return (CMSSite)getProperty( ctx, BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SendOrderToDatahubCronjob.baseSite</code> attribute.
	 * @return the baseSite - CMS Site
	 */
	public CMSSite getBaseSite()
	{
		return getBaseSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SendOrderToDatahubCronjob.baseSite</code> attribute. 
	 * @param value the baseSite - CMS Site
	 */
	public void setBaseSite(final SessionContext ctx, final CMSSite value)
	{
		setProperty(ctx, BASESITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SendOrderToDatahubCronjob.baseSite</code> attribute. 
	 * @param value the baseSite - CMS Site
	 */
	public void setBaseSite(final CMSSite value)
	{
		setBaseSite( getSession().getSessionContext(), value );
	}
	
}
