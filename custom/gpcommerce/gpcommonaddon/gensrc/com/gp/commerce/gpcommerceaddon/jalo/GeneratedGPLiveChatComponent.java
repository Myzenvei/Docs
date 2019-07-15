/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.gpcommerceaddon.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import com.gp.commerce.gpcommerceaddon.jalo.MardiGrasImageAndTextComponent;
import de.hybris.platform.acceleratorcms.jalo.components.JspIncludeComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.gpcommerceaddon.jalo.GPLiveChatComponent GPLiveChatComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPLiveChatComponent extends MardiGrasImageAndTextComponent
{
	/** Qualifier of the <code>GPLiveChatComponent.liveChat</code> attribute **/
	public static final String LIVECHAT = "liveChat";
	/** Qualifier of the <code>GPLiveChatComponent.showContactUs</code> attribute **/
	public static final String SHOWCONTACTUS = "showContactUs";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(MardiGrasImageAndTextComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(LIVECHAT, AttributeMode.INITIAL);
		tmp.put(SHOWCONTACTUS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPLiveChatComponent.liveChat</code> attribute.
	 * @return the liveChat
	 */
	public JspIncludeComponent getLiveChat(final SessionContext ctx)
	{
		return (JspIncludeComponent)getProperty( ctx, LIVECHAT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPLiveChatComponent.liveChat</code> attribute.
	 * @return the liveChat
	 */
	public JspIncludeComponent getLiveChat()
	{
		return getLiveChat( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPLiveChatComponent.liveChat</code> attribute. 
	 * @param value the liveChat
	 */
	public void setLiveChat(final SessionContext ctx, final JspIncludeComponent value)
	{
		setProperty(ctx, LIVECHAT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPLiveChatComponent.liveChat</code> attribute. 
	 * @param value the liveChat
	 */
	public void setLiveChat(final JspIncludeComponent value)
	{
		setLiveChat( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPLiveChatComponent.showContactUs</code> attribute.
	 * @return the showContactUs
	 */
	public Boolean isShowContactUs(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWCONTACTUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPLiveChatComponent.showContactUs</code> attribute.
	 * @return the showContactUs
	 */
	public Boolean isShowContactUs()
	{
		return isShowContactUs( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPLiveChatComponent.showContactUs</code> attribute. 
	 * @return the showContactUs
	 */
	public boolean isShowContactUsAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowContactUs( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPLiveChatComponent.showContactUs</code> attribute. 
	 * @return the showContactUs
	 */
	public boolean isShowContactUsAsPrimitive()
	{
		return isShowContactUsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPLiveChatComponent.showContactUs</code> attribute. 
	 * @param value the showContactUs
	 */
	public void setShowContactUs(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWCONTACTUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPLiveChatComponent.showContactUs</code> attribute. 
	 * @param value the showContactUs
	 */
	public void setShowContactUs(final Boolean value)
	{
		setShowContactUs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPLiveChatComponent.showContactUs</code> attribute. 
	 * @param value the showContactUs
	 */
	public void setShowContactUs(final SessionContext ctx, final boolean value)
	{
		setShowContactUs( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPLiveChatComponent.showContactUs</code> attribute. 
	 * @param value the showContactUs
	 */
	public void setShowContactUs(final boolean value)
	{
		setShowContactUs( getSession().getSessionContext(), value );
	}
	
}
