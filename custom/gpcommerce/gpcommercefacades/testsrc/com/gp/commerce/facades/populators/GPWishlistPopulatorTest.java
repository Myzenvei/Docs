package com.gp.commerce.facades.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

/**
 * @author psaraswat
 */
@UnitTest
public class GPWishlistPopulatorTest {

	private static final String WISHLIST = "wishlist";

	@InjectMocks
	private GPWishlistPopulator populator=new GPWishlistPopulator();
	
	@Mock
	private Converter<Wishlist2EntryModel, WishlistEntryData> wishlistEntryConverter;

	Wishlist2Model source=mock(Wishlist2Model.class);
	WishlistData target=new WishlistData();
	Wishlist2EntryModel entry = mock(Wishlist2EntryModel.class);
	WishlistEntryData entryData=new WishlistEntryData();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		List<Wishlist2EntryModel> entries =new ArrayList<>();
		entries.add(entry);
		List<WishlistEntryData> entriesData =new ArrayList<>();
		entriesData.add(entryData);
		
		when(source.getDescription()).thenReturn(WISHLIST);
		when(source.getName()).thenReturn(WISHLIST);
		when(source.getWishlistUid()).thenReturn(WISHLIST);
		when(source.getEntries()).thenReturn(entries);
		when(wishlistEntryConverter.convertAll(entries)).thenReturn(entriesData);
	}

	@Test
	public void testPopulate() {
		populator.populate(source, target);
		
		assertNotNull(target);
		assertEquals(WISHLIST,target.getName());
		assertEquals(WISHLIST,target.getDescription());
		assertEquals(WISHLIST,target.getWishlistUid());
		assertNotNull(target.getWishlistEntries());
	}

}
