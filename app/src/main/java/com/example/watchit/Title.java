package com.example.watchit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

public class Title implements Parcelable
{
    private Bitmap image;
    private String caption, description, year, genre, producer, url;

    public Title(HashMap<String, String> data, Bitmap image)
    {
        this.image = image;
        this.url = data.get("Ссылка");
        this.caption = data.get("Название");
        this.description = data.get("Описание");
        this.year = data.get("Год");
        this.genre = data.get("Жанр");
        this.producer = data.get("Режиссёр");
    }

    protected Title(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
        caption = in.readString();
        description = in.readString();
        year = in.readString();
        genre = in.readString();
        producer = in.readString();
        url = in.readString();
    }

    public static final Creator<Title> CREATOR = new Creator<Title>() {
        @Override
        public Title createFromParcel(Parcel in) {
            return new Title(in);
        }

        @Override
        public Title[] newArray(int size) {
            return new Title[size];
        }
    };

    public String getCaption()
    {
        return this.caption;
    }

    public Bitmap getImage()
    {
        return this.image;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getYear()
    {
        return this.description;
    }

    public String getGenre()
    {
        return this.genre;
    }

    public String getProducer()
    {
        return this.producer;
    }

    public String getUrl()
    {
        return this.url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bitmap image_compressed = BitmapCompressor.compressBitmap(image, 150);
        /*int width = image.getWidth();
        int height = image.getHeight();
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        image = Bitmap.createScaledBitmap(image, halfWidth, halfHeight, false);*/
        dest.writeParcelable(image_compressed, flags);
        dest.writeString(caption);
        dest.writeString(description);
        dest.writeString(year);
        dest.writeString(genre);
        dest.writeString(producer);
        dest.writeString(url);
    }
}
