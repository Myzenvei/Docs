package com.gp.commerce.facades.wishlist;

import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.Date;
import java.util.List;

/**
 * Builder class to be used in Wishlist test data preparation
 * @author megverma
 *
 */
public class WishlistBuilder {
    
	private Wishlist2Model wishlist = new Wishlist2Model();
	
	public WishlistBuilder withEntries(List<Wishlist2EntryModel> wishlistEntries) {
        this.wishlist.setEntries(wishlistEntries);
        return this;
    }

    public WishlistBuilder withName(String name) {
    	this.wishlist.setName(name);
        return this;
    }
    
    public WishlistBuilder withUid(String uid) {
    	this.wishlist.setWishlistUid(uid);
        return this;
    }
    
    public WishlistBuilder withModifiedtime(Date modTime) {
    	this.wishlist.setModifiedtime(modTime);
        return this;
    }
    
    public Wishlist2Model build() {	
        return wishlist;
    }
}
