/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPProductSolutionCMSComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import com.gp.commerce.gpcommerceaddon.jalo.GPIndustrialSolutionsComponent;
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
 * Generated class for type {@link com.gp.commerce.core.jalo.GPTabbedImageTileComponent GPTabbedImageTileComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPTabbedImageTileComponent extends GPIndustrialSolutionsComponent
{
	/** Qualifier of the <code>GPTabbedImageTileComponent.productSolution</code> attribute **/
	public static final String PRODUCTSOLUTION = "productSolution";
	/** Relation ordering override parameter constants for GPProductSolutionCMSComponent2GPTabbedImageTileComponent from ((gpcommonaddon))*/
	protected static String GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_SRC_ORDERED = "relation.GPProductSolutionCMSComponent2GPTabbedImageTileComponent.source.ordered";
	protected static String GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_TGT_ORDERED = "relation.GPProductSolutionCMSComponent2GPTabbedImageTileComponent.target.ordered";
	/** Relation disable markmodifed parameter constants for GPProductSolutionCMSComponent2GPTabbedImageTileComponent from ((gpcommonaddon))*/
	protected static String GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED = "relation.GPProductSolutionCMSComponent2GPTabbedImageTileComponent.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(GPIndustrialSolutionsComponent.DEFAULT_INITIAL_ATTRIBUTES);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPProductSolutionCMSComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTabbedImageTileComponent.productSolution</code> attribute.
	 * @return the productSolution
	 */
	public Collection<GPProductSolutionCMSComponent> getProductSolution(final SessionContext ctx)
	{
		final List<GPProductSolutionCMSComponent> items = getLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			"GPProductSolutionCMSComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTabbedImageTileComponent.productSolution</code> attribute.
	 * @return the productSolution
	 */
	public Collection<GPProductSolutionCMSComponent> getProductSolution()
	{
		return getProductSolution( getSession().getSessionContext() );
	}
	
	public long getProductSolutionCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			"GPProductSolutionCMSComponent",
			null
		);
	}
	
	public long getProductSolutionCount()
	{
		return getProductSolutionCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTabbedImageTileComponent.productSolution</code> attribute. 
	 * @param value the productSolution
	 */
	public void setProductSolution(final SessionContext ctx, final Collection<GPProductSolutionCMSComponent> value)
	{
		setLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTabbedImageTileComponent.productSolution</code> attribute. 
	 * @param value the productSolution
	 */
	public void setProductSolution(final Collection<GPProductSolutionCMSComponent> value)
	{
		setProductSolution( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productSolution. 
	 * @param value the item to add to productSolution
	 */
	public void addToProductSolution(final SessionContext ctx, final GPProductSolutionCMSComponent value)
	{
		addLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productSolution. 
	 * @param value the item to add to productSolution
	 */
	public void addToProductSolution(final GPProductSolutionCMSComponent value)
	{
		addToProductSolution( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productSolution. 
	 * @param value the item to remove from productSolution
	 */
	public void removeFromProductSolution(final SessionContext ctx, final GPProductSolutionCMSComponent value)
	{
		removeLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productSolution. 
	 * @param value the item to remove from productSolution
	 */
	public void removeFromProductSolution(final GPProductSolutionCMSComponent value)
	{
		removeFromProductSolution( getSession().getSessionContext(), value );
	}
	
}
