/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.acceleratorcms.jalo.components.ProductReferencesComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPProductReferencesComponent GPProductReferencesComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPProductReferencesComponent extends ProductReferencesComponent
{
	/** Qualifier of the <code>GPProductReferencesComponent.productCodes</code> attribute **/
	public static final String PRODUCTCODES = "productCodes";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ProductReferencesComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(PRODUCTCODES, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductReferencesComponent.productCodes</code> attribute.
	 * @return the productCodes - Property for products to be displayed in the component example - code1:code2:code3
	 */
	public String getProductCodes(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPProductReferencesComponent.getProductCodes requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, PRODUCTCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductReferencesComponent.productCodes</code> attribute.
	 * @return the productCodes - Property for products to be displayed in the component example - code1:code2:code3
	 */
	public String getProductCodes()
	{
		return getProductCodes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductReferencesComponent.productCodes</code> attribute. 
	 * @return the localized productCodes - Property for products to be displayed in the component example - code1:code2:code3
	 */
	public Map<Language,String> getAllProductCodes(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,PRODUCTCODES,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductReferencesComponent.productCodes</code> attribute. 
	 * @return the localized productCodes - Property for products to be displayed in the component example - code1:code2:code3
	 */
	public Map<Language,String> getAllProductCodes()
	{
		return getAllProductCodes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductReferencesComponent.productCodes</code> attribute. 
	 * @param value the productCodes - Property for products to be displayed in the component example - code1:code2:code3
	 */
	public void setProductCodes(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPProductReferencesComponent.setProductCodes requires a session language", 0 );
		}
		setLocalizedProperty(ctx, PRODUCTCODES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductReferencesComponent.productCodes</code> attribute. 
	 * @param value the productCodes - Property for products to be displayed in the component example - code1:code2:code3
	 */
	public void setProductCodes(final String value)
	{
		setProductCodes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductReferencesComponent.productCodes</code> attribute. 
	 * @param value the productCodes - Property for products to be displayed in the component example - code1:code2:code3
	 */
	public void setAllProductCodes(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,PRODUCTCODES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductReferencesComponent.productCodes</code> attribute. 
	 * @param value the productCodes - Property for products to be displayed in the component example - code1:code2:code3
	 */
	public void setAllProductCodes(final Map<Language,String> value)
	{
		setAllProductCodes( getSession().getSessionContext(), value );
	}
	
}
