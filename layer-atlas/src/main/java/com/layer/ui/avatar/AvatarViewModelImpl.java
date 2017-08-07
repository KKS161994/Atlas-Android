package com.layer.ui.avatar;

import com.layer.sdk.messaging.Identity;
import com.layer.ui.identity.IdentityFormatter;
import com.layer.ui.util.imagecache.ImageCacheWrapper;

public class AvatarViewModelImpl implements AvatarViewModel  {

    private IdentityFormatter mIdentityFormatter;
    private ImageCacheWrapper mImageCacheWrapper;

    public AvatarViewModelImpl(ImageCacheWrapper imageCacheWrapper) {
        mImageCacheWrapper = imageCacheWrapper;
    }

    public void setIdentityFormatter(IdentityFormatter identityFormatter) {
        mIdentityFormatter = identityFormatter;
    }

    public String getInitialsForAvatarView(Identity identity) {
        return mIdentityFormatter.getInitials(identity);
    }

    @Override
    public ImageCacheWrapper getImageCacheWrapper() {
        return mImageCacheWrapper;
    }

}
