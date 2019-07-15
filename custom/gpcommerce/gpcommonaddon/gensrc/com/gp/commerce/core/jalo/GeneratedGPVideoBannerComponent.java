/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.acceleratorcms.jalo.components.SimpleResponsiveBannerComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPVideoBannerComponent GPVideoBannerComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPVideoBannerComponent extends SimpleResponsiveBannerComponent
{
	/** Qualifier of the <code>GPVideoBannerComponent.videoSrc</code> attribute **/
	public static final String VIDEOSRC = "videoSrc";
	/** Qualifier of the <code>GPVideoBannerComponent.playIconColor</code> attribute **/
	public static final String PLAYICONCOLOR = "playIconColor";
	/** Qualifier of the <code>GPVideoBannerComponent.headerText</code> attribute **/
	public static final String HEADERTEXT = "headerText";
	/** Qualifier of the <code>GPVideoBannerComponent.bodyText</code> attribute **/
	public static final String BODYTEXT = "bodyText";
	/** Qualifier of the <code>GPVideoBannerComponent.ctaText</code> attribute **/
	public static final String CTATEXT = "ctaText";
	/** Qualifier of the <code>GPVideoBannerComponent.ctaBackgroundColor</code> attribute **/
	public static final String CTABACKGROUNDCOLOR = "ctaBackgroundColor";
	/** Qualifier of the <code>GPVideoBannerComponent.headerTextColor</code> attribute **/
	public static final String HEADERTEXTCOLOR = "headerTextColor";
	/** Qualifier of the <code>GPVideoBannerComponent.bodyTextColor</code> attribute **/
	public static final String BODYTEXTCOLOR = "bodyTextColor";
	/** Qualifier of the <code>GPVideoBannerComponent.ctaTextColor</code> attribute **/
	public static final String CTATEXTCOLOR = "ctaTextColor";
	/** Qualifier of the <code>GPVideoBannerComponent.textPosition</code> attribute **/
	public static final String TEXTPOSITION = "textPosition";
	/** Qualifier of the <code>GPVideoBannerComponent.subHeadingText</code> attribute **/
	public static final String SUBHEADINGTEXT = "subHeadingText";
	/** Qualifier of the <code>GPVideoBannerComponent.subHeadingColor</code> attribute **/
	public static final String SUBHEADINGCOLOR = "subHeadingColor";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleResponsiveBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(VIDEOSRC, AttributeMode.INITIAL);
		tmp.put(PLAYICONCOLOR, AttributeMode.INITIAL);
		tmp.put(HEADERTEXT, AttributeMode.INITIAL);
		tmp.put(BODYTEXT, AttributeMode.INITIAL);
		tmp.put(CTATEXT, AttributeMode.INITIAL);
		tmp.put(CTABACKGROUNDCOLOR, AttributeMode.INITIAL);
		tmp.put(HEADERTEXTCOLOR, AttributeMode.INITIAL);
		tmp.put(BODYTEXTCOLOR, AttributeMode.INITIAL);
		tmp.put(CTATEXTCOLOR, AttributeMode.INITIAL);
		tmp.put(TEXTPOSITION, AttributeMode.INITIAL);
		tmp.put(SUBHEADINGTEXT, AttributeMode.INITIAL);
		tmp.put(SUBHEADINGCOLOR, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.bodyText</code> attribute.
	 * @return the bodyText
	 */
	public String getBodyText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BODYTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.bodyText</code> attribute.
	 * @return the bodyText
	 */
	public String getBodyText()
	{
		return getBodyText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.bodyText</code> attribute. 
	 * @param value the bodyText
	 */
	public void setBodyText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BODYTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.bodyText</code> attribute. 
	 * @param value the bodyText
	 */
	public void setBodyText(final String value)
	{
		setBodyText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.bodyTextColor</code> attribute.
	 * @return the bodyTextColor
	 */
	public String getBodyTextColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BODYTEXTCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.bodyTextColor</code> attribute.
	 * @return the bodyTextColor
	 */
	public String getBodyTextColor()
	{
		return getBodyTextColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.bodyTextColor</code> attribute. 
	 * @param value the bodyTextColor
	 */
	public void setBodyTextColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BODYTEXTCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.bodyTextColor</code> attribute. 
	 * @param value the bodyTextColor
	 */
	public void setBodyTextColor(final String value)
	{
		setBodyTextColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.ctaBackgroundColor</code> attribute.
	 * @return the ctaBackgroundColor
	 */
	public String getCtaBackgroundColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTABACKGROUNDCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.ctaBackgroundColor</code> attribute.
	 * @return the ctaBackgroundColor
	 */
	public String getCtaBackgroundColor()
	{
		return getCtaBackgroundColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.ctaBackgroundColor</code> attribute. 
	 * @param value the ctaBackgroundColor
	 */
	public void setCtaBackgroundColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTABACKGROUNDCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.ctaBackgroundColor</code> attribute. 
	 * @param value the ctaBackgroundColor
	 */
	public void setCtaBackgroundColor(final String value)
	{
		setCtaBackgroundColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.ctaText</code> attribute.
	 * @return the ctaText
	 */
	public String getCtaText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.ctaText</code> attribute.
	 * @return the ctaText
	 */
	public String getCtaText()
	{
		return getCtaText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.ctaText</code> attribute. 
	 * @param value the ctaText
	 */
	public void setCtaText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.ctaText</code> attribute. 
	 * @param value the ctaText
	 */
	public void setCtaText(final String value)
	{
		setCtaText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.ctaTextColor</code> attribute.
	 * @return the ctaTextColor
	 */
	public String getCtaTextColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXTCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.ctaTextColor</code> attribute.
	 * @return the ctaTextColor
	 */
	public String getCtaTextColor()
	{
		return getCtaTextColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.ctaTextColor</code> attribute. 
	 * @param value the ctaTextColor
	 */
	public void setCtaTextColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXTCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.ctaTextColor</code> attribute. 
	 * @param value the ctaTextColor
	 */
	public void setCtaTextColor(final String value)
	{
		setCtaTextColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.headerText</code> attribute.
	 * @return the headerText
	 */
	public String getHeaderText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADERTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.headerText</code> attribute.
	 * @return the headerText
	 */
	public String getHeaderText()
	{
		return getHeaderText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.headerText</code> attribute. 
	 * @param value the headerText
	 */
	public void setHeaderText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADERTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.headerText</code> attribute. 
	 * @param value the headerText
	 */
	public void setHeaderText(final String value)
	{
		setHeaderText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.headerTextColor</code> attribute.
	 * @return the headerTextColor
	 */
	public String getHeaderTextColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADERTEXTCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.headerTextColor</code> attribute.
	 * @return the headerTextColor
	 */
	public String getHeaderTextColor()
	{
		return getHeaderTextColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.headerTextColor</code> attribute. 
	 * @param value the headerTextColor
	 */
	public void setHeaderTextColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADERTEXTCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.headerTextColor</code> attribute. 
	 * @param value the headerTextColor
	 */
	public void setHeaderTextColor(final String value)
	{
		setHeaderTextColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.playIconColor</code> attribute.
	 * @return the playIconColor
	 */
	public String getPlayIconColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PLAYICONCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.playIconColor</code> attribute.
	 * @return the playIconColor
	 */
	public String getPlayIconColor()
	{
		return getPlayIconColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.playIconColor</code> attribute. 
	 * @param value the playIconColor
	 */
	public void setPlayIconColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PLAYICONCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.playIconColor</code> attribute. 
	 * @param value the playIconColor
	 */
	public void setPlayIconColor(final String value)
	{
		setPlayIconColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.subHeadingColor</code> attribute.
	 * @return the subHeadingColor
	 */
	public String getSubHeadingColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADINGCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.subHeadingColor</code> attribute.
	 * @return the subHeadingColor
	 */
	public String getSubHeadingColor()
	{
		return getSubHeadingColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.subHeadingColor</code> attribute. 
	 * @param value the subHeadingColor
	 */
	public void setSubHeadingColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADINGCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.subHeadingColor</code> attribute. 
	 * @param value the subHeadingColor
	 */
	public void setSubHeadingColor(final String value)
	{
		setSubHeadingColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.subHeadingText</code> attribute.
	 * @return the subHeadingText
	 */
	public String getSubHeadingText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADINGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.subHeadingText</code> attribute.
	 * @return the subHeadingText
	 */
	public String getSubHeadingText()
	{
		return getSubHeadingText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.subHeadingText</code> attribute. 
	 * @param value the subHeadingText
	 */
	public void setSubHeadingText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADINGTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.subHeadingText</code> attribute. 
	 * @param value the subHeadingText
	 */
	public void setSubHeadingText(final String value)
	{
		setSubHeadingText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.textPosition</code> attribute.
	 * @return the textPosition
	 */
	public String getTextPosition(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TEXTPOSITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.textPosition</code> attribute.
	 * @return the textPosition
	 */
	public String getTextPosition()
	{
		return getTextPosition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.textPosition</code> attribute. 
	 * @param value the textPosition
	 */
	public void setTextPosition(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TEXTPOSITION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.textPosition</code> attribute. 
	 * @param value the textPosition
	 */
	public void setTextPosition(final String value)
	{
		setTextPosition( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.videoSrc</code> attribute.
	 * @return the videoSrc
	 */
	public String getVideoSrc(final SessionContext ctx)
	{
		return (String)getProperty( ctx, VIDEOSRC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPVideoBannerComponent.videoSrc</code> attribute.
	 * @return the videoSrc
	 */
	public String getVideoSrc()
	{
		return getVideoSrc( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.videoSrc</code> attribute. 
	 * @param value the videoSrc
	 */
	public void setVideoSrc(final SessionContext ctx, final String value)
	{
		setProperty(ctx, VIDEOSRC,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPVideoBannerComponent.videoSrc</code> attribute. 
	 * @param value the videoSrc
	 */
	public void setVideoSrc(final String value)
	{
		setVideoSrc( getSession().getSessionContext(), value );
	}
	
}
