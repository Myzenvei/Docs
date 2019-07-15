/**
 *
 */
package de.hybris.platform.multicountry.productcockpit.services.security.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.multicountry.MockScopeMap;
import de.hybris.platform.multicountry.productcockpit.services.ProductLockingService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.GenericVariantProductModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.ContextConfiguration;

import com.google.common.collect.Lists;


/**
 * @author daniel.norberg
 *
 */
@ContextConfiguration("classpath:/multicountry-spring-test.xml")
@IntegrationTest
public class ProductCockpitUIAccessRightServiceTest extends ServicelayerTransactionalTest
{
    protected final boolean lockForm = Boolean.parseBoolean(Config.getString("productcockpit.lockform", "false"));
    
    protected final static Logger LOG = Logger.getLogger(ProductCockpitUIAccessRightService.class.getName());
    private static final String VERSION = "TestVersion";
    
    @Resource
    private UserService userService;
    @Resource
    private SessionService sessionService;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private ModelService modelService;
    @Mock
    private SystemService systemService;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    @Resource
    private ProductService productService;
    
    private TypeService cockpitTypeService;
    
    private ProductLockingService productLockingService;
    
    private ProductCockpitUIAccessRightService productCockpitUiAccessRightService;
    
    private ProductModel baseProduct;
    
    private ProductModel variant1;
    private ProductModel variant2;
    
    @Mock
    private TypedObject typedObject;
    @Mock
    private ObjectType objectType;
    
    private ApplicationContext applicationContext;
    
    private EmployeeModel user;
    
    private CatalogVersionModel catalogVersion;
    
    private void initApplicationContext()
    {
        
        // init spring context with predefined load of xml configurations
        final GenericApplicationContext context = new GenericApplicationContext();
        context.setResourceLoader(new DefaultResourceLoader(Registry.class.getClassLoader()));
        context.setClassLoader(Registry.class.getClassLoader());
        context.getBeanFactory().setBeanClassLoader(Registry.class.getClassLoader());
        if (context.getBeanFactory().getRegisteredScope("tenant") == null)
        {
            context.getBeanFactory().registerScope("tenant", new MockScopeMap());
        }
        
        context.setParent(Registry.getCoreApplicationContext());
        final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
        xmlReader.setBeanClassLoader(Registry.class.getClassLoader());
        xmlReader.loadBeanDefinitions(getSpringConfigurationLocations());
        context.refresh();
        final AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
        this.applicationContext = context;
        
    }
    
    private String[] getSpringConfigurationLocations()
    {
        return new String[]
        { "classpath:/multicountry-spring.xml", "classpath:/cockpit/cockpit-spring-wrappers.xml", //
            "classpath:/cockpit/cockpit-spring-services.xml", //
            "classpath:/productcockpit/productcockpit-spring-services.xml", //
            "classpath:/cockpit/cockpit-web-spring.xml", //
            "classpath:/productcockpit/productcockpit-web-spring.xml", //
            "classpath:/multicountrycockpit/productcockpit/spring/productcockpit-web.xml" };
    }
    
    
    @Before
    public void setUp() throws ImpExException
    {
        initApplicationContext();
        MockitoAnnotations.initMocks(this);
        // Perform import and setup as admin user
        userService.setCurrentUser(userService.getAdminUser());
        
        importCsv("/multicountry/test/testSearchRestriction.impex", "UTF-8");
        importCsv("/multicountry/test/testImpersonate.impex", "UTF-8");
        
        user = userService.getUserForUID("productmanager-test", EmployeeModel.class);
        
        assertThat(user.getPk(), is(notNullValue()));
        
        // get catalog version
        catalogVersion = flexibleSearchService
        .<CatalogVersionModel> search("SELECT {pk} FROM {CatalogVersion} WHERE {version} = ?version",
                                      Collections.singletonMap("version", VERSION))
        .getResult().iterator().next();
        
        // Get base product
        baseProduct = getProductForCode("TestProduct");
        
        // Get variant products
        variant1 = getProductForCode("TestProduct_1");
        variant2 = getProductForCode("TestProduct_2");
        
        // Active session [always last!]
        catalogVersionService.setSessionCatalogVersions(Lists.newArrayList(catalogVersion));
        
        final CustomerModel customer = userService.getAnonymousUser();
        
        LOG.info("user.getName() -> " + customer.getName());
        
        userService.setCurrentUser(customer);
        
        // fetching beans from the custom spring context
        cockpitTypeService = (TypeService) applicationContext.getBean("cockpitTypeService");
        
        productLockingService = (ProductLockingService) applicationContext.getBean("productLockingService");
        
        productCockpitUiAccessRightService = new ProductCockpitUIAccessRightService()
        {
            
            @Override
            public boolean isWritable(final ObjectType type)
            {
                return true;
            }
            
            @Override
            public boolean isWritable(final ObjectType type, final TypedObject item)
            {
                return isObjectWritable(item);
            }
            
        };
        
        // preparing the access service
        productCockpitUiAccessRightService.setProductLockingService(productLockingService);
        productCockpitUiAccessRightService.setSystemService(systemService);
        productCockpitUiAccessRightService.setModelService(modelService);
        productCockpitUiAccessRightService.setCockpitTypeService(cockpitTypeService);
        productCockpitUiAccessRightService.setUserService(userService);
    }
    
