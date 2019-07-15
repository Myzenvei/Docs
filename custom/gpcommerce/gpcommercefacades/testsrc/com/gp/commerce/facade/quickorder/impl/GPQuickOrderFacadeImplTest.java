package com.gp.commerce.facade.quickorder.impl;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.core.services.quickorder.GPQuickOrderService;
import com.gp.commerce.facade.quickorder.impl.GPQuickOrderFacadeImpl;
import com.gp.commerce.facades.data.GPQuickOrderData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;

@UnitTest
public class GPQuickOrderFacadeImplTest {

	@Mock
	private GPQuickOrderService gpQuickOrderService;
	@Mock
	private GPProductService gpProductService;
	@Mock
	private GPQuickOrderData quickOrderData;
	@Mock
	private List<GPCustomerMaterialInfoModel> materialInfos;
	MultipartFile file;
	List<ProductData> productList = new ArrayList<ProductData>();
	ProductData product = new ProductData();
	@InjectMocks
	GPQuickOrderFacadeImpl quickOrderfacade = new GPQuickOrderFacadeImpl();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		quickOrderfacade.setGpQuickOrderService(gpQuickOrderService);
		quickOrderfacade.setGpProductService(gpProductService);
		product.setCode("p123");
		ProductData product1 = new ProductData();
		product1.setCode("p456");
		productList.add(product1);
		productList.add(product);
	}

	@Test
	public void testGetProductsForQuickOrder() {

		List<GPCustomerMaterialInfoModel> materialInfos1 = new ArrayList<>();
		GPCustomerMaterialInfoModel gpCustomerMaterialInfoModel1 = new GPCustomerMaterialInfoModel();
		gpCustomerMaterialInfoModel1.setCmirCode("p456");
		gpCustomerMaterialInfoModel1.setProductCode("p456");
		materialInfos1.add(gpCustomerMaterialInfoModel1);
		Mockito.when(gpQuickOrderService.getMaterialInfoForB2BUnit("b2bunit", "p456,p123")).thenReturn(materialInfos1);
		quickOrderfacade.getProductsForQuickOrder("b2bunit", productList);
		assertNotNull(quickOrderfacade.getProductsForQuickOrder("b2bunit", productList));
	}

	@Test
	public void testgetProductListFromCSV() throws Exception {
		final MultipartFile file1 = Mockito.mock(MultipartFile.class);
		//final InputStream stream = Mockito.mock(InputStream.class);

		final BufferedReader br = Mockito.mock(BufferedReader.class);
		InputStream stubInputStream = IOUtils.toInputStream("some test data for my input stream", "UTF-8");
		final InputStreamReader in = Mockito.mock(InputStreamReader.class);

		Mockito.when(file1.getInputStream()).thenReturn(stubInputStream);

		// Mockito.when(new BufferedReader(in)).thenReturn(br);
		// Mockito.when(br.readLine()).thenReturn("This is for testing the
		// product list, from all csv \n","line2","/n");

		quickOrderfacade.getProductListFromCSV(file1);
		assertNotNull(quickOrderfacade.getProductListFromCSV(file1));
	}

}
