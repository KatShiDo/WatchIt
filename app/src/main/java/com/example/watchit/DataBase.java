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
    private PreparedStatement st_prep;

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
                rs = st.executeQuery("SELECT user_avatar FROM AppUser " +
                        "WHERE user_nickname = '" + nickname + "'");
                rs.next();
                Bitmap avatar;
                if (rs.getBytes("user_avatar") != null)
                {
                    //Blob blob = rs.getBlob("user_avatar");
                    //byte[] bArray = blob.getBytes(1L, (int)blob.length());
                    byte[] bArray = rs.getBytes("user_avatar");
                    avatar = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
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
            //final String MSSQL_PASS = "20030117ybrbnF";
            //final String MSSQL_PASS = "j6QuwLjG8LDy";

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

    public void set_user_avatar(String nickname, Bitmap avatar) throws SQLException
    {
        st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        Thread set_avatar = new Thread(() -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            try {
                /*
                PreparedStatement statement = con.prepareStatement("UPDATE AppUser SET user_avatar = ? " +
                        "WHERE user_nickname = '" + nickname + "'");
                //OutputStream outputStream = blob.setBinaryStream(1);
                //outputStream.write(bArray);
                statement.setBinaryStream(1, new ByteArrayInputStream(bArray));//, bArray.length);//, bArray.length);
                //statement.setBlob(1, blob);
                statement.executeUpdate();
                statement.close();
                //st = con.createStatement();*/
                ResultSet rs = st.executeQuery("SELECT * FROM AppUser");
                rs.next();
                while (!rs.getString("user_nickname").equals(nickname))
                {
                    rs.next();
                }
                rs.updateBytes("user_avatar", bArray);
                rs.updateRow();
            } catch (SQLException throwables) {
                /*if (throwables.getCause() instanceof SocketException)
                {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Thread thread1 = new Thread(() -> {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    thread1.start();
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread thread = new Thread(new DBConnection());
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        set_user_avatar(nickname, avatar);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/
                throwables.printStackTrace();
            }
        });
        set_avatar.start();
        try {
            set_avatar.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