    private void prepareVariantObjectAndType(final ProductModel aVariant)
    {
        
        final Set<ObjectType> types = new HashSet<ObjectType>();
        types.add(cockpitTypeService.getObjectType(ProductModel._TYPECODE));
        
        given(objectType.getCode()).willReturn(GenericVariantProductModel._TYPECODE);
        given(objectType.getSupertypes()).willReturn(types);
        given(typedObject.getObject()).willReturn(aVariant);
        given(typedObject.getType()).willReturn(cockpitTypeService.getBaseType(GenericVariantProductModel._TYPECODE));
    }
    
    private void prepareBaseObjectAndType(final ProductModel aBaseProduct)
    {
        
        final Set<ObjectType> types = new HashSet<ObjectType>();
        
        given(objectType.getCode()).willReturn(ProductModel._TYPECODE);
        given(objectType.getSupertypes()).willReturn(types);
        given(typedObject.getObject()).willReturn(aBaseProduct);
        given(typedObject.getType()).willReturn(cockpitTypeService.getBaseType(ProductModel._TYPECODE));
    }
    
    @Test
    public void shouldLockVariantIfLockedByOtherUser()
    {
        // product locked by single user
        variant1.setLockedBy(Collections.singletonList(user));
        
        modelService.save(variant1);
        
        prepareVariantObjectAndType(variant1);
        
        // check if product is looked for anonymous user
        final boolean writeable = productCockpitUiAccessRightService.isWritable(objectType, typedObject);
        
        checkWriteable(writeable, false);
        
    }
    
    private void checkWriteable(final boolean writeable, final boolean match)
    {
        Assert.assertThat(Boolean.valueOf(writeable), is(Boolean.valueOf(match)));
    }
    
    @Test
    public void shouldAllowWriteIfVariantLockedBySameUser()
    {
        // change current user
        userService.setCurrentUser(user);
        
        // product locked by single user
        variant1.setLockedBy(Collections.singletonList(user));
        
        modelService.save(variant1);
        
        prepareVariantObjectAndType(variant1);
        
        final boolean writeable = productCockpitUiAccessRightService.isWritable(objectType, typedObject);
        
        checkWriteable(writeable, true);
    }
    
    @Test
    public void shouldAllowWriteOfVariantIfBaseProductIsLockedBySameUser()
    {
        // change current user
        userService.setCurrentUser(user);
        
        // product locked by single user
        baseProduct.setLockedBy(Collections.singletonList(user));
        
        modelService.save(variant1);
        
        prepareVariantObjectAndType(variant1);
        
        final boolean writeable = productCockpitUiAccessRightService.isWritable(objectType, typedObject);
        
        checkWriteable(writeable, true);
    }
    
    @Test
    public void shouldAllowWriteOfVariantIfBaseProductIsLockedByOtherUser()
    {
        // product locked by single user
        baseProduct.setLockedBy(Collections.singletonList(user));
        
        modelService.save(baseProduct);
        
        prepareVariantObjectAndType(variant1);
        
        final boolean writeable = productCockpitUiAccessRightService.isWritable(objectType, typedObject);
        
        checkWriteable(writeable, true);
    }
    
    @Test
    public void shouldAllowWriteOfVariantIfNotLocked()
    {
        // change current user
        userService.setCurrentUser(user);
        
        // unlock product
        variant1.setLockedBy(null);
        modelService.save(variant1);
        
        prepareVariantObjectAndType(variant1);
        
        final boolean writeable = productCockpitUiAccessRightService.isWritable(objectType, typedObject);
        
        checkWriteable(writeable, true);
    }
    
    @Test
    public void shouldAllowWriteOfVariantIfOtherVariantIsLockedByOtherUser()
    {
        // variant product locked by single user
        variant2.setLockedBy(Collections.singletonList(user));
        
        modelService.save(variant2);
        
        prepareVariantObjectAndType(variant1);
        
        final boolean writeable = productCockpitUiAccessRightService.isWritable(objectType, typedObject);
        
        checkWriteable(writeable, true);
    }
    
    @Test
    public void shouldAllowWriteOfProductIfVariantIsLockedByOtherUser()
    {
        // variant product locked by single user
        variant2.setLockedBy(Collections.singletonList(user));
        
        modelService.save(variant2);
        
        prepareBaseObjectAndType(baseProduct);
        
        final boolean writeable = productCockpitUiAccessRightService.isWritable(objectType, typedObject);
        
        checkWriteable(writeable, true);
    }
    
    private ProductModel getProductForCode(final String code)
    {
        return productService.getProductForCode(code);
    }
    
}