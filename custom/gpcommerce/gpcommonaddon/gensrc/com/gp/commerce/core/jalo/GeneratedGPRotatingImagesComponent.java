/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.acceleratorcms.jalo.components.SimpleResponsiveBannerComponent;
import de.hybris.platform.cms2lib.components.RotatingImagesComponent;
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
 * Generated class for type {@link com.gp.commerce.core.jalo.GPRotatingImagesComponent GPRotatingImagesComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPRotatingImagesComponent extends RotatingImagesComponent
{
	/** Qualifier of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute **/
	public static final String LOOPCAROUSEL = "loopCarousel";
	/** Qualifier of the <code>GPRotatingImagesComponent.cmsComponents</code> attribute **/
	public static final String CMSCOMPONENTS = "cmsComponents";
	/** Relation ordering override parameter constants for Rotating2simpleResponsiveRelation from ((gpcommonaddon))*/
	protected static String ROTATING2SIMPLERESPONSIVERELATION_SRC_ORDERED = "relation.Rotating2simpleResponsiveRelation.source.ordered";
	protected static String ROTATING2SIMPLERESPONSIVERELATION_TGT_ORDERED = "relation.Rotating2simpleResponsiveRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for Rotating2simpleResponsiveRelation from ((gpcommonaddon))*/
	protected static String ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED = "relation.Rotating2simpleResponsiveRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(RotatingImagesComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(LOOPCAROUSEL, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPRotatingImagesComponent.cmsComponents</code> attribute.
	 * @return the cmsComponents
	 */
	public Collection<SimpleResponsiveBannerComponent> getCmsComponents(final SessionContext ctx)
	{
		final List<SimpleResponsiveBannerComponent> items = getLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			"SimpleResponsiveBannerComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPRotatingImagesComponent.cmsComponents</code> attribute.
	 * @return the cmsComponents
	 */
	public Collection<SimpleResponsiveBannerComponent> getCmsComponents()
	{
		return getCmsComponents( getSession().getSessionContext() );
	}
	
	public long getCmsComponentsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			"SimpleResponsiveBannerComponent",
			null
		);
	}
	
	public long getCmsComponentsCount()
	{
		return getCmsComponentsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPRotatingImagesComponent.cmsComponents</code> attribute. 
	 * @param value the cmsComponents
	 */
	public void setCmsComponents(final SessionContext ctx, final Collection<SimpleResponsiveBannerComponent> value)
	{
		setLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPRotatingImagesComponent.cmsComponents</code> attribute. 
	 * @param value the cmsComponents
	 */
	public void setCmsComponents(final Collection<SimpleResponsiveBannerComponent> value)
	{
		setCmsComponents( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to cmsComponents. 
	 * @param value the item to add to cmsComponents
	 */
	public void addToCmsComponents(final SessionContext ctx, final SimpleResponsiveBannerComponent value)
	{
		addLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to cmsComponents. 
	 * @param value the item to add to cmsComponents
	 */
	public void addToCmsComponents(final SimpleResponsiveBannerComponent value)
	{
		addToCmsComponents( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from cmsComponents. 
	 * @param value the item to remove from cmsComponents
	 */
	public void removeFromCmsComponents(final SessionContext ctx, final SimpleResponsiveBannerComponent value)
	{
		removeLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from cmsComponents. 
	 * @param value the item to remove from cmsComponents
	 */
	public void removeFromCmsComponents(final SimpleResponsiveBannerComponent value)
	{
		removeFromCmsComponents( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("SimpleResponsiveBannerComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute.
	 * @return the loopCarousel - The component header color attached to this  component.
	 */
	public Boolean isLoopCarousel(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, LOOPCAROUSEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute.
	 * @return the loopCarousel - The component header color attached to this  component.
	 */
	public Boolean isLoopCarousel()
	{
		return isLoopCarousel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute. 
	 * @return the loopCarousel - The component header color attached to this  component.
	 */
	public boolean isLoopCarouselAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isLoopCarousel( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute. 
	 * @return the loopCarousel - The component header color attached to this  component.
	 */
	public boolean isLoopCarouselAsPrimitive()
	{
		return isLoopCarouselAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute. 
	 * @param value the loopCarousel - The component header color attached to this  component.
	 */
	public void setLoopCarousel(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, LOOPCAROUSEL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute. 
	 * @param value the loopCarousel - The component header color attached to this  component.
	 */
	public void setLoopCarousel(final Boolean value)
	{
		setLoopCarousel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute. 
	 * @param value the loopCarousel - The component header color attached to this  component.
	 */
	public void setLoopCarousel(final SessionContext ctx, final boolean value)
	{
		setLoopCarousel( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPRotatingImagesComponent.loopCarousel</code> attribute. 
	 * @param value the loopCarousel - The component header color attached to this  component.
	 */
	public void setLoopCarousel(final boolean value)
	{
		setLoopCarousel( getSession().getSessionContext(), value );
	}
	
}
