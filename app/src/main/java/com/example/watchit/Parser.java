package com.example.watchit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

public class Parser
{
    private static Thread secThread;
    private static Runnable runnable;
    private static Document doc;
    private static String url_initted;

    private static void getWeb(String url) throws IOException
    {
        if (!url.equals(url_initted))
        {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 RuxitSynthetic/1.0 v9966304240 t38550 ath9b965f92 altpub cvcv=2")
                    .referrer("http://www.google.com")
                    .get();
            url_initted = url;
        }
    }

    private static void init(String url) throws InterruptedException {
        runnable = () -> {
            try {
                getWeb(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
        secThread.join();
    }

    public static HashMap<String, String> getInformation(String url) throws InterruptedException {
        init(url);
        HashMap<String, String> data = new HashMap<>();
        try
        {
            Elements info1 = doc.getElementsByTag("div");
            Elements info2 = info1.get(1).children();
            Elements info3 = info2.get(1).children();
            Elements info_title = info3.get(0).children();
            Elements info_description1 = info3.get(1).children();
            Elements info_description2 = info_description1.get(0).children();
            Elements info_description3 = info_description2.get(0).children();
            Elements info_description4 = info_description3.get(0).children();
            Elements info_description5 = info_description4.get(0).children();
            Elements info_description6 = info_description5.get(1).children();
            Elements info_description7 = info_description6.get(0).children();
            Elements info_description8 = info_description7.get(1).children();
            Element title_description = info_description8.get(0).children().get(0);

            Element title_caption = info_title.get(1).children().get(0).children().get(2).children().get(0).children().get(0).children().get(0).children().
                    get(0).children().get(1).children().get(0).children().get(0);

            Elements title_information_table = info_title.get(1).children().get(0).children().get(2).children().get(0).children().get(0).children().
                    get(1).children().get(0).children().get(1).children();
            Element title_year = title_information_table.get(0).children().get(1).children().get(0);
            Element title_producer = title_information_table.get(4).children().get(1).children().get(0);
            Elements title_genre = title_information_table.get(2).children().get(1).children().get(0).children();
            StringBuilder title_genre_string = new StringBuilder();
            for (Element genre : title_genre)
            {
                title_genre_string.append(genre.text());
            }
            data.put("Ссылка", url);
            data.put("Название", title_caption.text());
            data.put("Описание", title_description.text());
            data.put("Год", title_year.text());
            data.put("Режиссёр", title_producer.text());
            data.put("Жанр", title_genre_string.toString());
        }
        catch (Exception e)
        {
            data.put("Ссылка", "Error: couldn't read data from url");
            data.put("Название", "Error: couldn't read data from url");
            data.put("Описание", "Error: couldn't read data from url");
            data.put("Год", "Error: couldn't read data from url");
            data.put("Режиссёр", "Error: couldn't read data from url");
            data.put("Жанр", "Error: couldn't read data from url");
        }
        return data;
    }

    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmap(String url, Context context) throws InterruptedException {
        init(url);
        Element title_image = doc.selectFirst("img[class=film-poster styles_root__2Q5Ds styles_rootInDark__3mPn2 image styles_root__eMUmk styles_rootLoaded__SyGwc]");
        try
        {
            String text = title_image.toString();
            int begin = text.indexOf("src") + 7;
            int end = text.indexOf("srcset") - 2;
            char[] url_string = new char[200];
            text.getChars(begin, end, url_string, 0);

            url_string = Arrays.copyOf(url_string, url_string.length + 8);

            for (int i = end - begin + 7; i >= 8; i--)
            {
                url_string[i] = url_string[i - 8];
            }

            url_string[0] = 'h'; url_string[1] = 't'; url_string[2] = 't'; url_string[3] = 'p'; url_string[4] = 's'; url_string[5] = ':';
            url_string[6] = '/'; url_string[7] = '/'; // https://

            String str = new String(url_string);
            str = str.substring(0, 103);

            final Bitmap[] bitmap = new Bitmap[1];
            String finalStr = str;
            Thread thread = new Thread() {
                public void run()
                {
                    bitmap[0] = getBitmapFromURL(finalStr);
                }
            };
            thread.start();
            thread.join();
            return bitmap[0];
        }
        catch (Exception e)
        {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_not_found);
        }
    }
}
