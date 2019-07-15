package com.gp.commerce.facade.catalog.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.gp.commerce.facade.catalog.impl.GpCatalogFacadeImpl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commercefacades.converter.PageablePopulator;
import de.hybris.platform.site.BaseSiteService;

@UnitTest
public class GpCatalogFacadeImplTest {

	
	@Mock
	private CategoryService categoryService;
	
	@Mock
	private PageablePopulator<CategoryModel, CategoryHierarchyData, CatalogOption> gpCategoryHierarchyPopulator;

	@Spy
	@InjectMocks
	GpCatalogFacadeImpl  gpCatalogFacadeImpl=new  GpCatalogFacadeImpl();
	
	@Mock
	BaseSiteService baseSiteService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		
	}
	@Test
	public void getCategoryByIdTest() {
		
		final CategoryHierarchyData data = new CategoryHierarchyData();
		data.setUrl("test/catallogversionId");
		String catalogId="test";
		String catalogVersionId="test";
		String categoryId="test";
		final CatalogVersionModel catalogVersionModel = new CatalogVersionModel();
		CategoryModel category=new CategoryModel();
		PageOption page=null;
		Set<CatalogOption> opts=new HashSet<CatalogOption>();
		
		Mockito.doReturn(category).when(categoryService).getCategoryForCode(catalogVersionModel, categoryId);
		Mockito.doNothing().when(gpCategoryHierarchyPopulator).populate(category,data,opts,page);
		gpCatalogFacadeImpl.getCategoryById(catalogId,catalogVersionId,catalogId,page,opts);
	}

}
