import com.github.kusaanko.youtubelivechat.ChatItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class mySQLFile {

    static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public static void createMainTable(Connection c, Statement stmt, String channelName) throws SQLException {
        stmt = c.createStatement();
        String sql = "CREATE TABLE " +channelName +
                " (USERID        CHAR(30)   NOT NULL," +
                " USERNAME       TEXT       NOT NULL, " +
                " CHANNELID      CHAR(30)   NOT NULL, " +
                " CHANNELNAME    TEXT       NOT NULL, " +
                " DATE           CHAR(30)   NOT NULL, " +
                " MESSAGE        TEXT       NOT NULL)" +
                " CHARACTER SET  utf8mb4" +
                " COLLATE        utf8mb4_general_ci";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public static void createIDTable(Connection c, Statement stmt) throws SQLException {
        stmt = c.createStatement();
        String sql = "CREATE TABLE userids" +
                " (USERID  INT PRIMARY KEY  NOT NULL," +
                " NAME           CHAR(100)  NOT NULL) " +
                " CHARACTER SET  utf8mb4" +
                " COLLATE        utf8mb4_general_ci";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public static void insertData(Connection c, ChatItem item, String channelID, String channelName) throws SQLException {

        String date = format.format(new Date(item.getTimestamp() / 1000));
        String userID = item.getAuthorChannelID();
        String userName = item.getAuthorName();
        String msg = item.getMessage();
        //    System.out.println(user.getUsers().get(0).getBroadcasterType());
        //    System.out.println(user.getUsers().get(0).getType());

        String sql = "INSERT INTO ChatYoutube (USERID,USERNAME,CHANNELID,CHANNELNAME,DATE,MESSAGE) VALUES (?,?,?,?,?,?);";
        try (PreparedStatement pstmt = c.prepareStatement(sql);) {
            pstmt.setString(1, userID);
            pstmt.setString(2, userName);
            pstmt.setString(3, channelID);
            pstmt.setString(4, channelName);
            pstmt.setString(5, date);
            pstmt.setBytes(6, msg.getBytes(StandardCharsets.UTF_8));

            pstmt.executeUpdate();
        }
        c.commit();
    }

    private static Connection establishConnection() throws IOException {

        Connection c = null;
        mainFile.setCredentials();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            c = DriverManager
                    .getConnection("jdbc:mysql://"+mainFile.databaseURL,
                            mainFile.databaseUsername,mainFile.databasePassword);
            c.setAutoCommit(false);
            System.out.println("Established connection successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        return c;
    }

    public static void main(String[] args) throws SQLException, IOException {

        Connection c = establishConnection();
        createMainTable(c, null, "ChatYoutube");
  //      createIDTable(c, null);

    }
}