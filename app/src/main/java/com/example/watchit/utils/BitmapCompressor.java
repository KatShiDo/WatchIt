package com.example.watchit.utils;

import android.graphics.Bitmap;

public class BitmapCompressor {
    public static Bitmap compressBitmap(Bitmap bitmap, int recWidth){
        if (bitmap != null) {
            int imageHeight = bitmap.getHeight();
            int imageWidth = bitmap.getWidth();
            float k = (float) imageWidth/ (float) recWidth;
            float newHeightFloat = ((float) imageHeight/k);
            int newHeight = (int) newHeightFloat;
            return Bitmap.createScaledBitmap(bitmap, recWidth, newHeight, false);
        }
        else {
            return null;
        }
    }
}
