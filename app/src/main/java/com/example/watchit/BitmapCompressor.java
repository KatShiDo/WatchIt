package com.example.watchit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapCompressor {
    public static Bitmap compressBitmap(Bitmap bitmap, int recWidth){
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if (bitmap != null)
        {
            int imageHeight = bitmap.getHeight();
            int imageWidth = bitmap.getWidth();
            float k = (float) imageWidth/ (float) recWidth;
            float newHeightFloat = ((float) imageHeight/k);
            int newHeight = (int) newHeightFloat;
            return Bitmap.createScaledBitmap(bitmap, recWidth, newHeight, false);
        }
        else
        {
            return bitmap;
        }
    }
}
