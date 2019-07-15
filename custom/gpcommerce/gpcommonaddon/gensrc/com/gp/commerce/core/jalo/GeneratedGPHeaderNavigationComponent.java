/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.acceleratorcms.jalo.components.NavigationComponent;
import de.hybris.platform.cms2.jalo.navigation.CMSNavigationNode;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPHeaderNavigationComponent GPHeaderNavigationComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPHeaderNavigationComponent extends NavigationComponent
{
	/** Qualifier of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute **/
	public static final String SHOWQUICKORDER = "showQuickOrder";
	/** Qualifier of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute **/
	public static final String INCLUDEMINICART = "includeMiniCart";
	/** Qualifier of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute **/
	public static final String INCLUDESEARCHBOX = "includeSearchBox";
	/** Qualifier of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute **/
	public static final String SHOWFINDASTORE = "showFindAStore";
	/** Qualifier of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute **/
	public static final String SHOWLOGINREGISTER = "showLoginRegister";
	/** Qualifier of the <code>GPHeaderNavigationComponent.promoTxt</code> attribute **/
	public static final String PROMOTXT = "promoTxt";
	/** Qualifier of the <code>GPHeaderNavigationComponent.promoUrl</code> attribute **/
	public static final String PROMOURL = "promoUrl";
	/** Qualifier of the <code>GPHeaderNavigationComponent.brandLogo</code> attribute **/
	public static final String BRANDLOGO = "brandLogo";
	/** Qualifier of the <code>GPHeaderNavigationComponent.checkoutMenu</code> attribute **/
	public static final String CHECKOUTMENU = "checkoutMenu";
	/** Qualifier of the <code>GPHeaderNavigationComponent.findStoreMenu</code> attribute **/
	public static final String FINDSTOREMENU = "findStoreMenu";
	/** Qualifier of the <code>GPHeaderNavigationComponent.loginMenu</code> attribute **/
	public static final String LOGINMENU = "loginMenu";
	/** Qualifier of the <code>GPHeaderNavigationComponent.logoutMenu</code> attribute **/
	public static final String LOGOUTMENU = "logoutMenu";
	/** Qualifier of the <code>GPHeaderNavigationComponent.categoryNavigationMenu</code> attribute **/
	public static final String CATEGORYNAVIGATIONMENU = "categoryNavigationMenu";
	/** Qualifier of the <code>GPHeaderNavigationComponent.accountNavigationMenu</code> attribute **/
	public static final String ACCOUNTNAVIGATIONMENU = "accountNavigationMenu";
	/** Qualifier of the <code>GPHeaderNavigationComponent.showMyList</code> attribute **/
	public static final String SHOWMYLIST = "showMyList";
	/** Qualifier of the <code>GPHeaderNavigationComponent.brandMobileLogo</code> attribute **/
	public static final String BRANDMOBILELOGO = "brandMobileLogo";
	/** Qualifier of the <code>GPHeaderNavigationComponent.checkoutLogo</code> attribute **/
	public static final String CHECKOUTLOGO = "checkoutLogo";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(NavigationComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SHOWQUICKORDER, AttributeMode.INITIAL);
		tmp.put(INCLUDEMINICART, AttributeMode.INITIAL);
		tmp.put(INCLUDESEARCHBOX, AttributeMode.INITIAL);
		tmp.put(SHOWFINDASTORE, AttributeMode.INITIAL);
		tmp.put(SHOWLOGINREGISTER, AttributeMode.INITIAL);
		tmp.put(PROMOTXT, AttributeMode.INITIAL);
		tmp.put(PROMOURL, AttributeMode.INITIAL);
		tmp.put(BRANDLOGO, AttributeMode.INITIAL);
		tmp.put(CHECKOUTMENU, AttributeMode.INITIAL);
		tmp.put(FINDSTOREMENU, AttributeMode.INITIAL);
		tmp.put(LOGINMENU, AttributeMode.INITIAL);
		tmp.put(LOGOUTMENU, AttributeMode.INITIAL);
		tmp.put(CATEGORYNAVIGATIONMENU, AttributeMode.INITIAL);
		tmp.put(ACCOUNTNAVIGATIONMENU, AttributeMode.INITIAL);
		tmp.put(SHOWMYLIST, AttributeMode.INITIAL);
		tmp.put(BRANDMOBILELOGO, AttributeMode.INITIAL);
		tmp.put(CHECKOUTLOGO, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.accountNavigationMenu</code> attribute.
	 * @return the accountNavigationMenu
	 */
	public CMSNavigationNode getAccountNavigationMenu(final SessionContext ctx)
	{
		return (CMSNavigationNode)getProperty( ctx, ACCOUNTNAVIGATIONMENU);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.accountNavigationMenu</code> attribute.
	 * @return the accountNavigationMenu
	 */
	public CMSNavigationNode getAccountNavigationMenu()
	{
		return getAccountNavigationMenu( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.accountNavigationMenu</code> attribute. 
	 * @param value the accountNavigationMenu
	 */
	public void setAccountNavigationMenu(final SessionContext ctx, final CMSNavigationNode value)
	{
		setProperty(ctx, ACCOUNTNAVIGATIONMENU,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.accountNavigationMenu</code> attribute. 
	 * @param value the accountNavigationMenu
	 */
	public void setAccountNavigationMenu(final CMSNavigationNode value)
	{
		setAccountNavigationMenu( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.brandLogo</code> attribute.
	 * @return the brandLogo
	 */
	public String getBrandLogo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BRANDLOGO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.brandLogo</code> attribute.
	 * @return the brandLogo
	 */
	public String getBrandLogo()
	{
		return getBrandLogo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.brandLogo</code> attribute. 
	 * @param value the brandLogo
	 */
	public void setBrandLogo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BRANDLOGO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.brandLogo</code> attribute. 
	 * @param value the brandLogo
	 */
	public void setBrandLogo(final String value)
	{
		setBrandLogo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.brandMobileLogo</code> attribute.
	 * @return the brandMobileLogo
	 */
	public String getBrandMobileLogo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BRANDMOBILELOGO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.brandMobileLogo</code> attribute.
	 * @return the brandMobileLogo
	 */
	public String getBrandMobileLogo()
	{
		return getBrandMobileLogo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.brandMobileLogo</code> attribute. 
	 * @param value the brandMobileLogo
	 */
	public void setBrandMobileLogo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BRANDMOBILELOGO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.brandMobileLogo</code> attribute. 
	 * @param value the brandMobileLogo
	 */
	public void setBrandMobileLogo(final String value)
	{
		setBrandMobileLogo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.categoryNavigationMenu</code> attribute.
	 * @return the categoryNavigationMenu
	 */
	public CMSNavigationNode getCategoryNavigationMenu(final SessionContext ctx)
	{
		return (CMSNavigationNode)getProperty( ctx, CATEGORYNAVIGATIONMENU);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.categoryNavigationMenu</code> attribute.
	 * @return the categoryNavigationMenu
	 */
	public CMSNavigationNode getCategoryNavigationMenu()
	{
		return getCategoryNavigationMenu( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.categoryNavigationMenu</code> attribute. 
	 * @param value the categoryNavigationMenu
	 */
	public void setCategoryNavigationMenu(final SessionContext ctx, final CMSNavigationNode value)
	{
		setProperty(ctx, CATEGORYNAVIGATIONMENU,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.categoryNavigationMenu</code> attribute. 
	 * @param value the categoryNavigationMenu
	 */
	public void setCategoryNavigationMenu(final CMSNavigationNode value)
	{
		setCategoryNavigationMenu( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.checkoutLogo</code> attribute.
	 * @return the checkoutLogo
	 */
	public String getCheckoutLogo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CHECKOUTLOGO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.checkoutLogo</code> attribute.
	 * @return the checkoutLogo
	 */
	public String getCheckoutLogo()
	{
		return getCheckoutLogo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.checkoutLogo</code> attribute. 
	 * @param value the checkoutLogo
	 */
	public void setCheckoutLogo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CHECKOUTLOGO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.checkoutLogo</code> attribute. 
	 * @param value the checkoutLogo
	 */
	public void setCheckoutLogo(final String value)
	{
		setCheckoutLogo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.checkoutMenu</code> attribute.
	 * @return the checkoutMenu
	 */
	public CMSNavigationNode getCheckoutMenu(final SessionContext ctx)
	{
		return (CMSNavigationNode)getProperty( ctx, CHECKOUTMENU);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.checkoutMenu</code> attribute.
	 * @return the checkoutMenu
	 */
	public CMSNavigationNode getCheckoutMenu()
	{
		return getCheckoutMenu( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.checkoutMenu</code> attribute. 
	 * @param value the checkoutMenu
	 */
	public void setCheckoutMenu(final SessionContext ctx, final CMSNavigationNode value)
	{
		setProperty(ctx, CHECKOUTMENU,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.checkoutMenu</code> attribute. 
	 * @param value the checkoutMenu
	 */
	public void setCheckoutMenu(final CMSNavigationNode value)
	{
		setCheckoutMenu( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.findStoreMenu</code> attribute.
	 * @return the findStoreMenu
	 */
	public CMSNavigationNode getFindStoreMenu(final SessionContext ctx)
	{
		return (CMSNavigationNode)getProperty( ctx, FINDSTOREMENU);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.findStoreMenu</code> attribute.
	 * @return the findStoreMenu
	 */
	public CMSNavigationNode getFindStoreMenu()
	{
		return getFindStoreMenu( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.findStoreMenu</code> attribute. 
	 * @param value the findStoreMenu
	 */
	public void setFindStoreMenu(final SessionContext ctx, final CMSNavigationNode value)
	{
		setProperty(ctx, FINDSTOREMENU,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.findStoreMenu</code> attribute. 
	 * @param value the findStoreMenu
	 */
	public void setFindStoreMenu(final CMSNavigationNode value)
	{
		setFindStoreMenu( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute.
	 * @return the includeMiniCart
	 */
	public Boolean isIncludeMiniCart(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, INCLUDEMINICART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute.
	 * @return the includeMiniCart
	 */
	public Boolean isIncludeMiniCart()
	{
		return isIncludeMiniCart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute. 
	 * @return the includeMiniCart
	 */
	public boolean isIncludeMiniCartAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIncludeMiniCart( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute. 
	 * @return the includeMiniCart
	 */
	public boolean isIncludeMiniCartAsPrimitive()
	{
		return isIncludeMiniCartAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute. 
	 * @param value the includeMiniCart
	 */
	public void setIncludeMiniCart(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, INCLUDEMINICART,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute. 
	 * @param value the includeMiniCart
	 */
	public void setIncludeMiniCart(final Boolean value)
	{
		setIncludeMiniCart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute. 
	 * @param value the includeMiniCart
	 */
	public void setIncludeMiniCart(final SessionContext ctx, final boolean value)
	{
		setIncludeMiniCart( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.includeMiniCart</code> attribute. 
	 * @param value the includeMiniCart
	 */
	public void setIncludeMiniCart(final boolean value)
	{
		setIncludeMiniCart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute.
	 * @return the includeSearchBox
	 */
	public Boolean isIncludeSearchBox(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, INCLUDESEARCHBOX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute.
	 * @return the includeSearchBox
	 */
	public Boolean isIncludeSearchBox()
	{
		return isIncludeSearchBox( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute. 
	 * @return the includeSearchBox
	 */
	public boolean isIncludeSearchBoxAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIncludeSearchBox( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute. 
	 * @return the includeSearchBox
	 */
	public boolean isIncludeSearchBoxAsPrimitive()
	{
		return isIncludeSearchBoxAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute. 
	 * @param value the includeSearchBox
	 */
	public void setIncludeSearchBox(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, INCLUDESEARCHBOX,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute. 
	 * @param value the includeSearchBox
	 */
	public void setIncludeSearchBox(final Boolean value)
	{
		setIncludeSearchBox( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute. 
	 * @param value the includeSearchBox
	 */
	public void setIncludeSearchBox(final SessionContext ctx, final boolean value)
	{
		setIncludeSearchBox( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.includeSearchBox</code> attribute. 
	 * @param value the includeSearchBox
	 */
	public void setIncludeSearchBox(final boolean value)
	{
		setIncludeSearchBox( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.loginMenu</code> attribute.
	 * @return the loginMenu
	 */
	public CMSNavigationNode getLoginMenu(final SessionContext ctx)
	{
		return (CMSNavigationNode)getProperty( ctx, LOGINMENU);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.loginMenu</code> attribute.
	 * @return the loginMenu
	 */
	public CMSNavigationNode getLoginMenu()
	{
		return getLoginMenu( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.loginMenu</code> attribute. 
	 * @param value the loginMenu
	 */
	public void setLoginMenu(final SessionContext ctx, final CMSNavigationNode value)
	{
		setProperty(ctx, LOGINMENU,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.loginMenu</code> attribute. 
	 * @param value the loginMenu
	 */
	public void setLoginMenu(final CMSNavigationNode value)
	{
		setLoginMenu( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.logoutMenu</code> attribute.
	 * @return the logoutMenu
	 */
	public CMSNavigationNode getLogoutMenu(final SessionContext ctx)
	{
		return (CMSNavigationNode)getProperty( ctx, LOGOUTMENU);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.logoutMenu</code> attribute.
	 * @return the logoutMenu
	 */
	public CMSNavigationNode getLogoutMenu()
	{
		return getLogoutMenu( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.logoutMenu</code> attribute. 
	 * @param value the logoutMenu
	 */
	public void setLogoutMenu(final SessionContext ctx, final CMSNavigationNode value)
	{
		setProperty(ctx, LOGOUTMENU,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.logoutMenu</code> attribute. 
	 * @param value the logoutMenu
	 */
	public void setLogoutMenu(final CMSNavigationNode value)
	{
		setLogoutMenu( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.promoTxt</code> attribute.
	 * @return the promoTxt
	 */
	public String getPromoTxt(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROMOTXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.promoTxt</code> attribute.
	 * @return the promoTxt
	 */
	public String getPromoTxt()
	{
		return getPromoTxt( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.promoTxt</code> attribute. 
	 * @param value the promoTxt
	 */
	public void setPromoTxt(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROMOTXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.promoTxt</code> attribute. 
	 * @param value the promoTxt
	 */
	public void setPromoTxt(final String value)
	{
		setPromoTxt( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.promoUrl</code> attribute.
	 * @return the promoUrl
	 */
	public String getPromoUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROMOURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.promoUrl</code> attribute.
	 * @return the promoUrl
	 */
	public String getPromoUrl()
	{
		return getPromoUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.promoUrl</code> attribute. 
	 * @param value the promoUrl
	 */
	public void setPromoUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROMOURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.promoUrl</code> attribute. 
	 * @param value the promoUrl
	 */
	public void setPromoUrl(final String value)
	{
		setPromoUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute.
	 * @return the showFindAStore
	 */
	public Boolean isShowFindAStore(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWFINDASTORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute.
	 * @return the showFindAStore
	 */
	public Boolean isShowFindAStore()
	{
		return isShowFindAStore( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute. 
	 * @return the showFindAStore
	 */
	public boolean isShowFindAStoreAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowFindAStore( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute. 
	 * @return the showFindAStore
	 */
	public boolean isShowFindAStoreAsPrimitive()
	{
		return isShowFindAStoreAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute. 
	 * @param value the showFindAStore
	 */
	public void setShowFindAStore(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWFINDASTORE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute. 
	 * @param value the showFindAStore
	 */
	public void setShowFindAStore(final Boolean value)
	{
		setShowFindAStore( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute. 
	 * @param value the showFindAStore
	 */
	public void setShowFindAStore(final SessionContext ctx, final boolean value)
	{
		setShowFindAStore( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showFindAStore</code> attribute. 
	 * @param value the showFindAStore
	 */
	public void setShowFindAStore(final boolean value)
	{
		setShowFindAStore( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute.
	 * @return the showLoginRegister
	 */
	public Boolean isShowLoginRegister(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWLOGINREGISTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute.
	 * @return the showLoginRegister
	 */
	public Boolean isShowLoginRegister()
	{
		return isShowLoginRegister( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute. 
	 * @return the showLoginRegister
	 */
	public boolean isShowLoginRegisterAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowLoginRegister( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute. 
	 * @return the showLoginRegister
	 */
	public boolean isShowLoginRegisterAsPrimitive()
	{
		return isShowLoginRegisterAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute. 
	 * @param value the showLoginRegister
	 */
	public void setShowLoginRegister(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWLOGINREGISTER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute. 
	 * @param value the showLoginRegister
	 */
	public void setShowLoginRegister(final Boolean value)
	{
		setShowLoginRegister( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute. 
	 * @param value the showLoginRegister
	 */
	public void setShowLoginRegister(final SessionContext ctx, final boolean value)
	{
		setShowLoginRegister( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showLoginRegister</code> attribute. 
	 * @param value the showLoginRegister
	 */
	public void setShowLoginRegister(final boolean value)
	{
		setShowLoginRegister( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showMyList</code> attribute.
	 * @return the showMyList
	 */
	public Boolean isShowMyList(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWMYLIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showMyList</code> attribute.
	 * @return the showMyList
	 */
	public Boolean isShowMyList()
	{
		return isShowMyList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showMyList</code> attribute. 
	 * @return the showMyList
	 */
	public boolean isShowMyListAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowMyList( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showMyList</code> attribute. 
	 * @return the showMyList
	 */
	public boolean isShowMyListAsPrimitive()
	{
		return isShowMyListAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showMyList</code> attribute. 
	 * @param value the showMyList
	 */
	public void setShowMyList(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWMYLIST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showMyList</code> attribute. 
	 * @param value the showMyList
	 */
	public void setShowMyList(final Boolean value)
	{
		setShowMyList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showMyList</code> attribute. 
	 * @param value the showMyList
	 */
	public void setShowMyList(final SessionContext ctx, final boolean value)
	{
		setShowMyList( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showMyList</code> attribute. 
	 * @param value the showMyList
	 */
	public void setShowMyList(final boolean value)
	{
		setShowMyList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute.
	 * @return the showQuickOrder
	 */
	public Boolean isShowQuickOrder(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWQUICKORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute.
	 * @return the showQuickOrder
	 */
	public Boolean isShowQuickOrder()
	{
		return isShowQuickOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute. 
	 * @return the showQuickOrder
	 */
	public boolean isShowQuickOrderAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowQuickOrder( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute. 
	 * @return the showQuickOrder
	 */
	public boolean isShowQuickOrderAsPrimitive()
	{
		return isShowQuickOrderAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute. 
	 * @param value the showQuickOrder
	 */
	public void setShowQuickOrder(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWQUICKORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute. 
	 * @param value the showQuickOrder
	 */
	public void setShowQuickOrder(final Boolean value)
	{
		setShowQuickOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute. 
	 * @param value the showQuickOrder
	 */
	public void setShowQuickOrder(final SessionContext ctx, final boolean value)
	{
		setShowQuickOrder( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPHeaderNavigationComponent.showQuickOrder</code> attribute. 
	 * @param value the showQuickOrder
	 */
	public void setShowQuickOrder(final boolean value)
	{
		setShowQuickOrder( getSession().getSessionContext(), value );
	}
	
}
