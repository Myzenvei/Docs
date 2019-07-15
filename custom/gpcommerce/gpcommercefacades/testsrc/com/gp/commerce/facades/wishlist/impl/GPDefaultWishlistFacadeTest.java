package com.gp.commerce.facades.wishlist.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.model.WishlistProcessModel;
import com.gp.commerce.core.services.event.ShareWishlistEvent;
import com.gp.commerce.core.wishlist.services.GPWishlistService;
import com.gp.commerce.dto.wishlist.WishlistEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistWsDTO;
import com.gp.commerce.facades.data.WishlistData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import org.junit.Assert;

import com.gp.commerce.facades.wishlist.impl.GPDefaultWishlistFacade;
import com.gp.commerce.facades.wishlist.WishlistEntryBuilder;
import com.gp.commerce.facades.product.ProductBuilder;
import com.gp.commerce.facades.wishlist.WishlistBuilder;

import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

/**
 * @author psaraswat
 */
@UnitTest
public class GPDefaultWishlistFacadeTest {

	private static final String PRODUCT_CODE = "12456";
	private static final String WISHLIST_GUID = "WISHLIST_GUID";
	private static final String CUSTOMER_WISHLIST = "Customer Wishlist";
	private static final String NAME = "wishlist";
	@InjectMocks
	GPDefaultWishlistFacade wishlist = new GPDefaultWishlistFacade();

	@Mock
	private CommerceCartService commerceCartService;
	@Mock
	private Wishlist2Service wishlistService;
	@Mock
	private KeyGenerator guidKeyGenerator;
	@Mock
	private Converter<Wishlist2Model, WishlistData> wishlistConverter;
	@Mock
	private ProductService productService;
	@Mock
	private ModelService modelService;
	@Mock
	private WishlistWsDTO wishlistdto;
	@Mock
	private WishlistEntryRequestDTO dto;
	@Mock
	private GPWishlistService gpWishlistService;
	@Mock
	private WishlistData wishlistData;
	@Mock
	private WishlistProcessModel wishlistProcessModel ;
	
	@Mock
	private UserService userService;
	@Mock
	private UserModel userModel;
	
	@Mock
	private EventService eventService;
	
	@Mock
	private BusinessProcessService businessProcessService;

	Wishlist2Model wishlistModel=mock(Wishlist2Model.class);
	ProductModel product=mock(ProductModel.class);
	WishlistData data=new WishlistData();
	List<Wishlist2Model> wishlists=new ArrayList<>();
	List<WishlistData> wishListDataList = new ArrayList<>();
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		wishlists.add(wishlistModel);

		when(wishlistService.createWishlist(NAME, CUSTOMER_WISHLIST)).thenReturn(wishlistModel);
		when(guidKeyGenerator.generate()).thenReturn(WISHLIST_GUID);
		when(wishlistConverter.convert(wishlistModel)).thenReturn(data);
		when(wishlistService.getWishlists()).thenReturn(wishlists);
		when(wishlistModel.getWishlistUid()).thenReturn(NAME);

