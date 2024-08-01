package com.Leenah.coloringbook;

import android.graphics.Bitmap;

/**
 * Created by Leenah on 20/05/2017.
 * leenah.apps@gmail.com
 */

public class ImageItem {
    private Bitmap image;

    public ImageItem(Bitmap image) {
        super();
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
