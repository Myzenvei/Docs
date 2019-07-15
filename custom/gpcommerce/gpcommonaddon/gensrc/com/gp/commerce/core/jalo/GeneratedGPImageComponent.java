/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPMediaComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.acceleratorcms.jalo.components.SimpleResponsiveBannerComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPImageComponent GPImageComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPImageComponent extends SimpleResponsiveBannerComponent
{
	/** Qualifier of the <code>GPImageComponent.headingText</code> attribute **/
	public static final String HEADINGTEXT = "headingText";
	/** Qualifier of the <code>GPImageComponent.subHeadBgColor</code> attribute **/
	public static final String SUBHEADBGCOLOR = "subHeadBgColor";
	/** Qualifier of the <code>GPImageComponent.ctaText</code> attribute **/
	public static final String CTATEXT = "ctaText";
	/** Qualifier of the <code>GPImageComponent.ctaColor</code> attribute **/
	public static final String CTACOLOR = "ctaColor";
	/** Qualifier of the <code>GPImageComponent.ctaBgColor</code> attribute **/
	public static final String CTABGCOLOR = "ctaBgColor";
	/** Qualifier of the <code>GPImageComponent.headingColor</code> attribute **/
	public static final String HEADINGCOLOR = "headingColor";
	/** Qualifier of the <code>GPImageComponent.subHeadingColor</code> attribute **/
	public static final String SUBHEADINGCOLOR = "subHeadingColor";
	/** Qualifier of the <code>GPImageComponent.backgroundColor</code> attribute **/
	public static final String BACKGROUNDCOLOR = "backgroundColor";
	/** Qualifier of the <code>GPImageComponent.textPosition</code> attribute **/
	public static final String TEXTPOSITION = "textPosition";
	/** Qualifier of the <code>GPImageComponent.subHeadingText</code> attribute **/
	public static final String SUBHEADINGTEXT = "subHeadingText";
	/** Qualifier of the <code>GPImageComponent.showTextFullWidth</code> attribute **/
	public static final String SHOWTEXTFULLWIDTH = "showTextFullWidth";
	/** Qualifier of the <code>GPImageComponent.imageLink</code> attribute **/
	public static final String IMAGELINK = "imageLink";
	/** Qualifier of the <code>GPImageComponent.isExternalImage</code> attribute **/
	public static final String ISEXTERNALIMAGE = "isExternalImage";
	/** Qualifier of the <code>GPImageComponent.heroLogo</code> attribute **/
	public static final String HEROLOGO = "heroLogo";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleResponsiveBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(HEADINGTEXT, AttributeMode.INITIAL);
		tmp.put(SUBHEADBGCOLOR, AttributeMode.INITIAL);
		tmp.put(CTATEXT, AttributeMode.INITIAL);
		tmp.put(CTACOLOR, AttributeMode.INITIAL);
		tmp.put(CTABGCOLOR, AttributeMode.INITIAL);
		tmp.put(HEADINGCOLOR, AttributeMode.INITIAL);
		tmp.put(SUBHEADINGCOLOR, AttributeMode.INITIAL);
		tmp.put(BACKGROUNDCOLOR, AttributeMode.INITIAL);
		tmp.put(TEXTPOSITION, AttributeMode.INITIAL);
		tmp.put(SUBHEADINGTEXT, AttributeMode.INITIAL);
		tmp.put(SHOWTEXTFULLWIDTH, AttributeMode.INITIAL);
		tmp.put(IMAGELINK, AttributeMode.INITIAL);
		tmp.put(ISEXTERNALIMAGE, AttributeMode.INITIAL);
		tmp.put(HEROLOGO, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor
	 */
	public String getBackgroundColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BACKGROUNDCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor
	 */
	public String getBackgroundColor()
	{
		return getBackgroundColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor
	 */
	public void setBackgroundColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BACKGROUNDCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor
	 */
	public void setBackgroundColor(final String value)
	{
		setBackgroundColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.ctaBgColor</code> attribute.
	 * @return the ctaBgColor
	 */
	public String getCtaBgColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTABGCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.ctaBgColor</code> attribute.
	 * @return the ctaBgColor
	 */
	public String getCtaBgColor()
	{
		return getCtaBgColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.ctaBgColor</code> attribute. 
	 * @param value the ctaBgColor
	 */
	public void setCtaBgColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTABGCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.ctaBgColor</code> attribute. 
	 * @param value the ctaBgColor
	 */
	public void setCtaBgColor(final String value)
	{
		setCtaBgColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.ctaColor</code> attribute.
	 * @return the ctaColor
	 */
	public String getCtaColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTACOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.ctaColor</code> attribute.
	 * @return the ctaColor
	 */
	public String getCtaColor()
	{
		return getCtaColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.ctaColor</code> attribute. 
	 * @param value the ctaColor
	 */
	public void setCtaColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTACOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.ctaColor</code> attribute. 
	 * @param value the ctaColor
	 */
	public void setCtaColor(final String value)
	{
		setCtaColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.ctaText</code> attribute.
	 * @return the ctaText
	 */
	public String getCtaText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.ctaText</code> attribute.
	 * @return the ctaText
	 */
	public String getCtaText()
	{
		return getCtaText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.ctaText</code> attribute. 
	 * @param value the ctaText
	 */
	public void setCtaText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.ctaText</code> attribute. 
	 * @param value the ctaText
	 */
	public void setCtaText(final String value)
	{
		setCtaText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.headingColor</code> attribute.
	 * @return the headingColor
	 */
	public String getHeadingColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADINGCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.headingColor</code> attribute.
	 * @return the headingColor
	 */
	public String getHeadingColor()
	{
		return getHeadingColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.headingColor</code> attribute. 
	 * @param value the headingColor
	 */
	public void setHeadingColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADINGCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.headingColor</code> attribute. 
	 * @param value the headingColor
	 */
	public void setHeadingColor(final String value)
	{
		setHeadingColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.headingText</code> attribute.
	 * @return the headingText
	 */
	public String getHeadingText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADINGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.headingText</code> attribute.
	 * @return the headingText
	 */
	public String getHeadingText()
	{
		return getHeadingText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.headingText</code> attribute. 
	 * @param value the headingText
	 */
	public void setHeadingText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADINGTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.headingText</code> attribute. 
	 * @param value the headingText
	 */
	public void setHeadingText(final String value)
	{
		setHeadingText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.heroLogo</code> attribute.
	 * @return the heroLogo - Adding attribute for header logo
	 */
	public GPMediaComponent getHeroLogo(final SessionContext ctx)
	{
		return (GPMediaComponent)getProperty( ctx, HEROLOGO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.heroLogo</code> attribute.
	 * @return the heroLogo - Adding attribute for header logo
	 */
	public GPMediaComponent getHeroLogo()
	{
		return getHeroLogo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.heroLogo</code> attribute. 
	 * @param value the heroLogo - Adding attribute for header logo
	 */
	public void setHeroLogo(final SessionContext ctx, final GPMediaComponent value)
	{
		setProperty(ctx, HEROLOGO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.heroLogo</code> attribute. 
	 * @param value the heroLogo - Adding attribute for header logo
	 */
	public void setHeroLogo(final GPMediaComponent value)
	{
		setHeroLogo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.imageLink</code> attribute.
	 * @return the imageLink - The image link attribute for this  component.
	 */
	public String getImageLink(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IMAGELINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.imageLink</code> attribute.
	 * @return the imageLink - The image link attribute for this  component.
	 */
	public String getImageLink()
	{
		return getImageLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.imageLink</code> attribute. 
	 * @param value the imageLink - The image link attribute for this  component.
	 */
	public void setImageLink(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IMAGELINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.imageLink</code> attribute. 
	 * @param value the imageLink - The image link attribute for this  component.
	 */
	public void setImageLink(final String value)
	{
		setImageLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.isExternalImage</code> attribute.
	 * @return the isExternalImage
	 */
	public Boolean isIsExternalImage(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISEXTERNALIMAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.isExternalImage</code> attribute.
	 * @return the isExternalImage
	 */
	public Boolean isIsExternalImage()
	{
		return isIsExternalImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.isExternalImage</code> attribute. 
	 * @return the isExternalImage
	 */
	public boolean isIsExternalImageAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsExternalImage( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.isExternalImage</code> attribute. 
	 * @return the isExternalImage
	 */
	public boolean isIsExternalImageAsPrimitive()
	{
		return isIsExternalImageAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.isExternalImage</code> attribute. 
	 * @param value the isExternalImage
	 */
	public void setIsExternalImage(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISEXTERNALIMAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.isExternalImage</code> attribute. 
	 * @param value the isExternalImage
	 */
	public void setIsExternalImage(final Boolean value)
	{
		setIsExternalImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.isExternalImage</code> attribute. 
	 * @param value the isExternalImage
	 */
	public void setIsExternalImage(final SessionContext ctx, final boolean value)
	{
		setIsExternalImage( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.isExternalImage</code> attribute. 
	 * @param value the isExternalImage
	 */
	public void setIsExternalImage(final boolean value)
	{
		setIsExternalImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.showTextFullWidth</code> attribute.
	 * @return the showTextFullWidth
	 */
	public Boolean isShowTextFullWidth(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWTEXTFULLWIDTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.showTextFullWidth</code> attribute.
	 * @return the showTextFullWidth
	 */
	public Boolean isShowTextFullWidth()
	{
		return isShowTextFullWidth( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.showTextFullWidth</code> attribute. 
	 * @return the showTextFullWidth
	 */
	public boolean isShowTextFullWidthAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowTextFullWidth( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.showTextFullWidth</code> attribute. 
	 * @return the showTextFullWidth
	 */
	public boolean isShowTextFullWidthAsPrimitive()
	{
		return isShowTextFullWidthAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.showTextFullWidth</code> attribute. 
	 * @param value the showTextFullWidth
	 */
	public void setShowTextFullWidth(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWTEXTFULLWIDTH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.showTextFullWidth</code> attribute. 
	 * @param value the showTextFullWidth
	 */
	public void setShowTextFullWidth(final Boolean value)
	{
		setShowTextFullWidth( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.showTextFullWidth</code> attribute. 
	 * @param value the showTextFullWidth
	 */
	public void setShowTextFullWidth(final SessionContext ctx, final boolean value)
	{
		setShowTextFullWidth( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.showTextFullWidth</code> attribute. 
	 * @param value the showTextFullWidth
	 */
	public void setShowTextFullWidth(final boolean value)
	{
		setShowTextFullWidth( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.subHeadBgColor</code> attribute.
	 * @return the subHeadBgColor
	 */
	public String getSubHeadBgColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADBGCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.subHeadBgColor</code> attribute.
	 * @return the subHeadBgColor
	 */
	public String getSubHeadBgColor()
	{
		return getSubHeadBgColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.subHeadBgColor</code> attribute. 
	 * @param value the subHeadBgColor
	 */
	public void setSubHeadBgColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADBGCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.subHeadBgColor</code> attribute. 
	 * @param value the subHeadBgColor
	 */
	public void setSubHeadBgColor(final String value)
	{
		setSubHeadBgColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.subHeadingColor</code> attribute.
	 * @return the subHeadingColor
	 */
	public String getSubHeadingColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADINGCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.subHeadingColor</code> attribute.
	 * @return the subHeadingColor
	 */
	public String getSubHeadingColor()
	{
		return getSubHeadingColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.subHeadingColor</code> attribute. 
	 * @param value the subHeadingColor
	 */
	public void setSubHeadingColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADINGCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.subHeadingColor</code> attribute. 
	 * @param value the subHeadingColor
	 */
	public void setSubHeadingColor(final String value)
	{
		setSubHeadingColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.subHeadingText</code> attribute.
	 * @return the subHeadingText
	 */
	public String getSubHeadingText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADINGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.subHeadingText</code> attribute.
	 * @return the subHeadingText
	 */
	public String getSubHeadingText()
	{
		return getSubHeadingText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.subHeadingText</code> attribute. 
	 * @param value the subHeadingText
	 */
	public void setSubHeadingText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADINGTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.subHeadingText</code> attribute. 
	 * @param value the subHeadingText
	 */
	public void setSubHeadingText(final String value)
	{
		setSubHeadingText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.textPosition</code> attribute.
	 * @return the textPosition
	 */
	public String getTextPosition(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TEXTPOSITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageComponent.textPosition</code> attribute.
	 * @return the textPosition
	 */
	public String getTextPosition()
	{
		return getTextPosition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.textPosition</code> attribute. 
	 * @param value the textPosition
	 */
	public void setTextPosition(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TEXTPOSITION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageComponent.textPosition</code> attribute. 
	 * @param value the textPosition
	 */
	public void setTextPosition(final String value)
	{
		setTextPosition( getSession().getSessionContext(), value );
	}
	
}
