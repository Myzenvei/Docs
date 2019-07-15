package com.gp.commerce.facades.wishlist;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

/**
 * Builder class to be used in WishlistEntry test data preparation
 * @author megverma
 *
 */
public class WishlistEntryBuilder {
    
	Wishlist2EntryModel entry = new Wishlist2EntryModel();
	
   public WishlistEntryBuilder withProduct(ProductModel product) {
        this.entry.setProduct(product);
        return this;
    }
   
   public WishlistEntryBuilder withQuantity(Long quantity) {
       this.entry.setQuantity(quantity);
       return this;
   }
   
    public Wishlist2EntryModel build() {	
        return entry;
    }
   
}

