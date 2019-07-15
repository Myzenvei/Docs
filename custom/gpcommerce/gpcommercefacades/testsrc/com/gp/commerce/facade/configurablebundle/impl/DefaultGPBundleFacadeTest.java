package com.gp.commerce.facade.configurablebundle.impl;

import static org.mockito.Mockito.when;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.services.configurablebundle.GPBundleService;

import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class DefaultGPBundleFacadeTest {

	@Mock
	private BundleTemplateService bundleTemplateService;
	
	@Mock
    private GPBundleService gpBundleService;
	
	@Mock
    private Converter<BundleTemplateModel, BundleTemplateData> gpBundleConverter;
	
	@InjectMocks
	private DefaultGPBundleFacade gpBundleFacade = new DefaultGPBundleFacade();
	
	private static final String BUNDLE_ID = "BUNDLE_ID";

	private static final String ROOT_BUNDLE_ID = "TABLE PACKAGE BUNDLE";
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testBuildBundleTreeForInvalidSetup() {
		when(gpBundleService.validateBundleSetup(BUNDLE_ID)).thenReturn(false);
		BundleTemplateData bundleTemplateTree = gpBundleFacade.buildBundleTree(BUNDLE_ID);
		assertNull(bundleTemplateTree);
	}
	
	@Test
	public void testBuildBundleTreeForValidSetup() {
		BundleTemplateModel rootBundleTemplate = mock(BundleTemplateModel.class);
		BundleTemplateData bundleTemplateTree = mock(BundleTemplateData.class);
		when(bundleTemplateTree.getId()).thenReturn(ROOT_BUNDLE_ID);
		
		when(gpBundleService.validateBundleSetup(BUNDLE_ID)).thenReturn(true);
		when(gpBundleService.getRootBundleTemplate(BUNDLE_ID)).thenReturn(rootBundleTemplate);
		when(gpBundleConverter.convert(rootBundleTemplate)).thenReturn(bundleTemplateTree );
		
		BundleTemplateData result = gpBundleFacade.buildBundleTree(BUNDLE_ID);
		assertNotNull(result);
		assertEquals(ROOT_BUNDLE_ID,result.getId());
	}
	
	
}
