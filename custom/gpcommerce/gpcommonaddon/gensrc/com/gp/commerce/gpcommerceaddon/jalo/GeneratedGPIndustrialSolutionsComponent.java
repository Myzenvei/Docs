/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.gpcommerceaddon.jalo;

import com.gp.commerce.core.jalo.GPImageTileComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPImageTileComponent GPIndustrialSolutionsComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPIndustrialSolutionsComponent extends GPImageTileComponent
{
	/** Qualifier of the <code>GPIndustrialSolutionsComponent.ctaText2</code> attribute **/
	public static final String CTATEXT2 = "ctaText2";
	/** Qualifier of the <code>GPIndustrialSolutionsComponent.ctaTextColor2</code> attribute **/
	public static final String CTATEXTCOLOR2 = "ctaTextColor2";
	/** Qualifier of the <code>GPIndustrialSolutionsComponent.ctaTextLink2</code> attribute **/
	public static final String CTATEXTLINK2 = "ctaTextLink2";
	/** Qualifier of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute **/
	public static final String ISEXTERNALCTA2 = "isExternalCta2";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(GPImageTileComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CTATEXT2, AttributeMode.INITIAL);
		tmp.put(CTATEXTCOLOR2, AttributeMode.INITIAL);
		tmp.put(CTATEXTLINK2, AttributeMode.INITIAL);
		tmp.put(ISEXTERNALCTA2, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.ctaText2</code> attribute.
	 * @return the ctaText2 - The cta text for the component.
	 */
	public String getCtaText2(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXT2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.ctaText2</code> attribute.
	 * @return the ctaText2 - The cta text for the component.
	 */
	public String getCtaText2()
	{
		return getCtaText2( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.ctaText2</code> attribute. 
	 * @param value the ctaText2 - The cta text for the component.
	 */
	public void setCtaText2(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXT2,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.ctaText2</code> attribute. 
	 * @param value the ctaText2 - The cta text for the component.
	 */
	public void setCtaText2(final String value)
	{
		setCtaText2( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.ctaTextColor2</code> attribute.
	 * @return the ctaTextColor2 - The cta text color for the component.
	 */
	public String getCtaTextColor2(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXTCOLOR2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.ctaTextColor2</code> attribute.
	 * @return the ctaTextColor2 - The cta text color for the component.
	 */
	public String getCtaTextColor2()
	{
		return getCtaTextColor2( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.ctaTextColor2</code> attribute. 
	 * @param value the ctaTextColor2 - The cta text color for the component.
	 */
	public void setCtaTextColor2(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXTCOLOR2,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.ctaTextColor2</code> attribute. 
	 * @param value the ctaTextColor2 - The cta text color for the component.
	 */
	public void setCtaTextColor2(final String value)
	{
		setCtaTextColor2( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.ctaTextLink2</code> attribute.
	 * @return the ctaTextLink2 - The cta text for the component.
	 */
	public String getCtaTextLink2(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXTLINK2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.ctaTextLink2</code> attribute.
	 * @return the ctaTextLink2 - The cta text for the component.
	 */
	public String getCtaTextLink2()
	{
		return getCtaTextLink2( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.ctaTextLink2</code> attribute. 
	 * @param value the ctaTextLink2 - The cta text for the component.
	 */
	public void setCtaTextLink2(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXTLINK2,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.ctaTextLink2</code> attribute. 
	 * @param value the ctaTextLink2 - The cta text for the component.
	 */
	public void setCtaTextLink2(final String value)
	{
		setCtaTextLink2( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute.
	 * @return the isExternalCta2
	 */
	public Boolean isIsExternalCta2(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISEXTERNALCTA2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute.
	 * @return the isExternalCta2
	 */
	public Boolean isIsExternalCta2()
	{
		return isIsExternalCta2( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute. 
	 * @return the isExternalCta2
	 */
	public boolean isIsExternalCta2AsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsExternalCta2( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute. 
	 * @return the isExternalCta2
	 */
	public boolean isIsExternalCta2AsPrimitive()
	{
		return isIsExternalCta2AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute. 
	 * @param value the isExternalCta2
	 */
	public void setIsExternalCta2(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISEXTERNALCTA2,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute. 
	 * @param value the isExternalCta2
	 */
	public void setIsExternalCta2(final Boolean value)
	{
		setIsExternalCta2( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute. 
	 * @param value the isExternalCta2
	 */
	public void setIsExternalCta2(final SessionContext ctx, final boolean value)
	{
		setIsExternalCta2( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPIndustrialSolutionsComponent.isExternalCta2</code> attribute. 
	 * @param value the isExternalCta2
	 */
	public void setIsExternalCta2(final boolean value)
	{
		setIsExternalCta2( getSession().getSessionContext(), value );
	}
	
}