		doNothing().when(modelService).save(wishlistModel);
	}

	private void prepareWishlistModel() {

		if(CollectionUtils.isEmpty(wishlists)) {
			Wishlist2EntryModel wish1Entry1 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P1")
							.build()).build();

			Wishlist2EntryModel wish1Entry2 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P2")
							.build()).build();

			Wishlist2EntryModel wish1Entry3 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P3")
							.build()).build();

			List<Wishlist2EntryModel> wish1EntryList1 = new ArrayList<>();

			wish1EntryList1.add(wish1Entry1);
			wish1EntryList1.add(wish1Entry2);
			wish1EntryList1.add(wish1Entry3);

			Wishlist2Model wishlistModel1 = new WishlistBuilder()
					.withName("BirthdayWish")
					.withUid("W1")
					.withEntries(wish1EntryList1)
					.build();

			Wishlist2EntryModel wish2Entry1 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P4")
							.build()).build();

			Wishlist2EntryModel wish2Entry2 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P5")
							.build()).build();

			List<Wishlist2EntryModel> wish2EntryList2 = new ArrayList<>();

			wish2EntryList2.add(wish2Entry1);
			wish2EntryList2.add(wish2Entry2);

			Wishlist2Model wishlistModel2 = new WishlistBuilder()
					.withName("FestivalWish")
					.withUid("W2")
					.withEntries(wish2EntryList2)
					.build();

			Wishlist2EntryModel wish3Entry1 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P6")
							.build()).build();

			Wishlist2EntryModel wish3Entry2 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P7")
							.build()).build();

			Wishlist2EntryModel wish3Entry3 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P8")
							.build()).build();

			Wishlist2EntryModel wish3Entry4 = new WishlistEntryBuilder()
					.withProduct(new ProductBuilder().withCode("P9")
							.build()).build();

			List<Wishlist2EntryModel> wish2EntryList3 = new ArrayList<>();

			wish2EntryList3.add(wish3Entry1);
			wish2EntryList3.add(wish3Entry2);
			wish2EntryList3.add(wish3Entry3);
			wish2EntryList3.add(wish3Entry4);

			Wishlist2Model wishlistModel3 = new WishlistBuilder()
					.withName("CasualWish")
					.withUid("W3")
					.withEntries(wish2EntryList3)
					.build();

			this.wishlists.add(wishlistModel1);
			this.wishlists.add(wishlistModel2);
			this.wishlists.add(wishlistModel3);

		}

	}




	@Test
	public void testCreateWishlist() {
		assertNotNull(wishlist.createWishlist(NAME, CUSTOMER_WISHLIST));
	}

	@Test
	public void testGetWishlist() {
		assertNotNull(wishlist.getWishlist(NAME,null,null,false));
	}

	@Test
	public void testGetWishlistEmpty() {
		when(wishlistService.getWishlists()).thenReturn(null);

		assertNotNull(wishlist.getWishlist(NAME,null,null,false));
	}

	@Test
	public void testGetAllWishlist() {
		assertNotNull(wishlist.getAllWishlist());
	}

	@Test
	public void testRemoveWishlistEntryForProduct() {
		when(productService.getProductForCode(PRODUCT_CODE)).thenReturn(product);
		doNothing().when(wishlistService).removeWishlistEntryForProduct(product, wishlistModel);

		assertNotNull(wishlist.removeWishlistEntryForProduct(PRODUCT_CODE, NAME));
	}

	@Test
	public void testRemoveWishlist() {
		assertNotNull(wishlist.removeWishlist(NAME));
	}

	@Test
	public void testSortWishlistsByName() {

		prepareWishlistModel();
		when(wishlistService.getWishlists()).thenReturn(wishlists);
		wishlist.sortWishlistsByParam("name", "descending");
		assertNotNull(wishlist.sortWishlistsByParam("name", "descending"));
	}

	@Test
	public void testSortWishlistsByNoOfProducts() {

		prepareWishlistModel();

		when(wishlistService.getWishlists()).thenReturn(wishlists);

		wishlist.sortWishlistsByParam("noOfProducts", "descending");
	}

	@Test
	public void testSortWishlistsBySubtotal() {

		prepareWishlistModel();

		when(wishlistService.getWishlists()).thenReturn(wishlists);

		wishlist.sortWishlistsByParam("subtotal", "descending");
	}

	@Test
	public void testSortWishlistsBySubtotalAndAscendingOrder() {

		prepareWishlistModel();

		when(wishlistService.getWishlists()).thenReturn(wishlists);

		wishlist.sortWishlistsByParam("subtotal", "ascending");
	}

	@Test
	public void testSortWishlistsByModTime() {

		prepareWishlistModel();
		when(wishlistService.getWishlists()).thenReturn(wishlists);
		wishlist.sortWishlistsByParam("modifiedTime", "ascending");
		assertNotNull(wishlist.sortWishlistsByParam("modifiedTime", "ascending"));	
	}

	@Test
	public void testRemoveSelectedWishlists() {
		List<String> multiplewishlistuuid= new ArrayList<>();
		multiplewishlistuuid.add("testID1");
		wishlist.removeSelectedWishlists(multiplewishlistuuid);
	}
	
	@Test
	public void testUpdateWishListEntryForQuantity() {
		when(gpWishlistService.updateWishListEntryForQuantity(dto)).thenReturn(wishlistData);
		assertNotNull(wishlist.updateWishListEntryForQuantity(dto));
		assertNull(wishlistData.getName());
	}

	@Test
	public void testCreatePrecuratedList() {
		when(wishlistdto.getWishlistUid()).thenReturn("value");
		when(wishlistService.getWishlists()).thenReturn(wishlists);
		when(userService.getCurrentUser()).thenReturn(userModel);
		wishlist.savePrecuratedList(wishlistdto);
		assertNull(wishlistdto.getName());
	}
	@Test
	public void testupdatePrecuratedList() {
		when(wishlistdto.getWishlistUid()).thenReturn("value");
		when(wishlistService.getWishlists()).thenReturn(wishlists);
		when(userService.getCurrentUser()).thenReturn(userModel);
		when(gpWishlistService.getWishlistByType(userModel, "PRE_CURATED_LIST")).thenReturn(wishlistModel);
		wishlist.savePrecuratedList(wishlistdto);
		assertNull(wishlistdto.getName());
	}
	
	@Test
	public void testUpdateWishlistName() {
		wishlist.updateWishlistName("wishlistuuid","listName");
		assertNull(wishlistData.getName());
	}
	
	@Test
	public void testGetPrecuratedlist() {
		when(gpWishlistService.getPrecuratedlist("listName")).thenReturn(wishlistData);
		assertNotNull(wishlist.getPrecuratedlist("listName"));
	}
	@Test
	public void testShareWishList() {
		when(wishlistData.getWishlistUid()).thenReturn("wish1");
		when(wishlistModel.getWishlistUid()) .thenReturn("wish1");

		when(wishlistService.getWishlists()).thenReturn(wishlists);
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), 
				Mockito.anyString(), Mockito.anyMap())).thenReturn(wishlistProcessModel);
		doNothing().when(eventService).publishEvent(Mockito.anyObject());
		wishlists.add(wishlistModel);
		when(gpWishlistService.getWishlistById("wish1")).thenReturn(wishlistModel);
		wishlist.shareWishlist("toEmail", wishlistData);
		verify(eventService, times(2)).publishEvent(Mockito.any(ShareWishlistEvent.class));
	}
	
	@Test
	public void testCreateSharedlistForUser() {
		ShareProductForm form = Mockito.mock(ShareProductForm.class);
		List<String> recipientEmails = new ArrayList<>();
		recipientEmails.add("test1@test.com");
		recipientEmails.add("test2@test.com");
		form.setRecipientEmails(recipientEmails);
		B2BCustomerModel customer = new B2BCustomerModel();
		customer.setOriginalUid("testUID");
		customer.setEmail("test1@test.com");
		when(gpWishlistService.getWishlistById(NAME)).thenReturn(wishlistModel);
		when(gpWishlistService.getB2BCustomerForEmail("test1@test.com")).thenReturn(customer);
		Assert.assertEquals(NAME, wishlistModel.getWishlistUid());
		when(userService.getUserForUID(recipientEmails.get(0))).thenReturn(userModel);
		when(userService.getCurrentUser()).thenReturn(userModel);
		when(gpWishlistService.createWishListForLoggedInUser(wishlistModel,userModel)).thenReturn(wishlistData);
		
		//method call-1
		wishlist.createSharedlistForUser(NAME, form);
		
		//method call-2
		wishlist.createSharedlistForUser(NAME, null);
	}
}
