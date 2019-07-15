/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPBannerComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.acceleratorcms.jalo.components.SimpleResponsiveBannerComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPMarketingSidebySideComponent GPMarketingSidebySideComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPMarketingSidebySideComponent extends SimpleResponsiveBannerComponent
{
	/** Qualifier of the <code>GPMarketingSidebySideComponent.backgroundColor</code> attribute **/
	public static final String BACKGROUNDCOLOR = "backgroundColor";
	/** Qualifier of the <code>GPMarketingSidebySideComponent.marketingSideBanner</code> attribute **/
	public static final String MARKETINGSIDEBANNER = "marketingSideBanner";
	/** Relation ordering override parameter constants for GpMarketingSideBySide2GpBannerRelation from ((gpcommonaddon))*/
	protected static String GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_SRC_ORDERED = "relation.GpMarketingSideBySide2GpBannerRelation.source.ordered";
	protected static String GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_TGT_ORDERED = "relation.GpMarketingSideBySide2GpBannerRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for GpMarketingSideBySide2GpBannerRelation from ((gpcommonaddon))*/
	protected static String GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED = "relation.GpMarketingSideBySide2GpBannerRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleResponsiveBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(BACKGROUNDCOLOR, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingSidebySideComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor - The background color for this  component.
	 */
	public String getBackgroundColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BACKGROUNDCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingSidebySideComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor - The background color for this  component.
	 */
	public String getBackgroundColor()
	{
		return getBackgroundColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingSidebySideComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor - The background color for this  component.
	 */
	public void setBackgroundColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BACKGROUNDCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingSidebySideComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor - The background color for this  component.
	 */
	public void setBackgroundColor(final String value)
	{
		setBackgroundColor( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPBannerComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingSidebySideComponent.marketingSideBanner</code> attribute.
	 * @return the marketingSideBanner
	 */
	public Collection<GPBannerComponent> getMarketingSideBanner(final SessionContext ctx)
	{
		final List<GPBannerComponent> items = getLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			"GPBannerComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingSidebySideComponent.marketingSideBanner</code> attribute.
	 * @return the marketingSideBanner
	 */
	public Collection<GPBannerComponent> getMarketingSideBanner()
	{
		return getMarketingSideBanner( getSession().getSessionContext() );
	}
	
	public long getMarketingSideBannerCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			"GPBannerComponent",
			null
		);
	}
	
	public long getMarketingSideBannerCount()
	{
		return getMarketingSideBannerCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingSidebySideComponent.marketingSideBanner</code> attribute. 
	 * @param value the marketingSideBanner
	 */
	public void setMarketingSideBanner(final SessionContext ctx, final Collection<GPBannerComponent> value)
	{
		setLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingSidebySideComponent.marketingSideBanner</code> attribute. 
	 * @param value the marketingSideBanner
	 */
	public void setMarketingSideBanner(final Collection<GPBannerComponent> value)
	{
		setMarketingSideBanner( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to marketingSideBanner. 
	 * @param value the item to add to marketingSideBanner
	 */
	public void addToMarketingSideBanner(final SessionContext ctx, final GPBannerComponent value)
	{
		addLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to marketingSideBanner. 
	 * @param value the item to add to marketingSideBanner
	 */
	public void addToMarketingSideBanner(final GPBannerComponent value)
	{
		addToMarketingSideBanner( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from marketingSideBanner. 
	 * @param value the item to remove from marketingSideBanner
	 */
	public void removeFromMarketingSideBanner(final SessionContext ctx, final GPBannerComponent value)
	{
		removeLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from marketingSideBanner. 
	 * @param value the item to remove from marketingSideBanner
	 */
	public void removeFromMarketingSideBanner(final GPBannerComponent value)
	{
		removeFromMarketingSideBanner( getSession().getSessionContext(), value );
	}
	
}
