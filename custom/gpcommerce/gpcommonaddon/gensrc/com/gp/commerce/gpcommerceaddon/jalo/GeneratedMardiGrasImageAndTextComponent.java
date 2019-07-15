/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.gpcommerceaddon.jalo;

import com.gp.commerce.core.jalo.GPMediaComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.gpcommerceaddon.jalo.MardiGrasImageAndTextComponent MardiGrasImageAndTextComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMardiGrasImageAndTextComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>MardiGrasImageAndTextComponent.image</code> attribute **/
	public static final String IMAGE = "image";
	/** Qualifier of the <code>MardiGrasImageAndTextComponent.headingText</code> attribute **/
	public static final String HEADINGTEXT = "headingText";
	/** Qualifier of the <code>MardiGrasImageAndTextComponent.subHeadingText</code> attribute **/
	public static final String SUBHEADINGTEXT = "subHeadingText";
	/** Qualifier of the <code>MardiGrasImageAndTextComponent.informationText</code> attribute **/
	public static final String INFORMATIONTEXT = "informationText";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(IMAGE, AttributeMode.INITIAL);
		tmp.put(HEADINGTEXT, AttributeMode.INITIAL);
		tmp.put(SUBHEADINGTEXT, AttributeMode.INITIAL);
		tmp.put(INFORMATIONTEXT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasImageAndTextComponent.headingText</code> attribute.
	 * @return the headingText
	 */
	public String getHeadingText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADINGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasImageAndTextComponent.headingText</code> attribute.
	 * @return the headingText
	 */
	public String getHeadingText()
	{
		return getHeadingText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasImageAndTextComponent.headingText</code> attribute. 
	 * @param value the headingText
	 */
	public void setHeadingText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADINGTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasImageAndTextComponent.headingText</code> attribute. 
	 * @param value the headingText
	 */
	public void setHeadingText(final String value)
	{
		setHeadingText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasImageAndTextComponent.image</code> attribute.
	 * @return the image
	 */
	public GPMediaComponent getImage(final SessionContext ctx)
	{
		return (GPMediaComponent)getProperty( ctx, IMAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasImageAndTextComponent.image</code> attribute.
	 * @return the image
	 */
	public GPMediaComponent getImage()
	{
		return getImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasImageAndTextComponent.image</code> attribute. 
	 * @param value the image
	 */
	public void setImage(final SessionContext ctx, final GPMediaComponent value)
	{
		setProperty(ctx, IMAGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasImageAndTextComponent.image</code> attribute. 
	 * @param value the image
	 */
	public void setImage(final GPMediaComponent value)
	{
		setImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasImageAndTextComponent.informationText</code> attribute.
	 * @return the informationText
	 */
	public String getInformationText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, INFORMATIONTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasImageAndTextComponent.informationText</code> attribute.
	 * @return the informationText
	 */
	public String getInformationText()
	{
		return getInformationText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasImageAndTextComponent.informationText</code> attribute. 
	 * @param value the informationText
	 */
	public void setInformationText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, INFORMATIONTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasImageAndTextComponent.informationText</code> attribute. 
	 * @param value the informationText
	 */
	public void setInformationText(final String value)
	{
		setInformationText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasImageAndTextComponent.subHeadingText</code> attribute.
	 * @return the subHeadingText
	 */
	public String getSubHeadingText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADINGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MardiGrasImageAndTextComponent.subHeadingText</code> attribute.
	 * @return the subHeadingText
	 */
	public String getSubHeadingText()
	{
		return getSubHeadingText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasImageAndTextComponent.subHeadingText</code> attribute. 
	 * @param value the subHeadingText
	 */
	public void setSubHeadingText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADINGTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MardiGrasImageAndTextComponent.subHeadingText</code> attribute. 
	 * @param value the subHeadingText
	 */
	public void setSubHeadingText(final String value)
	{
		setSubHeadingText( getSession().getSessionContext(), value );
	}
	
}
