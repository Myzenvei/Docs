/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.interceptor.customer;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;


/**
 * @author dgandabonu
 */
@UnitTest
public class CustomRemoveDataOnSetURLPrepareInterceptorTest {
	
	private CustomRemoveDataOnSetURLPrepareInterceptor interceptor;
	
	@Mock
	private InterceptorContext mockInterceptorContext;
	
	@Mock
	private MediaModel mediaModel;
	
	@Mock
	private ItemModelContext itemModelContext;

	@Before
	public void setUp() throws Exception
	{
		
		Registry.activateMasterTenant();
		final String internalURL = "internalURL";
		interceptor = new CustomRemoveDataOnSetURLPrepareInterceptor();
		MockitoAnnotations.initMocks(this);
		mediaModel.setDataPK(123456L);
		
		MediaFolderModel folder = new MediaFolderModel();
		folder.setQualifier("qualify");
		
		when(mockInterceptorContext.isNew(mediaModel)).thenReturn(false);
		when(Boolean.valueOf(mockInterceptorContext.isModified(mediaModel,internalURL))).thenReturn(true);
		when(itemModelContext.getOriginalValue("internalURL")).thenReturn("replicated273654712");
		when(itemModelContext.getOriginalValue("dataPK")).thenReturn(12345L);
		when(itemModelContext.getOriginalValue("folder")).thenReturn(folder);
		when(itemModelContext.getOriginalValue("location")).thenReturn("location");
		when(ModelContextUtils.getItemModelContext(mediaModel)).thenReturn(itemModelContext);
		when(mediaModel.getPk()).thenReturn(PK.fromLong(12356798l));
	}
	
	@Test
	public void testOnPrepareRemoveData() throws InterceptorException{
		
		interceptor.onPrepare(mediaModel, mockInterceptorContext);
	}
	
}
