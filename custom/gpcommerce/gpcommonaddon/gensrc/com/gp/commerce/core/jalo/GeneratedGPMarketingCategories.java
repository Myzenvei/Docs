/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPTipsAndKnowledgeInfo;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPMarketingCategories GPMarketingCategories}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPMarketingCategories extends Category
{
	/** Qualifier of the <code>GPMarketingCategories.tipsAndKnowledgeInfoList</code> attribute **/
	public static final String TIPSANDKNOWLEDGEINFOLIST = "tipsAndKnowledgeInfoList";
	/** Relation ordering override parameter constants for GPKnowledgeTipRelation from ((gpcommonaddon))*/
	protected static String GPKNOWLEDGETIPRELATION_SRC_ORDERED = "relation.GPKnowledgeTipRelation.source.ordered";
	protected static String GPKNOWLEDGETIPRELATION_TGT_ORDERED = "relation.GPKnowledgeTipRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for GPKnowledgeTipRelation from ((gpcommonaddon))*/
	protected static String GPKNOWLEDGETIPRELATION_MARKMODIFIED = "relation.GPKnowledgeTipRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Category.DEFAULT_INITIAL_ATTRIBUTES);
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
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPTipsAndKnowledgeInfo");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(GPKNOWLEDGETIPRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingCategories.tipsAndKnowledgeInfoList</code> attribute.
	 * @return the tipsAndKnowledgeInfoList
	 */
	public List<GPTipsAndKnowledgeInfo> getTipsAndKnowledgeInfoList(final SessionContext ctx)
	{
		final List<GPTipsAndKnowledgeInfo> items = getLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			"GPTipsAndKnowledgeInfo",
			null,
			Utilities.getRelationOrderingOverride(GPKNOWLEDGETIPRELATION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingCategories.tipsAndKnowledgeInfoList</code> attribute.
	 * @return the tipsAndKnowledgeInfoList
	 */
	public List<GPTipsAndKnowledgeInfo> getTipsAndKnowledgeInfoList()
	{
		return getTipsAndKnowledgeInfoList( getSession().getSessionContext() );
	}
	
	public long getTipsAndKnowledgeInfoListCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			"GPTipsAndKnowledgeInfo",
			null
		);
	}
	
	public long getTipsAndKnowledgeInfoListCount()
	{
		return getTipsAndKnowledgeInfoListCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingCategories.tipsAndKnowledgeInfoList</code> attribute. 
	 * @param value the tipsAndKnowledgeInfoList
	 */
	public void setTipsAndKnowledgeInfoList(final SessionContext ctx, final List<GPTipsAndKnowledgeInfo> value)
	{
		setLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(GPKNOWLEDGETIPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(GPKNOWLEDGETIPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingCategories.tipsAndKnowledgeInfoList</code> attribute. 
	 * @param value the tipsAndKnowledgeInfoList
	 */
	public void setTipsAndKnowledgeInfoList(final List<GPTipsAndKnowledgeInfo> value)
	{
		setTipsAndKnowledgeInfoList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to tipsAndKnowledgeInfoList. 
	 * @param value the item to add to tipsAndKnowledgeInfoList
	 */
	public void addToTipsAndKnowledgeInfoList(final SessionContext ctx, final GPTipsAndKnowledgeInfo value)
	{
		addLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(GPKNOWLEDGETIPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(GPKNOWLEDGETIPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to tipsAndKnowledgeInfoList. 
	 * @param value the item to add to tipsAndKnowledgeInfoList
	 */
	public void addToTipsAndKnowledgeInfoList(final GPTipsAndKnowledgeInfo value)
	{
		addToTipsAndKnowledgeInfoList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from tipsAndKnowledgeInfoList. 
	 * @param value the item to remove from tipsAndKnowledgeInfoList
	 */
	public void removeFromTipsAndKnowledgeInfoList(final SessionContext ctx, final GPTipsAndKnowledgeInfo value)
	{
		removeLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(GPKNOWLEDGETIPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(GPKNOWLEDGETIPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from tipsAndKnowledgeInfoList. 
	 * @param value the item to remove from tipsAndKnowledgeInfoList
	 */
	public void removeFromTipsAndKnowledgeInfoList(final GPTipsAndKnowledgeInfo value)
	{
		removeFromTipsAndKnowledgeInfoList( getSession().getSessionContext(), value );
	}
	
}
