package com.gp.commerce.core.wishlist.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.wishlist.dao.GPWishlistDao;
import com.gp.commerce.dto.wishlist.WishlistEntryRequestDTO;
import com.gp.commerce.dto.wishlist.WishlistWsDTO;
import com.gp.commerce.facades.data.GPQuickOrderData;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

@UnitTest
public class GPDefaultWishlistServiceTest {

	@InjectMocks
	GPDefaultWishlistService wishlistService = new GPDefaultWishlistService();
	
	@Mock
	private WishlistData wishlistData;
	@Mock
	private Wishlist2Service wishlist2Service;
	@Mock
	private GPWishlistDao gpWishlistDao;
	@Mock
	private Converter<Wishlist2Model, WishlistData> wishlistConverter;
	@Mock
	private ModelService modelService;
	@Mock
	private WishlistEntryRequestDTO dto;
	@Mock
	private ProductWsDTO productWsDto;
	@Mock
	private ProductService productService;
	@Mock
	private ProductModel productModel;
	@Mock
	private Wishlist2EntryModel entryModel;
	@Mock
	private WishlistWsDTO wishlistdto;
	@Mock
	private KeyGenerator guidKeyGenerator;
	@Mock
	private UserModel userModel;
	@Mock
	private Wishlist2EntryModel wishlist2EntryModel;
	@Mock
	private Wishlist2Model wishlist2Model;
	@Mock
	private BaseSiteService baseSiteService;
	
