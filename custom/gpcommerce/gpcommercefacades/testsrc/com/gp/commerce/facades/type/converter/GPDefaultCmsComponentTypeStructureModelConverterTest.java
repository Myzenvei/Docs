package com.gp.commerce.facades.type.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.ObjectFactory;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.service.StringDecapitalizer;
import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.data.StructureTypeCategory;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeAttributeStructure;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructure;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class )
public class GPDefaultCmsComponentTypeStructureModelConverterTest {

	private static final String GPBRANDBARCOMPONENT = "GPBrandBarComponent";
	private static final String GPIMAGETILECOMPONENT = "GPImageTileComponent";
	private static final String GPBANNERCOMPONENT = "GPBannerComponent";
	private static final String GPMARKETINGCOMPONENT = "GpMarketingSideBySideComponent";
	private static final String GPHEROCOMPONENT = "GPRotatingImagesComponent";
	private static final String GPIMAGETEXTCOMPONENT = "GPImageTextComponent";
	private static final String GPUNSUBSCRIBECOMPONENT = "GPUnsubscribeComponent";
	private static final String FOOTERNAVCOMPONENT = "FooterNavigationComponent";
	private static final String GPIMAGELINKCOMPONENT = "GPImageLinkComponent";
	private static final String GPTABBEDIMAGETILECOMPONENT = "GPTabbedImageTileComponent";

	@Mock
	ComponentTypeStructure source;
	
	@Mock
	ComponentTypeData target;
	
	@Mock
	TypeService typeService;
	
	@Mock
	ComposedTypeModel composedTypeModel;
	
	@Mock
	ComponentTypeAttributeStructure attributeStructure;
	
	@Mock
	ComponentTypeAttributeData componentTypeAttributeData;
	
	@Mock
	StringDecapitalizer  stringDecapitalizer;
	
	@Mock
	ObjectFactory<ComponentTypeAttributeData> componentTypeAttributeDataFactory;
	
	@Mock
	Populator<ComposedTypeModel, ComponentTypeData> populator;
	
	@InjectMocks
	GPDefaultCmsComponentTypeStructureModelConverter cmsComponentTypeStructureModelConverter = new GPDefaultCmsComponentTypeStructureModelConverter();
	
	List<Populator<ComposedTypeModel, ComponentTypeData>> populators=new ArrayList<>();
	
	@Mock
	AttributeDescriptorModel attributeDescriptorModel;
	String balcklistAttributes="page,ctaStyle,isExternalLink";
	
	Set<ComponentTypeAttributeStructure> attributeStructures=new HashSet<>();
	
	
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		cmsComponentTypeStructureModelConverter.setTypeService(typeService);
		cmsComponentTypeStructureModelConverter.setComponentTypeAttributeDataFactory(componentTypeAttributeDataFactory);
		cmsComponentTypeStructureModelConverter.setStringDecapitalizer(stringDecapitalizer);
		
		Mockito.when(source.getCategory()).thenReturn(StructureTypeCategory.COMPONENT);
		Mockito.when(source.getPopulators()).thenReturn(populators);
		Mockito.when(source.getAttributes()).thenReturn(attributeStructures);
		Mockito.when(composedTypeModel.getDeclaredattributedescriptors()).thenReturn(Collections.singletonList(attributeDescriptorModel));
		Mockito.when(attributeDescriptorModel.getQualifier()).thenReturn("Qualifier");
		Mockito.when(attributeStructure.getQualifier()).thenReturn("Qualifier");
		Mockito.when(source.getTypeDataClass()).thenReturn(ComponentTypeStructure.class);
		Optional<String> optional=Optional.of(ComponentTypeStructure.class.getName());
		Mockito.when(stringDecapitalizer.decapitalize(ComponentTypeStructure.class)).thenReturn(optional);		
		Mockito.when(componentTypeAttributeDataFactory.getObject()).thenReturn(componentTypeAttributeData);
		populators.add(Mockito.mock(Populator.class));
		attributeStructures.add(attributeStructure);
		
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter("blacklist.brandbar")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.banner")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.imagetile")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.marketingSidebySide")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.rotatingImage")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.imagetext")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.unsubscribe")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.footer")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.tabbedimagetile")).thenReturn(balcklistAttributes);
		Mockito.when(Config.getParameter("blacklist.imagelink")).thenReturn(balcklistAttributes);
	}
	
	@Test
	public void testConvertGPBrandBarComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPBRANDBARCOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPBRANDBARCOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	@Test
	public void testConvertGPImageTileComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPIMAGETILECOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPIMAGETILECOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	@Test
	public void testConvertGPBannerComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPBANNERCOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPBANNERCOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	@Test
	public void testConvertGpMarketingSideBySideComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPMARKETINGCOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPMARKETINGCOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	@Test
	public void testConvertGPImageTextComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPIMAGETEXTCOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPIMAGETEXTCOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	@Test
	public void testGPUnsubscribeComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPUNSUBSCRIBECOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPUNSUBSCRIBECOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	@Test
	public void testConvertGPRotatingImagesComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPHEROCOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPHEROCOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	@Test
	public void testConvertGPTabbedImageTileComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPTABBEDIMAGETILECOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPTABBEDIMAGETILECOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	
	@Test
	public void testConvertGPImageLinkComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(GPIMAGELINKCOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(GPIMAGELINKCOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	
	@Test
	public void testConvertFooterNavigationComponent()
	{	
		Mockito.when(source.getTypecode()).thenReturn(FOOTERNAVCOMPONENT);
		Mockito.when(typeService.getComposedTypeForCode(FOOTERNAVCOMPONENT)).thenReturn(composedTypeModel);
		cmsComponentTypeStructureModelConverter.convert(source, target);
	}
	
}
