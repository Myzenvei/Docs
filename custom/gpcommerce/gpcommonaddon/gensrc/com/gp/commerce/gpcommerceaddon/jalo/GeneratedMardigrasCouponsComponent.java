/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.gpcommerceaddon.jalo;

import com.gp.commerce.core.jalo.GPMediaComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import com.gp.commerce.gpcommerceaddon.jalo.MardiGrasImageAndTextComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.MediaContainer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.gpcommerceaddon.jalo.MardigrasCouponsComponent MardigrasCouponsComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMardigrasCouponsComponent extends MardiGrasImageAndTextComponent
{
	/** Qualifier of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute **/
	public static final String BACKGROUNDIMAGE = "backGroundImage";
	/** Qualifier of the <code>MardigrasCouponsComponent.isSignUp</code> attribute **/
	public static final String ISSIGNUP = "isSignUp";
	/** Qualifier of the <code>MardigrasCouponsComponent.downloadFile</code> attribute **/
	public static final String DOWNLOADFILE = "downloadFile";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(MardiGrasImageAndTextComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(BACKGROUNDIMAGE, AttributeMode.INITIAL);
		tmp.put(ISSIGNUP, AttributeMode.INITIAL);
		tmp.put(DOWNLOADFILE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute.
	 * @return the backGroundImage - It is a media container containing images for specific resolutions
	 */
	public MediaContainer getBackGroundImage(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedMardigrasCouponsComponent.getBackGroundImage requires a session language", 0 );
		}
		return (MediaContainer)getLocalizedProperty( ctx, BACKGROUNDIMAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute.
	 * @return the backGroundImage - It is a media container containing images for specific resolutions
	 */
	public MediaContainer getBackGroundImage()
	{
		return getBackGroundImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute. 
	 * @return the localized backGroundImage - It is a media container containing images for specific resolutions
	 */
	public Map<Language,MediaContainer> getAllBackGroundImage(final SessionContext ctx)
	{
		return (Map<Language,MediaContainer>)getAllLocalizedProperties(ctx,BACKGROUNDIMAGE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute. 
	 * @return the localized backGroundImage - It is a media container containing images for specific resolutions
	 */
	public Map<Language,MediaContainer> getAllBackGroundImage()
	{
		return getAllBackGroundImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute. 
	 * @param value the backGroundImage - It is a media container containing images for specific resolutions
	 */
	public void setBackGroundImage(final SessionContext ctx, final MediaContainer value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedMardigrasCouponsComponent.setBackGroundImage requires a session language", 0 );
		}
		setLocalizedProperty(ctx, BACKGROUNDIMAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute. 
	 * @param value the backGroundImage - It is a media container containing images for specific resolutions
	 */
	public void setBackGroundImage(final MediaContainer value)
	{
		setBackGroundImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute. 
	 * @param value the backGroundImage - It is a media container containing images for specific resolutions
	 */
	public void setAllBackGroundImage(final SessionContext ctx, final Map<Language,MediaContainer> value)
	{
		setAllLocalizedProperties(ctx,BACKGROUNDIMAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.backGroundImage</code> attribute. 
	 * @param value the backGroundImage - It is a media container containing images for specific resolutions
	 */
	public void setAllBackGroundImage(final Map<Language,MediaContainer> value)
	{
		setAllBackGroundImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.downloadFile</code> attribute.
	 * @return the downloadFile
	 */
	public GPMediaComponent getDownloadFile(final SessionContext ctx)
	{
		return (GPMediaComponent)getProperty( ctx, DOWNLOADFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.downloadFile</code> attribute.
	 * @return the downloadFile
	 */
	public GPMediaComponent getDownloadFile()
	{
		return getDownloadFile( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.downloadFile</code> attribute. 
	 * @param value the downloadFile
	 */
	public void setDownloadFile(final SessionContext ctx, final GPMediaComponent value)
	{
		setProperty(ctx, DOWNLOADFILE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.downloadFile</code> attribute. 
	 * @param value the downloadFile
	 */
	public void setDownloadFile(final GPMediaComponent value)
	{
		setDownloadFile( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.isSignUp</code> attribute.
	 * @return the isSignUp - The boolean for determining whether it is a signup or teachers component.
	 */
	public Boolean isIsSignUp(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISSIGNUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.isSignUp</code> attribute.
	 * @return the isSignUp - The boolean for determining whether it is a signup or teachers component.
	 */
	public Boolean isIsSignUp()
	{
		return isIsSignUp( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.isSignUp</code> attribute. 
	 * @return the isSignUp - The boolean for determining whether it is a signup or teachers component.
	 */
	public boolean isIsSignUpAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsSignUp( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasCouponsComponent.isSignUp</code> attribute. 
	 * @return the isSignUp - The boolean for determining whether it is a signup or teachers component.
	 */
	public boolean isIsSignUpAsPrimitive()
	{
		return isIsSignUpAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.isSignUp</code> attribute. 
	 * @param value the isSignUp - The boolean for determining whether it is a signup or teachers component.
	 */
	public void setIsSignUp(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISSIGNUP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.isSignUp</code> attribute. 
	 * @param value the isSignUp - The boolean for determining whether it is a signup or teachers component.
	 */
	public void setIsSignUp(final Boolean value)
	{
		setIsSignUp( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.isSignUp</code> attribute. 
	 * @param value the isSignUp - The boolean for determining whether it is a signup or teachers component.
	 */
	public void setIsSignUp(final SessionContext ctx, final boolean value)
	{
		setIsSignUp( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasCouponsComponent.isSignUp</code> attribute. 
	 * @param value the isSignUp - The boolean for determining whether it is a signup or teachers component.
	 */
	public void setIsSignUp(final boolean value)
	{
		setIsSignUp( getSession().getSessionContext(), value );
	}
	
}