	Wishlist2Model wishlistModel=mock(Wishlist2Model.class);
	WishlistEntryRequestDTO reqdto = mock(WishlistEntryRequestDTO.class);
	List<Wishlist2Model> wishlists=new ArrayList<>();
	WishlistData data=new WishlistData();
	List<WishlistEntryRequestDTO> wishlistEntries = new ArrayList<>();
	List<Wishlist2EntryModel> value = new ArrayList<>();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
			}
	
	@Test
	public void testRemoveSelectedWishlists1() {
		this.wishlists.add(wishlistModel);
		when(wishlistModel.getWishlistUid()).thenReturn("testID1");
		when(wishlist2Service.getWishlists()).thenReturn(wishlists);
		List<String> multiplewishlistuuid= new ArrayList<>();
		multiplewishlistuuid.add("testID1");
		wishlistService.removeSelectedWishlists(multiplewishlistuuid);
	}
	
	@Test
	public void testRemoveSelectedWishlists2() {
		/*String uuid = "uuid";
		when(wishlistService.removeWishlist(uuid)).thenReturn(wishlistData);
		List<String> multiplewishlistuuid= new ArrayList<>();
		multiplewishlistuuid.add("testID1");*/
		this.wishlists.add(wishlistModel);
		when(wishlistModel.getWishlistUid()).thenReturn("testID1");
		when(wishlist2Service.getWishlists()).thenReturn(wishlists);
		List<String> failedUidList = new ArrayList<>();
		failedUidList.add("FAILURE");
		when(wishlistData.getStatus()).thenReturn("FAILURE");
		List<String> multiplewishlistuuid= new ArrayList<>();
		multiplewishlistuuid.add("testID1");
		wishlistService.removeSelectedWishlists(multiplewishlistuuid);
	}
	
	@Test
	public void testGetPrecuratedlist() {
		String listName = "listName";
		when(wishlistConverter.convert(wishlistModel)).thenReturn(data);
		when(gpWishlistDao.getPrecuratedlist(listName)).thenReturn(wishlistModel);
		wishlistService.getPrecuratedlist(listName);
	}
	
	@Test
	public void testGetPrecuratedlistElseCondition() {
		String listName = null;
		wishlistService.getPrecuratedlist(listName);
	}
	
	@Test
	public void testUpdateWishlistName() {
		String listName ="listName";
		wishlistModel.setName(listName);
		doNothing().when(modelService).save(wishlistModel);
		wishlistService.updateWishlistName(wishlistModel,listName);
	}
	
	@Test
	public void testUpdateWishListEntryForQuantity() {
		this.wishlists.add(wishlistModel);
		when(dto.getWishlistUid()).thenReturn("uuid");
		when(dto.getProduct()).thenReturn(productWsDto);
		when(productWsDto.getCode()).thenReturn("code");
		when(wishlistModel.getWishlistUid()).thenReturn("uuid");
		String code ="code";
		when(productService.getProductForCode(code)).thenReturn(productModel);
		when(wishlist2Service.getWishlists()).thenReturn(wishlists);
		when(wishlist2Service.getWishlistEntryForProduct(productModel,wishlistModel)).thenReturn(entryModel);
		wishlistService.updateWishListEntryForQuantity(dto);
	}
	
	@Test
	public void testGetWishlistByName() {
		
		UserModel user = new UserModel();
		String name = "wishlist1";
		when(gpWishlistDao.getWishlistByName(user, name)).thenReturn(wishlistModel);
		wishlistService.getWishlistByName(user, name);
		
	}
	
	@Test
	public void testGetWishlistByType() {
		
		UserModel user = new UserModel();
		String name = "wishlist2";
		when(gpWishlistDao.getWishlistByName(user, name)).thenReturn(wishlistModel);
		
		//method call-1
		wishlistService.getWishlistByType(user, name);
		
		String name2 = null;
		when(gpWishlistDao.getWishlistByName(user, name)).thenReturn(null);
		//method call-2
		wishlistService.getWishlistByType(user, name2);
		
	}
	
	
	@Test
	public void testGetWishlistById() {
		
		String wishListId = "wishlist2";
		when(gpWishlistDao.getWishlistById(wishListId)).thenReturn(wishlistModel);
		
		//method call-1
		wishlistService.getWishlistById(wishListId);
		
		String wishListId2 = null;
		when(gpWishlistDao.getWishlistById(wishListId2)).thenReturn(null);
		
		//method call-2
		wishlistService.getWishlistById(wishListId2);
		
	}
	
	
	@Test
	public void testCalculateSubtotal() {
		
		wishlistService.calculateSubtotal(getWishListEntriesData());
	}
	
	
	@Test
	public void testUpdatePrecuratedList() {
		
		when(modelService.create(Wishlist2EntryModel.class)).thenReturn(new Wishlist2EntryModel());
		
		Wishlist2Model savedPrecurated = new Wishlist2Model();		
		savedPrecurated.setEntries(Collections.emptyList());
				
		// method call-1
		wishlistService.updatePrecuratedList(getWishListWsDTO(), savedPrecurated, getGpUserPrecuratedList());
				
		// method call-2
		wishlistService.updatePrecuratedList(getWishListWsDTO(), getSavedPrecuratedListWithSecondId(), getGpUserPrecuratedList());
		
		// method call-3
		wishlistService.updatePrecuratedList(getWishListWsDTO(), getSavedPrecuratedList(), getGpUserPrecuratedList());
	}
	
	
	@Test
	public void testCreateQuickOrderWishList() throws BusinessException {
		
		Wishlist2Model wishListModel = new Wishlist2Model(); 
		when(wishlist2Service.createWishlist(Mockito.anyString(),Mockito.anyString())).thenReturn(wishListModel);
		when(modelService.create(Wishlist2EntryModel.class)).thenReturn(new Wishlist2EntryModel());
		when(productService.getProductForCode(Mockito.anyString())).thenReturn(new ProductModel());
		when(guidKeyGenerator.generate()).thenReturn("acb2540f123iefe3e570fe");
		
		Assert.assertEquals(wishListModel,wishlistService.createQuickOrderWishList(getGPQuickOrderData()));
		
	}
	
	@Test
	public void testCreatePrecuratedList() {
		
		Wishlist2Model wishListModel = new Wishlist2Model(); 
		when(wishlist2Service.createWishlist(Mockito.anyString(),Mockito.anyString())).thenReturn(wishListModel);
		when(modelService.create(Wishlist2EntryModel.class)).thenReturn(new Wishlist2EntryModel());
		when(guidKeyGenerator.generate()).thenReturn("acb2540f123iefe3e570fe");
		
		wishlistService.createPrecuratedList(getWishListWsDTO(), getGpUserPrecuratedList());
	}
	
	@Test
	public void testGetB2BCustomerForEmail() throws UnknownIdentifierException{
		String email="test@test.com";
		B2BCustomerModel customer = new B2BCustomerModel();
		customer.setOriginalUid("testUID");
		customer.setEmail(email);
		List<B2BCustomerModel> b2bCustomers = new ArrayList<>();
		b2bCustomers.add(customer);
		when(gpWishlistDao.getB2BCustomerForEmail(email)).thenReturn(b2bCustomers);
		
		wishlistService.getB2BCustomerForEmail(email);
	}
	
	@Test
	public void testCreateWishListForLoggedInUser() {
		UserModel user = new UserModel();
		Wishlist2Model wishlist = getGpUserPrecuratedList();
		when(guidKeyGenerator.generate()).thenReturn("acb2540f123iefe3e570fe");
		doNothing().when(modelService).save(wishlist);
		when(modelService.create(Wishlist2EntryModel.class)).thenReturn(new Wishlist2EntryModel());
		when(wishlistConverter.convert(wishlist)).thenReturn(data);
		
		wishlistService.createWishListForLoggedInUser(wishlist, user);
	}
	
	private Wishlist2Model getGpUserPrecuratedList() {
		
		Wishlist2Model gpUserPrecurated =  new Wishlist2Model();
		List<Wishlist2EntryModel> Wishlist2EntryModelList = new ArrayList<>();
		Wishlist2EntryModel wishList2EntryModel = new Wishlist2EntryModel();
		ProductModel productModel = new ProductModel();
		productModel.setCode("3000412833");
		wishList2EntryModel.setProduct(productModel);
		wishList2EntryModel.setQuantity(Long.valueOf(1));
		Wishlist2EntryModelList.add(wishList2EntryModel);
		gpUserPrecurated.setEntries(Wishlist2EntryModelList);
		return gpUserPrecurated;
	}
	
	
	private WishlistWsDTO getWishListWsDTO() {
		
		WishlistWsDTO wishlistdto = new WishlistWsDTO();
		List<WishlistEntryRequestDTO> wishListEntryRequestList = new ArrayList<>();
		WishlistEntryRequestDTO wishListEntryRequest = new WishlistEntryRequestDTO();
		ProductWsDTO productWsDTO = new ProductWsDTO();
		productWsDTO.setCode("3000412833");
		wishListEntryRequest.setProduct(productWsDTO);
		wishListEntryRequest.setQuantity(1L);
		wishListEntryRequestList.add(wishListEntryRequest);
		wishlistdto.setWishlistEntries(wishListEntryRequestList);
		return wishlistdto;
	}
	
	
	private Wishlist2Model getSavedPrecuratedList() {
		
		Wishlist2Model savedPrecurated = new Wishlist2Model();
		List<Wishlist2EntryModel> savedPrecuratedEntriesList2 = new ArrayList<>();
		Wishlist2EntryModel wishListEntryModel2 = new Wishlist2EntryModel();
		ProductModel productModel2 = new ProductModel();
		productModel2.setCode("300122812");
		wishListEntryModel2.setProduct(productModel2);
		savedPrecuratedEntriesList2.add(wishListEntryModel2);
		savedPrecurated.setEntries(savedPrecuratedEntriesList2);
		return savedPrecurated;
	}
	
	private Wishlist2Model getSavedPrecuratedListWithSecondId() {
		
		Wishlist2Model savedPrecurated = getSavedPrecuratedList();
		savedPrecurated.getEntries().get(0).getProduct().setCode("3000412833");
		return savedPrecurated; 
	}
	
	private List<WishlistEntryData> getWishListEntriesData() {
		
		List<WishlistEntryData> wishlistEntryDataList = new ArrayList<>();
		WishlistEntryData wishListEntry = new WishlistEntryData();
		WishlistEntryData wishListEntry2 = new WishlistEntryData();
		ProductData productData = new ProductData();
		PriceData priceData = new PriceData();
		priceData.setValue(null);
		productData.setPrice(priceData); 
		wishListEntry.setProduct(productData);
		wishListEntry.setQuantity(2L);
		wishlistEntryDataList.add(wishListEntry);
		
		ProductData productData2 = new ProductData();
		PriceData priceData2 = new PriceData();
		BigDecimal price2 = new BigDecimal(30);
		priceData2.setValue(price2);
		productData2.setPrice(priceData2); 
		wishListEntry2.setProduct(productData2);
		wishListEntry2.setQuantity(4L);
		wishlistEntryDataList.add(wishListEntry2);
		return wishlistEntryDataList;
		
	}
	
	private GPQuickOrderData getGPQuickOrderData() {
		
		GPQuickOrderData gpQuickOrderData = new GPQuickOrderData();
		List<ProductData> ProductDataList = new ArrayList<>();
		ProductData productData = new ProductData();
		productData.setCode("3000412383");
		productData.setCount(2);
		ProductDataList.add(productData);
		gpQuickOrderData.setItems(ProductDataList);
		return gpQuickOrderData;
	}
	
	@Test
	public void testUpdateWishlist() {
		wishlist2EntryModel.setComment("Comment");
		wishlistService.updateWishlist(wishlist2EntryModel);
	}
	
}
