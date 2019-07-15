/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.gpcommerceaddon.jalo;

import com.gp.commerce.core.jalo.GPHeaderNavigationComponent;
import com.gp.commerce.core.jalo.GPImageLinkComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.CMSLinkComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPHeaderNavigationComponent MardigrasHeaderNavigationComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMardigrasHeaderNavigationComponent extends GPHeaderNavigationComponent
{
	/** Qualifier of the <code>MardigrasHeaderNavigationComponent.socialLink</code> attribute **/
	public static final String SOCIALLINK = "socialLink";
	/** Qualifier of the <code>MardigrasHeaderNavigationComponent.socialIcon</code> attribute **/
	public static final String SOCIALICON = "socialIcon";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(GPHeaderNavigationComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SOCIALLINK, AttributeMode.INITIAL);
		tmp.put(SOCIALICON, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasHeaderNavigationComponent.socialIcon</code> attribute.
	 * @return the socialIcon - The cms navigation node of this navigation component.
	 */
	public GPImageLinkComponent getSocialIcon(final SessionContext ctx)
	{
		return (GPImageLinkComponent)getProperty( ctx, SOCIALICON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasHeaderNavigationComponent.socialIcon</code> attribute.
	 * @return the socialIcon - The cms navigation node of this navigation component.
	 */
	public GPImageLinkComponent getSocialIcon()
	{
		return getSocialIcon( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasHeaderNavigationComponent.socialIcon</code> attribute. 
	 * @param value the socialIcon - The cms navigation node of this navigation component.
	 */
	public void setSocialIcon(final SessionContext ctx, final GPImageLinkComponent value)
	{
		setProperty(ctx, SOCIALICON,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasHeaderNavigationComponent.socialIcon</code> attribute. 
	 * @param value the socialIcon - The cms navigation node of this navigation component.
	 */
	public void setSocialIcon(final GPImageLinkComponent value)
	{
		setSocialIcon( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasHeaderNavigationComponent.socialLink</code> attribute.
	 * @return the socialLink - The cms navigation node of this navigation component.
	 */
	public CMSLinkComponent getSocialLink(final SessionContext ctx)
	{
		return (CMSLinkComponent)getProperty( ctx, SOCIALLINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardigrasHeaderNavigationComponent.socialLink</code> attribute.
	 * @return the socialLink - The cms navigation node of this navigation component.
	 */
	public CMSLinkComponent getSocialLink()
	{
		return getSocialLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasHeaderNavigationComponent.socialLink</code> attribute. 
	 * @param value the socialLink - The cms navigation node of this navigation component.
	 */
	public void setSocialLink(final SessionContext ctx, final CMSLinkComponent value)
	{
		setProperty(ctx, SOCIALLINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardigrasHeaderNavigationComponent.socialLink</code> attribute. 
	 * @param value the socialLink - The cms navigation node of this navigation component.
	 */
	public void setSocialLink(final CMSLinkComponent value)
	{
		setSocialLink( getSession().getSessionContext(), value );
	}
	
}
