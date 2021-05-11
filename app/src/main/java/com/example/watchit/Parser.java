package com.example.watchit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    private static String[] url_initted_list = new String[0];
    private static Document[] document_initted_list = new Document[0];

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

    private static void getWeb(String url) throws IOException
    {
        boolean flag = false;
        for (int i = 0; i < url_initted_list.length; i++)
        {
            if (url.equals(url_initted_list[i]))
            {
                flag = true;
                doc = document_initted_list[i];
                break;
            }
        }
        if (!flag)
        {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 RuxitSynthetic/1.0 v9966304240 t38550 ath9b965f92 altpub cvcv=2")
                    .referrer("http://www.google.com")
                    .get();
            url_initted_list = Arrays.copyOf(url_initted_list, url_initted_list.length + 1);
            url_initted_list[url_initted_list.length - 1] = url;

            document_initted_list = Arrays.copyOf(document_initted_list, document_initted_list.length + 1);
            document_initted_list[document_initted_list.length - 1] = doc;
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

    public static String get_domain(String url)
    {
        String[] url_splitted = url.split("/");
        String domain;
        domain = url_splitted[2];

        String[] domain_splitted = domain.split("\\.");
        if (!domain_splitted[0].equals("www"))
        {
            return domain_splitted[0];
        }
        else
        {
            return domain_splitted[1];
        }
    }

    public static HashMap<String, String> getInformation(String url) throws InterruptedException {
        init(url);
        switch (get_domain(url))
        {
            case "kinopoisk":
                return getInformationKinopoisk(url);
            case "okko":
                return getInformationOkko(url);
            case "netflix":
                return getInformationNetflix(url);
            case "animego":
                return getInformationAnimego(url);
            default:
                return null;
        }
    }



    public static Bitmap getBitmap(String url, Context context) throws InterruptedException {
        init(url);
        switch (get_domain(url))
        {
            case "kinopoisk":
                return getBitmapKinopoisk(context);
            case "okko":
                return getBitmapOkko(context);
            case "netflix":
                return getBitmapNetflix(context);
            case "animego":
                return getBitmapAnimego(context);
            default:
                return null;
        }
    }

    public static HashMap<String, String> getInformationKinopoisk(String url) {
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
                title_genre_string.append(genre.text()).append(" ");
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

    public static Bitmap getBitmapKinopoisk(Context context) {
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

    public static HashMap<String, String> getInformationOkko(String url) {
        HashMap<String, String> data = new HashMap<>();
        try
        {
            Elements info1 = doc.getElementsByTag("div");
            Elements info2 = info1.get(0).children();
            Elements info3 = info2.get(1).children();
            Elements info4 = info3.get(0).children();
            Elements info5 = info4.get(1).children();
            Elements info6 = info5.get(0).children();

            Element title_caption = info6.get(0).children().get(1).children().get(1);
            Elements title_information = info6.get(1).children().get(3).children();
            Elements info_description1 = info4.get(2).children();
            Elements info_description2 = info_description1.get(0).children();
            Elements info_description3 = info_description2.get(0).children();
            Elements info_description4 = info_description3.get(0).children();
            Elements info_description5 = info_description4.get(2).children();
            Elements info_description6 = info_description5.get(0).children();
            Elements info_description7 = info_description6.get(0).children();
            Element title_description = info_description7.get(0);

            Element title_year = title_information.get(0).children().get(1).children().get(0);
            Element title_producer = title_information.get(2).children().get(1).children().get(0).children().get(0);
            Elements title_genre = title_information.get(1).children().get(1).children();
            StringBuilder title_genre_string = new StringBuilder();
            for (Element genre : title_genre)
            {
                String genre_str = genre.children().get(0).text();
                title_genre_string.append(genre_str).append(" ");
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

    public static Bitmap getBitmapOkko(Context context) {
        Element title_image = doc.selectFirst("picture[class=_1pIJl]").children().get(2);
        try
        {
            String text = title_image.toString();
            int begin = text.indexOf("1x") + 6;
            int end = text.indexOf("2x") - 1;
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

    public static HashMap<String, String> getInformationNetflix(String url) {
        HashMap<String, String> data = new HashMap<>();
        try
        {
            Elements info1 = doc.getElementsByTag("div");
            Elements info2 = info1.get(0).children();
            Elements info3 = info2.get(0).children();
            Elements info4 = info3.get(1).children();
            Elements info5 = info4.get(0).children();
            Elements info6 = info5.get(0).children();
            Elements info7 = info6.get(1).children();
            Elements info8 = info7.get(0).children();
            Element title_caption = info8.get(0);
            Elements info9 = info8.get(2).children();
            Element title_description = info9.get(0);
            Element title_year = info8.get(1).children().get(0);

            Element title_producer = info9.get(1).children().get(1).children().get(1);

            Elements info10 = info3.get(4).children();
            Elements info11 = info10.get(1).children();
            Elements info12 = info11.get(1).children();
            Elements title_genre = info12.get(1).children();

            StringBuilder title_genre_string = new StringBuilder();
            for (Element genre : title_genre)
            {
                title_genre_string.append(genre.text()).append(" ");
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

    public static Bitmap getBitmapNetflix(Context context) {
        Element title_image = doc.selectFirst("div[class=hero-image hero-image-desktop]");
        try
        {
            String text = title_image.toString();
            int begin = text.indexOf("url") + 5;
            int end = text.indexOf(")") - 1;
            char[] url_string = new char[200];
            text.getChars(begin, end, url_string, 0);

            url_string = Arrays.copyOf(url_string, url_string.length + 8);

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

    public static HashMap<String, String> getInformationAnimego(String url) {
        HashMap<String, String> data = new HashMap<>();
        try
        {
            Elements info1 = doc.getElementsByTag("div");
            Elements info2 = info1.get(0).children();
            Elements info3 = info2.get(0).children();

            Elements info4 = info3.get(1).children();
            Elements info5 = info4.get(0).children();
            Elements info6 = info5.get(0).children();


            Elements info7 = info6.get(0).children();
            Elements info8 = info6.get(1).children();
            Elements info9 = info8.get(1).children();
            Elements info10 = info9.get(1).children();
            Elements info11 = info10.get(1).children();
            Element title_producer = info11.get(2).children().get(1);
            Element title_caption = info7.get(0);
            Element title_year = info8.get(1).children().get(0);
            Elements title_genre = info7.get(3).children().get(0).children();

            Elements info12 = info5.get(1).children();
            Element title_description = info12.get(0);

            StringBuilder title_genre_string = new StringBuilder();
            for (Element genre : title_genre)
            {
                title_genre_string.append(genre.text()).append(" ");
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

    public static Bitmap getBitmapAnimego(Context context) {
        Element title_image = doc.selectFirst("div[class=page__poster img-fit-cover]").children().get(0);
        try
        {
            String text = title_image.toString();
            int begin = text.indexOf("src") + 5;
            int end = text.indexOf("alt") - 2;
            char[] url_string = new char[200];
            text.getChars(begin, end, url_string, 0);

            url_string = Arrays.copyOf(url_string, url_string.length + 8);

            for (int i = end - begin + 21; i >= 22; i--)
            {
                url_string[i] = url_string[i - 22];
            }

            url_string[0] = 'h'; url_string[1] = 't'; url_string[2] = 't'; url_string[3] = 'p'; url_string[4] = 's'; url_string[5] = ':';
            url_string[6] = '/'; url_string[7] = '/'; // https://

            url_string[8] = 'a'; url_string[9] = 'n'; url_string[10] = 'i'; url_string[11] = 'm'; url_string[12] = 'e'; url_string[13] = 'g';
            url_string[14] = 'o'; url_string[15] = '.'; url_string[16] = 'o'; url_string[17] = 'n'; url_string[18] = 'l'; url_string[19] = 'i';
            url_string[20] = 'n'; url_string[21] = 'e'; //animego.online



            String str = new String(url_string);
            str = str.substring(0, 109);

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
