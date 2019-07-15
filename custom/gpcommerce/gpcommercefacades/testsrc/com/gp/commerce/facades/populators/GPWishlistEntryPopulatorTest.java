package com.gp.commerce.facades.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.facades.data.WishlistEntryData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
/**
 * @author psaraswat
 */
@UnitTest
public class GPWishlistEntryPopulatorTest {

	private static final String WISH_LIST_UID = "WISH-LIST_UID";

	@InjectMocks
	private GPWishlistEntryPopulator populator=new GPWishlistEntryPopulator();
	
	@Mock
	private Converter<ProductModel, ProductData> productConverter;
	
	Wishlist2EntryModel source = mock(Wishlist2EntryModel.class);
	WishlistEntryData target=new WishlistEntryData();
	ProductModel product=mock(ProductModel.class);
	ProductData data=new ProductData();
	Wishlist2Model wishlist=mock(Wishlist2Model.class);
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		
		when(productConverter.convert(product)).thenReturn(data);
		when(source.getProduct()).thenReturn(product);
		when(source.getWishlist()).thenReturn(wishlist);
		when(wishlist.getWishlistUid()).thenReturn(WISH_LIST_UID);
		when(wishlist.getName()).thenReturn(WISH_LIST_UID);
	}

	@Test
	public void testPopulate() {
		
		populator.populate(source, target);
		
		assertNotNull(target.getWishlistUid());
		assertNotNull(target.getWishlistName());
		assertEquals(WISH_LIST_UID, target.getWishlistUid());
		assertEquals(WISH_LIST_UID, target.getWishlistName());
		assertNotNull(target.getProduct());
	}
	
	

}
