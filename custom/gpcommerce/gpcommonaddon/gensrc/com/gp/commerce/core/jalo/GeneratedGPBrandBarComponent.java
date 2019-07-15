/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPBannerComponent;
import com.gp.commerce.core.jalo.GPImageLinkComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
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
 * Generated class for type {@link com.gp.commerce.core.jalo.GPBrandBarComponent GPBrandBarComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPBrandBarComponent extends GPBannerComponent
{
	/** Qualifier of the <code>GPBrandBarComponent.images</code> attribute **/
	public static final String IMAGES = "images";
	/** Relation ordering override parameter constants for BrandBar2GpImageLinkRelation from ((gpcommonaddon))*/
	protected static String BRANDBAR2GPIMAGELINKRELATION_SRC_ORDERED = "relation.BrandBar2GpImageLinkRelation.source.ordered";
	protected static String BRANDBAR2GPIMAGELINKRELATION_TGT_ORDERED = "relation.BrandBar2GpImageLinkRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for BrandBar2GpImageLinkRelation from ((gpcommonaddon))*/
	protected static String BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED = "relation.BrandBar2GpImageLinkRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(GPBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBrandBarComponent.images</code> attribute.
	 * @return the images
	 */
	public Collection<GPImageLinkComponent> getImages(final SessionContext ctx)
	{
		final List<GPImageLinkComponent> items = getLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			"GPImageLinkComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBrandBarComponent.images</code> attribute.
	 * @return the images
	 */
	public Collection<GPImageLinkComponent> getImages()
	{
		return getImages( getSession().getSessionContext() );
	}
	
	public long getImagesCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			"GPImageLinkComponent",
			null
		);
	}
	
	public long getImagesCount()
	{
		return getImagesCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBrandBarComponent.images</code> attribute. 
	 * @param value the images
	 */
	public void setImages(final SessionContext ctx, final Collection<GPImageLinkComponent> value)
	{
		setLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBrandBarComponent.images</code> attribute. 
	 * @param value the images
	 */
	public void setImages(final Collection<GPImageLinkComponent> value)
	{
		setImages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to images. 
	 * @param value the item to add to images
	 */
	public void addToImages(final SessionContext ctx, final GPImageLinkComponent value)
	{
		addLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to images. 
	 * @param value the item to add to images
	 */
	public void addToImages(final GPImageLinkComponent value)
	{
		addToImages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from images. 
	 * @param value the item to remove from images
	 */
	public void removeFromImages(final SessionContext ctx, final GPImageLinkComponent value)
	{
		removeLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from images. 
	 * @param value the item to remove from images
	 */
	public void removeFromImages(final GPImageLinkComponent value)
	{
		removeFromImages( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPImageLinkComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED);
		}
		return true;
	}
	
}
