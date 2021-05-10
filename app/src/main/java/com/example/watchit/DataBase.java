package com.example.watchit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    public User form_user(String nickname, boolean isFriend, Context context) throws InterruptedException, SQLException {
        st = con.createStatement();
        User[] user = {null};
        Thread user_form = new Thread(() -> {
            try
            {
                ResultSet rs;
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
                rs = st.executeQuery("SELECT user_avatar_index FROM AppUser " +
                        "WHERE user_nickname = '" + nickname + "'");
                rs.next();
                Bitmap avatar;
                switch (rs.getInt("user_avatar_index"))
                {
                    case 0:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.user);
                        break;
                    case 1:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av1);
                        break;
                    case 2:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av2);
                        break;
                    case 3:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av3);
                        break;
                    case 4:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av4);
                        break;
                    case 5:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av5);
                        break;
                    case 6:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av6);
                        break;
                    case 7:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av7);
                        break;
                    case 8:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av8);
                        break;
                    case 9:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av9);
                        break;
                    case 10:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av10);
                        break;
                    case 11:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av11);
                        break;
                    case 12:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av12);
                        break;
                    case 13:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av13);
                        break;
                    case 14:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av14);
                        break;
                    case 15:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av15);
                        break;
                    case 16:
                        avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.av16);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + rs.getInt("user_avatar_index"));
                }
                User[] friends = null;
                if (!isFriend)
                {
                    friends = new User[0];
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

        st.close();

        return user[0];
    }

    public User login_user(String nickname, String password, Context context) throws InterruptedException, SQLException {
        st = con.createStatement();
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

        st.close();

        return user[0];
    }

    private class DBConnection implements Runnable
    {
        @Override
        public void run()
        {
            final String MSSQL_DB = "jdbc:jtds:sqlserver://katshido.database.windows.net:1433;databaseName=MS_BD;" +
                    "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;Authentication=ActiveDirectoryIntegrated;testOnBorrow=true;validationQuery=\"select 1\"";
            final String MSSQL_LOGIN = "KatShiDo@katshido";
            final String MSSQL_PASS = "fv7sHEJ9hcs";

            try
            {
                java.lang.Class.forName("net.sourceforge.jtds.jdbc.Driver");
                con = null;
                try
                {
                    con = DriverManager.getConnection(MSSQL_DB, MSSQL_LOGIN, MSSQL_PASS);
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

    public void add_friend(String nickname, User user) throws SQLException {
        st = con.createStatement();
        new Thread(() -> {
            try {
                st.executeUpdate("INSERT INTO Relationship (user_1, user_2) " +
                        "VALUES ('" + user.getNickname() + "', '" + nickname + "')");
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }).start();
    }

    public void add_title(String url, boolean isWatched, User user) throws SQLException {
        st = con.createStatement();
        new Thread(() -> {
            try {
                st.executeUpdate("INSERT INTO Title (title_url, title_watched, user_nickname) " +
                        "VALUES ('" + url + "', '" + isWatched + "', '" + user.getNickname() + "')");
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }).start();
    }

    public boolean add_user(String nickname, String password) throws InterruptedException, SQLException {
        st = con.createStatement();
        boolean[] flag = {true};
        Thread thread_add_user = new Thread(() -> {
            try {
                ResultSet rs = st.executeQuery("SELECT * FROM AppUser " +
                        "WHERE user_nickname = '" + nickname + "'");
                if (rs.next())
                {
                    flag[0] = false;
                }
                else
                {
                    st.executeUpdate("INSERT INTO AppUser (user_nickname, user_password) " +
                            "VALUES ('" + nickname + "', '" + password + "')");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        thread_add_user.start();
        thread_add_user.join();

        st.close();

        return flag[0];
    }

    public void set_user_avatar(String nickname, int avatar_index) throws SQLException
    {
        st = con.createStatement();
        new Thread(() -> {
            try {
                st.executeUpdate("UPDATE AppUser SET user_avatar_index = '" + avatar_index + "' " +
                        "WHERE user_nickname = '" + nickname + "'");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }).start();
    }
}
