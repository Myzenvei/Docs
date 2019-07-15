package com.gp.commerce.core.dao.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.exceptions.GPCommerceDataException;
import com.gp.commerce.core.model.GPAttributeConfigModel;
import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.model.GPVariantColorCodesModel;
import com.gp.commerce.core.product.dao.impl.GPDefaultProductDao;

@UnitTest
public class GPDefaultProductDaoTest {

	private static final String PRODUCTCODE = "553637";
	private static final String B2BUNITCODE = "43214";
	private static final String UID = "vanityfair";
	private static final String ASSETCODE = "fixedLens";

	@InjectMocks
	private final GPDefaultProductDao dao = new GPDefaultProductDao("Product");

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dao.setFlexibleSearchService(flexibleSearchService);
	}

	@Test
	public void testGetCMIRCodeForProductAndB2BUnit() {

		final List<GPCustomerMaterialInfoModel> cmirCodes = new ArrayList<>();
		final GPCustomerMaterialInfoModel cmirModel = new GPCustomerMaterialInfoModel();
		cmirModel.setCmirCode("1234");
		cmirCodes.add(cmirModel);
		final SearchResult result=new SearchResultImpl(cmirCodes, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		final String cmirCode = dao.getCMIRCodeForProductAndB2BUnit(PRODUCTCODE, B2BUNITCODE);

		assertTrue("1234".equals(cmirCode));
	}

	@Test
	public void testGetCMIRCodeForProductAndB2BUnitCMIRNull()
	{

		final List<GPCustomerMaterialInfoModel> cmirCodes = new ArrayList<>();

		final SearchResult result = new SearchResultImpl(cmirCodes, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		final String cmirCode = dao.getCMIRCodeForProductAndB2BUnit(PRODUCTCODE, B2BUNITCODE);

		assertTrue(cmirCode == null);
	}

	@Test
	public void testGetCMIRCodeForProductAndB2BUnitException() {

		final List<GPCustomerMaterialInfoModel> cmirCodes = new ArrayList<>();
		final GPCustomerMaterialInfoModel cmirModel1 = new GPCustomerMaterialInfoModel();
		final GPCustomerMaterialInfoModel cmirModel2 = new GPCustomerMaterialInfoModel();
		cmirModel1.setCmirCode("1234");
		cmirModel2.setCmirCode("4321");
		cmirCodes.add(cmirModel1);
		cmirCodes.add(cmirModel2);
		final SearchResult result=new SearchResultImpl(cmirCodes, 1, 1, 1);
		boolean exception=false;

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		try {
			 dao.getCMIRCodeForProductAndB2BUnit(PRODUCTCODE, B2BUNITCODE);
		}catch(final GPCommerceDataException e) {
			exception=true;
		}
		assertTrue(exception);

	}

	@Test
	public void testGetAllColorCodes()
	{
		final GPVariantColorCodesModel colorCodeModel = new GPVariantColorCodesModel();
		colorCodeModel.setColorName("red");
		colorCodeModel.setColorCode("#12345");
		final List<GPVariantColorCodesModel> list = new ArrayList<>();
		list.add(colorCodeModel);
		final SearchResult result = new SearchResultImpl(list, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		final List<GPVariantColorCodesModel> resultList = dao.getAllColorCodes();

		assertTrue(resultList.get(0).getColorName().equals("red"));

	}

	@Test
	public void testGetAllColorCodesNull()
	{

		final List<GPVariantColorCodesModel> list = new ArrayList<>();
		final SearchResult result = new SearchResultImpl(list, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		final List<GPVariantColorCodesModel> resultList = dao.getAllColorCodes();

		assertTrue(resultList.size() == 0);

	}

	@Test
	public void testgetAttributeList() {
		final List<GPAttributeConfigModel> attributeConfigList=new ArrayList<>();
		final CMSSiteModel cmsSite = new CMSSiteModel();
		cmsSite.setUid("8796093056040");
		final GPAttributeConfigModel gpAttributeConfigModel= new GPAttributeConfigModel();
		gpAttributeConfigModel.setCmsSite(cmsSite);
		attributeConfigList.add(gpAttributeConfigModel);
		final SearchResult result=new SearchResultImpl(attributeConfigList, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		final List<GPAttributeConfigModel> gpAttributeConfigModelList = dao.getAttributeList(UID, ASSETCODE);

		assertTrue("8796093056040".equals(gpAttributeConfigModelList.get(0).getCmsSite().getUid()));
	}

	@Test
	public void testgetAttributeListNull()
	{
		final List<GPAttributeConfigModel> attributeConfigList = new ArrayList<>();

		final SearchResult result = new SearchResultImpl(attributeConfigList, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		final List<GPAttributeConfigModel> gpAttributeConfigModelList = dao.getAttributeList(UID, ASSETCODE);

		assertTrue(gpAttributeConfigModelList == null);
	}

	@Test
	public void testProductAssociatedWithSite()
	{
		final ProductModel product = new ProductModel();
		final CMSSiteModel site = new CMSSiteModel();
		final List sites = new ArrayList();
		sites.add(site);
		product.setCode(PRODUCTCODE);
		product.setSite(sites);
		site.setName("dixie", new Locale("en"));

		final SearchResult result = new SearchResultImpl(sites, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		assertTrue(dao.isProductAssociatedWithSite(product, site));

	}

	@Test
	public void testProductAssociatedWithoutSite()
	{
		final ProductModel product = new ProductModel();
		final CMSSiteModel site = new CMSSiteModel();
		final List sites = new ArrayList();
		product.setCode(PRODUCTCODE);

		final SearchResult result = new SearchResultImpl(sites, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		assertFalse(dao.isProductAssociatedWithSite(product, site));

	}

	@Test
	public void testGetProductForCodeOrUpc()
	{
		final List<ProductModel> products = new ArrayList();

		final ProductModel product = new ProductModel();

		product.setCode(PRODUCTCODE);
		product.setEan(PRODUCTCODE);

		products.add(product);

		final SearchResult result = new SearchResultImpl(products, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		assertTrue(dao.getProductForCodeOrUpc(PRODUCTCODE).size() == 1);

	}

	@Test
	public void testGetProductForCodeOrUpcNoResult()
	{
		final List<ProductModel> products = new ArrayList();

		final SearchResult result = new SearchResultImpl(products, 1, 1, 1);

		when(flexibleSearchService.search(Mockito.any(FlexibleSearchQuery.class))).thenReturn(result);

		assertTrue(dao.getProductForCodeOrUpc(PRODUCTCODE).size() == 0);

	}
}
