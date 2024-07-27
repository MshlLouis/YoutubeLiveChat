import com.github.kusaanko.youtubelivechat.ChatItem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class mySQLFile {

    static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public static void createMainTable(Connection c, Statement stmt, String channelName) throws SQLException {
        stmt = c.createStatement();
        String sql = "CREATE TABLE " +channelName +
                " (USERID        CHAR(30)   NOT NULL," +
                " USERNAME       TEXT       NOT NULL, " +
                " CHANNELID      CHAR(30)   NOT NULL, " +
                " CHANNELNAME    TEXT       NOT NULL, " +
                " MESSAGETYPE    TEXT       NOT NULL, " +
                " PURCHASEAMOUNT TEXT       , " +
                " AUTHORTYPE     TEXT       NOT NULL, " +
                " DATE           CHAR(30)   NOT NULL, " +
                " MESSAGE        TEXT       )" +
                " CHARACTER SET  utf8mb4" +
                " COLLATE        utf8mb4_unicode_520_ci";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public static void createIDTable(Connection c, Statement stmt) throws SQLException {
        stmt = c.createStatement();
        String sql = "CREATE TABLE useridsYT" +
                " (USERID        CHAR(30) PRIMARY KEY  NOT NULL," +
                " NAME           TEXT                  NOT NULL) " +
                " CHARACTER SET  utf8mb4" +
                " COLLATE        utf8mb4_unicode_520_ci";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public static void insertData(Connection c, ChatItem item, String channelID, String channelName) throws SQLException {

        String userID = item.getAuthorChannelID();
        String userName = item.getAuthorName();
        String msgType = String.valueOf(item.getType());
        String authorType = item.getAuthorType().toString();
        String date = format.format(new Date(item.getTimestamp() / 1000));
        String purchaseAmount = item.getPurchaseAmount();
        String msg = item.getMessage();

        String sql = "INSERT INTO ChatYoutube (USERID,USERNAME,CHANNELID,CHANNELNAME,MESSAGETYPE,PURCHASEAMOUNT,AUTHORTYPE,DATE,MESSAGE) VALUES (?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement pstmt = c.prepareStatement(sql);) {
            pstmt.setString(1, userID);
            pstmt.setBytes(2, userName.getBytes(StandardCharsets.UTF_8));
            pstmt.setString(3, channelID);
            pstmt.setBytes(4, channelName.getBytes(StandardCharsets.UTF_8));
            pstmt.setString(5, msgType);
            pstmt.setString(6, purchaseAmount);
            pstmt.setString(7, authorType);
            pstmt.setString(8, date);
            if (msg != null) {
                pstmt.setBytes(9, msg.getBytes(StandardCharsets.UTF_8));
            } else {
                pstmt.setString(9, null);
            }

            pstmt.executeUpdate();
        }
        c.commit();
    }

    public static void insertDataIDs(Connection c, ChatItem item) throws SQLException {

        String userID = item.getAuthorChannelID();
        String userName = item.getAuthorName();

        String sql = "INSERT INTO useridsYT" +" (USERID,NAME) VALUES (?,?);";
        try (PreparedStatement pstmt = c.prepareStatement(sql);) {
            pstmt.setString(1, userID);
            pstmt.setBytes(2, userName.getBytes(StandardCharsets.UTF_8));

            pstmt.executeUpdate();
        }
        catch (Exception ignored) {

        }
        c.commit();
    }

    private static Connection establishConnection() throws IOException {

        Connection c = null;
        mainFile.setCredentials();

        try {
            c = DriverManager
                    .getConnection("jdbc:mariadb://"+mainFile.databaseURL,
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
        createIDTable(c, null);

    }
}