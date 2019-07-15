/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.gpcommerceaddon.jalo;

import com.gp.commerce.core.jalo.GPBannerComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.gpcommerceaddon.jalo.MardiGrasHeroBannerComponent MardiGrasHeroBannerComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMardiGrasHeroBannerComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>MardiGrasHeroBannerComponent.firstBanner</code> attribute **/
	public static final String FIRSTBANNER = "firstBanner";
	/** Qualifier of the <code>MardiGrasHeroBannerComponent.secondBanner</code> attribute **/
	public static final String SECONDBANNER = "secondBanner";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(FIRSTBANNER, AttributeMode.INITIAL);
		tmp.put(SECONDBANNER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasHeroBannerComponent.firstBanner</code> attribute.
	 * @return the firstBanner
	 */
	public GPBannerComponent getFirstBanner(final SessionContext ctx)
	{
		return (GPBannerComponent)getProperty( ctx, FIRSTBANNER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasHeroBannerComponent.firstBanner</code> attribute.
	 * @return the firstBanner
	 */
	public GPBannerComponent getFirstBanner()
	{
		return getFirstBanner( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasHeroBannerComponent.firstBanner</code> attribute. 
	 * @param value the firstBanner
	 */
	public void setFirstBanner(final SessionContext ctx, final GPBannerComponent value)
	{
		setProperty(ctx, FIRSTBANNER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasHeroBannerComponent.firstBanner</code> attribute. 
	 * @param value the firstBanner
	 */
	public void setFirstBanner(final GPBannerComponent value)
	{
		setFirstBanner( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasHeroBannerComponent.secondBanner</code> attribute.
	 * @return the secondBanner
	 */
	public GPBannerComponent getSecondBanner(final SessionContext ctx)
	{
		return (GPBannerComponent)getProperty( ctx, SECONDBANNER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasHeroBannerComponent.secondBanner</code> attribute.
	 * @return the secondBanner
	 */
	public GPBannerComponent getSecondBanner()
	{
		return getSecondBanner( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasHeroBannerComponent.secondBanner</code> attribute. 
	 * @param value the secondBanner
	 */
	public void setSecondBanner(final SessionContext ctx, final GPBannerComponent value)
	{
		setProperty(ctx, SECONDBANNER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasHeroBannerComponent.secondBanner</code> attribute. 
	 * @param value the secondBanner
	 */
	public void setSecondBanner(final GPBannerComponent value)
	{
		setSecondBanner( getSession().getSessionContext(), value );
	}
	
}
