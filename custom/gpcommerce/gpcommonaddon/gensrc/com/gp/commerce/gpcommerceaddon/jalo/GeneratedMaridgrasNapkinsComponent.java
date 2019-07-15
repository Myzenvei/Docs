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
 * Generated class for type {@link com.gp.commerce.core.jalo.GPImageTileComponent MaridgrasNapkinsComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMaridgrasNapkinsComponent extends GPImageTileComponent
{
	/** Qualifier of the <code>MaridgrasNapkinsComponent.subTileText</code> attribute **/
	public static final String SUBTILETEXT = "subTileText";
	/** Qualifier of the <code>MaridgrasNapkinsComponent.informationalText</code> attribute **/
	public static final String INFORMATIONALTEXT = "informationalText";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(GPImageTileComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SUBTILETEXT, AttributeMode.INITIAL);
		tmp.put(INFORMATIONALTEXT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaridgrasNapkinsComponent.informationalText</code> attribute.
	 * @return the informationalText
	 */
	public String getInformationalText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, INFORMATIONALTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaridgrasNapkinsComponent.informationalText</code> attribute.
	 * @return the informationalText
	 */
	public String getInformationalText()
	{
		return getInformationalText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MaridgrasNapkinsComponent.informationalText</code> attribute. 
	 * @param value the informationalText
	 */
	public void setInformationalText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, INFORMATIONALTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MaridgrasNapkinsComponent.informationalText</code> attribute. 
	 * @param value the informationalText
	 */
	public void setInformationalText(final String value)
	{
		setInformationalText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaridgrasNapkinsComponent.subTileText</code> attribute.
	 * @return the subTileText
	 */
	public String getSubTileText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBTILETEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaridgrasNapkinsComponent.subTileText</code> attribute.
	 * @return the subTileText
	 */
	public String getSubTileText()
	{
		return getSubTileText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MaridgrasNapkinsComponent.subTileText</code> attribute. 
	 * @param value the subTileText
	 */
	public void setSubTileText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBTILETEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MaridgrasNapkinsComponent.subTileText</code> attribute. 
	 * @param value the subTileText
	 */
	public void setSubTileText(final String value)
	{
		setSubTileText( getSession().getSessionContext(), value );
	}
	
}
