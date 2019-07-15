/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
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
 * Generated class for type {@link com.gp.commerce.core.jalo.GPMediaContainerlink GPMediaContainerlink}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPMediaContainerlink extends SimpleCMSComponent
{
	/** Qualifier of the <code>GPMediaContainerlink.media</code> attribute **/
	public static final String MEDIA = "media";
	/** Qualifier of the <code>GPMediaContainerlink.mediaUrl</code> attribute **/
	public static final String MEDIAURL = "mediaUrl";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(MEDIA, AttributeMode.INITIAL);
		tmp.put(MEDIAURL, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMediaContainerlink.media</code> attribute.
	 * @return the media - A list of bundle promotion images
	 */
	public MediaContainer getMedia(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPMediaContainerlink.getMedia requires a session language", 0 );
		}
		return (MediaContainer)getLocalizedProperty( ctx, MEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMediaContainerlink.media</code> attribute.
	 * @return the media - A list of bundle promotion images
	 */
	public MediaContainer getMedia()
	{
		return getMedia( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMediaContainerlink.media</code> attribute. 
	 * @return the localized media - A list of bundle promotion images
	 */
	public Map<Language,MediaContainer> getAllMedia(final SessionContext ctx)
	{
		return (Map<Language,MediaContainer>)getAllLocalizedProperties(ctx,MEDIA,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMediaContainerlink.media</code> attribute. 
	 * @return the localized media - A list of bundle promotion images
	 */
	public Map<Language,MediaContainer> getAllMedia()
	{
		return getAllMedia( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMediaContainerlink.media</code> attribute. 
	 * @param value the media - A list of bundle promotion images
	 */
	public void setMedia(final SessionContext ctx, final MediaContainer value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPMediaContainerlink.setMedia requires a session language", 0 );
		}
		setLocalizedProperty(ctx, MEDIA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMediaContainerlink.media</code> attribute. 
	 * @param value the media - A list of bundle promotion images
	 */
	public void setMedia(final MediaContainer value)
	{
		setMedia( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMediaContainerlink.media</code> attribute. 
	 * @param value the media - A list of bundle promotion images
	 */
	public void setAllMedia(final SessionContext ctx, final Map<Language,MediaContainer> value)
	{
		setAllLocalizedProperties(ctx,MEDIA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMediaContainerlink.media</code> attribute. 
	 * @param value the media - A list of bundle promotion images
	 */
	public void setAllMedia(final Map<Language,MediaContainer> value)
	{
		setAllMedia( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMediaContainerlink.mediaUrl</code> attribute.
	 * @return the mediaUrl
	 */
	public String getMediaUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, MEDIAURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMediaContainerlink.mediaUrl</code> attribute.
	 * @return the mediaUrl
	 */
	public String getMediaUrl()
	{
		return getMediaUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMediaContainerlink.mediaUrl</code> attribute. 
	 * @param value the mediaUrl
	 */
	public void setMediaUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, MEDIAURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMediaContainerlink.mediaUrl</code> attribute. 
	 * @param value the mediaUrl
	 */
	public void setMediaUrl(final String value)
	{
		setMediaUrl( getSession().getSessionContext(), value );
	}
	
}
