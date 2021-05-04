package com.example.watchit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;

public class DataBase
{
    private Connection con;
    private Statement st;

    public DataBase() throws InterruptedException {
        Thread db_con = new Thread(new DBConnection());
        db_con.start();
        db_con.join();
    }

    private User form_user(String nickname, boolean isFriend, Context context) throws InterruptedException {
        User[] user = {null};
        Thread user_form = new Thread(() -> {
            try
            {
                ResultSet rs = null;
                Title[] titles_unwatched = new Title[0];
                Title[] titles_watched = new Title[0];
                rs = st.executeQuery("SELECT t.title_url, t.title_watched FROM AppUser a " +
                        "JOIN Title t ON a.user_nickname = t.user_nickname " +
                        "WHERE a.user_nickname = '" + nickname + "'" );
                int i = 0, j = 0;
                while (rs.next())
                {
                    if (rs.getBoolean("title_watched"))
                    {
                        titles_watched = Arrays.copyOf(titles_watched, titles_watched.length + 1);
                        titles_watched[i] = new Title(Parser.getInformation(rs.getString("title_url")),
                                Parser.getBitmap(rs.getString("title_url"), context));
                        i++;
                    }
                    else
                    {
                        titles_unwatched = Arrays.copyOf(titles_unwatched, titles_unwatched.length + 1);
                        titles_unwatched[j] = new Title(Parser.getInformation(rs.getString("title_url")),
                                Parser.getBitmap(rs.getString("title_url"), context));
                        j++;
                    }
                }
                rs = st.executeQuery("SELECT user_avatar FROM AppUser " +
                        "WHERE user_nickname = '" + nickname + "'");
                rs.next();
                Bitmap avatar;
                if (rs.getBlob("user_avatar") != null)
                {
                    avatar = BitmapFactory.decodeStream(rs.getBlob("user_avatar").getBinaryStream());
                }
                else
                {
                    avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.user);
                }
                User[] friends = null;
                if (!isFriend)
                {
                    friends = new User[0];
                    /*rs = st.executeQuery("SELECT a.user_nickname FROM Relationship r " +
                            "JOIN AppUser a " +
                            "ON r.user_2 = a.user_nickname " +
                            "WHERE r.user_1 = '" + nickname + "'");*/
                    Statement st_friends = con.createStatement();
                    ResultSet rs_friends = st_friends.executeQuery("SELECT user_2 FROM Relationship r " +
                            "WHERE r.user_1 = '" + nickname + "'");
                    i = 0;
                    while (rs_friends.next())
                    {
                        friends = Arrays.copyOf(friends, friends.length + 1);
                        friends[i] = form_user(rs_friends.getString("user_2"), true, context);
                        i++;
                    }
                }
                user[0] = new User(nickname, avatar, friends, titles_unwatched, titles_watched);
            }
            catch (SQLException | InterruptedException throwables)
            {
                throwables.printStackTrace();
            }
        });
        user_form.start();
        user_form.join();

        return user[0];
    }

    public User login_user(String nickname, String password, Context context) throws InterruptedException {
        final User[] user = {null};
        Thread user_log = new Thread(() -> {
            try
            {
                ResultSet rs = null;
                rs = st.executeQuery("SELECT user_password FROM AppUser " +
                        "WHERE user_nickname = '" + nickname + "'");
                rs.next();
                if (rs.getString("user_password").equals(password))
                {
                    user[0] = form_user(nickname, false, context);
                }
            }
            catch (SQLException | InterruptedException throwables)
            {
                throwables.printStackTrace();
            }
        });
        user_log.start();
        user_log.join();

        return user[0];
    }

    private class DBConnection implements Runnable
    {
        @Override
        public void run()
        {
            final String MSSQL_DB = "jdbc:jtds:sqlserver://katshido.database.windows.net:1433;databaseName=MS_BD;" +
                    "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;Authentication=ActiveDirectoryIntegrated";
            final String MSSQL_LOGIN = "KatShiDo@katshido";
            final String MSSQL_PASS= "20030117ybrbnF";

            try
            {
                java.lang.Class.forName("net.sourceforge.jtds.jdbc.Driver");
                con = null;
                try
                {
                    con = DriverManager.getConnection(MSSQL_DB, MSSQL_LOGIN, MSSQL_PASS);
                    st = con.createStatement();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
}
