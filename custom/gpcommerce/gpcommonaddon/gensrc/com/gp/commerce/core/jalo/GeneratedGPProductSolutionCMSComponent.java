/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPTabbedImageTileComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
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
 * Generated class for type {@link com.gp.commerce.core.jalo.GPProductSolutionCMSComponent GPProductSolutionCMSComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPProductSolutionCMSComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>GPProductSolutionCMSComponent.ctaLink</code> attribute **/
	public static final String CTALINK = "ctaLink";
	/** Qualifier of the <code>GPProductSolutionCMSComponent.ctaText</code> attribute **/
	public static final String CTATEXT = "ctaText";
	/** Qualifier of the <code>GPProductSolutionCMSComponent.subHeadingText</code> attribute **/
	public static final String SUBHEADINGTEXT = "subHeadingText";
	/** Qualifier of the <code>GPProductSolutionCMSComponent.subHeadingColor</code> attribute **/
	public static final String SUBHEADINGCOLOR = "subHeadingColor";
	/** Qualifier of the <code>GPProductSolutionCMSComponent.headerText</code> attribute **/
	public static final String HEADERTEXT = "headerText";
	/** Qualifier of the <code>GPProductSolutionCMSComponent.headerColor</code> attribute **/
	public static final String HEADERCOLOR = "headerColor";
	/** Qualifier of the <code>GPProductSolutionCMSComponent.tabs</code> attribute **/
	public static final String TABS = "tabs";
	/** Relation ordering override parameter constants for GPProductSolutionCMSComponent2GPTabbedImageTileComponent from ((gpcommonaddon))*/
	protected static String GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_SRC_ORDERED = "relation.GPProductSolutionCMSComponent2GPTabbedImageTileComponent.source.ordered";
	protected static String GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_TGT_ORDERED = "relation.GPProductSolutionCMSComponent2GPTabbedImageTileComponent.target.ordered";
	/** Relation disable markmodifed parameter constants for GPProductSolutionCMSComponent2GPTabbedImageTileComponent from ((gpcommonaddon))*/
	protected static String GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED = "relation.GPProductSolutionCMSComponent2GPTabbedImageTileComponent.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CTALINK, AttributeMode.INITIAL);
		tmp.put(CTATEXT, AttributeMode.INITIAL);
		tmp.put(SUBHEADINGTEXT, AttributeMode.INITIAL);
		tmp.put(SUBHEADINGCOLOR, AttributeMode.INITIAL);
		tmp.put(HEADERTEXT, AttributeMode.INITIAL);
		tmp.put(HEADERCOLOR, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.ctaLink</code> attribute.
	 * @return the ctaLink
	 */
	public String getCtaLink(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTALINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.ctaLink</code> attribute.
	 * @return the ctaLink
	 */
	public String getCtaLink()
	{
		return getCtaLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.ctaLink</code> attribute. 
	 * @param value the ctaLink
	 */
	public void setCtaLink(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTALINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.ctaLink</code> attribute. 
	 * @param value the ctaLink
	 */
	public void setCtaLink(final String value)
	{
		setCtaLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.ctaText</code> attribute.
	 * @return the ctaText
	 */
	public String getCtaText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.ctaText</code> attribute.
	 * @return the ctaText
	 */
	public String getCtaText()
	{
		return getCtaText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.ctaText</code> attribute. 
	 * @param value the ctaText
	 */
	public void setCtaText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.ctaText</code> attribute. 
	 * @param value the ctaText
	 */
	public void setCtaText(final String value)
	{
		setCtaText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.headerColor</code> attribute.
	 * @return the headerColor
	 */
	public String getHeaderColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADERCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.headerColor</code> attribute.
	 * @return the headerColor
	 */
	public String getHeaderColor()
	{
		return getHeaderColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.headerColor</code> attribute. 
	 * @param value the headerColor
	 */
	public void setHeaderColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADERCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.headerColor</code> attribute. 
	 * @param value the headerColor
	 */
	public void setHeaderColor(final String value)
	{
		setHeaderColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.headerText</code> attribute.
	 * @return the headerText
	 */
	public String getHeaderText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADERTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.headerText</code> attribute.
	 * @return the headerText
	 */
	public String getHeaderText()
	{
		return getHeaderText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.headerText</code> attribute. 
	 * @param value the headerText
	 */
	public void setHeaderText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADERTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.headerText</code> attribute. 
	 * @param value the headerText
	 */
	public void setHeaderText(final String value)
	{
		setHeaderText( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPTabbedImageTileComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.subHeadingColor</code> attribute.
	 * @return the subHeadingColor
	 */
	public String getSubHeadingColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADINGCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.subHeadingColor</code> attribute.
	 * @return the subHeadingColor
	 */
	public String getSubHeadingColor()
	{
		return getSubHeadingColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.subHeadingColor</code> attribute. 
	 * @param value the subHeadingColor
	 */
	public void setSubHeadingColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADINGCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.subHeadingColor</code> attribute. 
	 * @param value the subHeadingColor
	 */
	public void setSubHeadingColor(final String value)
	{
		setSubHeadingColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.subHeadingText</code> attribute.
	 * @return the subHeadingText
	 */
	public String getSubHeadingText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADINGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.subHeadingText</code> attribute.
	 * @return the subHeadingText
	 */
	public String getSubHeadingText()
	{
		return getSubHeadingText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.subHeadingText</code> attribute. 
	 * @param value the subHeadingText
	 */
	public void setSubHeadingText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADINGTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.subHeadingText</code> attribute. 
	 * @param value the subHeadingText
	 */
	public void setSubHeadingText(final String value)
	{
		setSubHeadingText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.tabs</code> attribute.
	 * @return the tabs
	 */
	public Collection<GPTabbedImageTileComponent> getTabs(final SessionContext ctx)
	{
		final List<GPTabbedImageTileComponent> items = getLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			"GPTabbedImageTileComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPProductSolutionCMSComponent.tabs</code> attribute.
	 * @return the tabs
	 */
	public Collection<GPTabbedImageTileComponent> getTabs()
	{
		return getTabs( getSession().getSessionContext() );
	}
	
	public long getTabsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			"GPTabbedImageTileComponent",
			null
		);
	}
	
	public long getTabsCount()
	{
		return getTabsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.tabs</code> attribute. 
	 * @param value the tabs
	 */
	public void setTabs(final SessionContext ctx, final Collection<GPTabbedImageTileComponent> value)
	{
		setLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPProductSolutionCMSComponent.tabs</code> attribute. 
	 * @param value the tabs
	 */
	public void setTabs(final Collection<GPTabbedImageTileComponent> value)
	{
		setTabs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to tabs. 
	 * @param value the item to add to tabs
	 */
	public void addToTabs(final SessionContext ctx, final GPTabbedImageTileComponent value)
	{
		addLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to tabs. 
	 * @param value the item to add to tabs
	 */
	public void addToTabs(final GPTabbedImageTileComponent value)
	{
		addToTabs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from tabs. 
	 * @param value the item to remove from tabs
	 */
	public void removeFromTabs(final SessionContext ctx, final GPTabbedImageTileComponent value)
	{
		removeLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPPRODUCTSOLUTIONCMSCOMPONENT2GPTABBEDIMAGETILECOMPONENT_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from tabs. 
	 * @param value the item to remove from tabs
	 */
	public void removeFromTabs(final GPTabbedImageTileComponent value)
	{
		removeFromTabs( getSession().getSessionContext(), value );
	}
	
}
