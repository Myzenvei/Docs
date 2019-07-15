/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2lib.components.ProductCarouselComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPProductCarouselComponent GPProductCarouselComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPProductCarouselComponent extends ProductCarouselComponent
{
	/** Qualifier of the <code>GPProductCarouselComponent.ctaLink</code> attribute **/
	public static final String CTALINK = "ctaLink";
	/** Qualifier of the <code>GPProductCarouselComponent.ctaText</code> attribute **/
	public static final String CTATEXT = "ctaText";
	/** Qualifier of the <code>GPProductCarouselComponent.noOfColumns</code> attribute **/
	public static final String NOOFCOLUMNS = "noOfColumns";
	/** Qualifier of the <code>GPProductCarouselComponent.isCarousel</code> attribute **/
	public static final String ISCAROUSEL = "isCarousel";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ProductCarouselComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CTALINK, AttributeMode.INITIAL);
		tmp.put(CTATEXT, AttributeMode.INITIAL);
		tmp.put(NOOFCOLUMNS, AttributeMode.INITIAL);
		tmp.put(ISCAROUSEL, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.ctaLink</code> attribute.
	 * @return the ctaLink
	 */
	public String getCtaLink(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTALINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.ctaLink</code> attribute.
	 * @return the ctaLink
	 */
	public String getCtaLink()
	{
		return getCtaLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.ctaLink</code> attribute. 
	 * @param value the ctaLink
	 */
	public void setCtaLink(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTALINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.ctaLink</code> attribute. 
	 * @param value the ctaLink
	 */
	public void setCtaLink(final String value)
	{
		setCtaLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.ctaText</code> attribute.
	 * @return the ctaText
	 */
	public String getCtaText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.ctaText</code> attribute.
	 * @return the ctaText
	 */
	public String getCtaText()
	{
		return getCtaText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.ctaText</code> attribute. 
	 * @param value the ctaText
	 */
	public void setCtaText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.ctaText</code> attribute. 
	 * @param value the ctaText
	 */
	public void setCtaText(final String value)
	{
		setCtaText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.isCarousel</code> attribute.
	 * @return the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public Boolean isIsCarousel(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISCAROUSEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.isCarousel</code> attribute.
	 * @return the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public Boolean isIsCarousel()
	{
		return isIsCarousel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.isCarousel</code> attribute. 
	 * @return the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public boolean isIsCarouselAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsCarousel( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.isCarousel</code> attribute. 
	 * @return the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public boolean isIsCarouselAsPrimitive()
	{
		return isIsCarouselAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.isCarousel</code> attribute. 
	 * @param value the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public void setIsCarousel(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISCAROUSEL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.isCarousel</code> attribute. 
	 * @param value the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public void setIsCarousel(final Boolean value)
	{
		setIsCarousel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.isCarousel</code> attribute. 
	 * @param value the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public void setIsCarousel(final SessionContext ctx, final boolean value)
	{
		setIsCarousel( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.isCarousel</code> attribute. 
	 * @param value the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public void setIsCarousel(final boolean value)
	{
		setIsCarousel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.noOfColumns</code> attribute.
	 * @return the noOfColumns - The number of tiles to be in the component.
	 */
	public String getNoOfColumns(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NOOFCOLUMNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductCarouselComponent.noOfColumns</code> attribute.
	 * @return the noOfColumns - The number of tiles to be in the component.
	 */
	public String getNoOfColumns()
	{
		return getNoOfColumns( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.noOfColumns</code> attribute. 
	 * @param value the noOfColumns - The number of tiles to be in the component.
	 */
	public void setNoOfColumns(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NOOFCOLUMNS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductCarouselComponent.noOfColumns</code> attribute. 
	 * @param value the noOfColumns - The number of tiles to be in the component.
	 */
	public void setNoOfColumns(final String value)
	{
		setNoOfColumns( getSession().getSessionContext(), value );
	}
	
}
