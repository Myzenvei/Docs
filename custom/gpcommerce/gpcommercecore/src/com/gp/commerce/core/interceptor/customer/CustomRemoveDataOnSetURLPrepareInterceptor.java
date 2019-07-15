/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.interceptor.customer;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;

/**
 * Created by sandubey on 6/19/18.
 */

public class CustomRemoveDataOnSetURLPrepareInterceptor implements PrepareInterceptor<MediaModel> {
    public CustomRemoveDataOnSetURLPrepareInterceptor() {
    }

    public void onPrepare(MediaModel media, InterceptorContext ctx) throws InterceptorException {
        if (media instanceof MediaModel) {
            if (!ctx.isNew(media) && ctx.isModified(media, "internalURL") && this.hadDataBeforeChangingURL(media, ctx)) {
                this.removePreviousData(media);
            }
        }

    }

    private boolean hadDataBeforeChangingURL(MediaModel media, InterceptorContext ctx) {
        if(ctx.isModified(media, "internalURL")) {
            String prev = (String)this.getPreviousValueForProperty(media, "internalURL");
            return "replicated273654712".equals(prev);
        } else {
            return "replicated273654712".equals(media.getInternalURL());
        }
    }

    private void removePreviousData(MediaModel media) {
        PK mediaPK = media.getPk();
        Long dataPK = (Long)this.getPreviousValueForProperty(media, "dataPK");
        MediaFolderModel folder = (MediaFolderModel)this.getPreviousValueForProperty(media, "folder");
        String location = (String)this.getPreviousValueForProperty(media, "location");
        MediaManager mediaManager = MediaManager.getInstance();
        mediaManager.deleteMediaDataUnlessReferenced(mediaPK, dataPK, folder.getQualifier(), location);
    }

    private <T> T getPreviousValueForProperty(MediaModel media, String propertyName) {
        ItemModelContext iCtx = ModelContextUtils.getItemModelContext(media);
        return iCtx.getOriginalValue(propertyName);
    }
}
