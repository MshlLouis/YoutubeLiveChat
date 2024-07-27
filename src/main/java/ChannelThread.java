import com.github.kusaanko.youtubelivechat.ChatItem;
import com.github.kusaanko.youtubelivechat.IdType;
import com.github.kusaanko.youtubelivechat.YouTubeLiveChat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChannelThread implements Runnable {

    String channelName;
    Connection c;

    public ChannelThread(String channelName, Connection c) {
        this.channelName = channelName;
        this.c = c;
    }

    public void getMessages() throws IOException, InterruptedException, SQLException {
        String channelId = YouTubeLiveChat.getChannelIdFromURL("https://www.youtube.com/@" +channelName);
        YouTubeLiveChat chat = new YouTubeLiveChat(channelId, true, IdType.CHANNEL);

        chat.update();
        Thread.sleep(1000);

        while (true) {
            chat.update();
            for (ChatItem item : chat.getChatItems()) {
             //   System.out.println(chat.getChannelId() +" " +format.format(new Date(item.getTimestamp() / 1000)) + " " + item.getType() + "[" + item.getAuthorName() + "] " +item.getAuthorChannelID() +" " + item.getAuthorType() + " " + item.getMessage() );
                mySQLFile.insertData(c, item, channelId, channelName);
            }
            Thread.sleep(1000);
        }
    }

    public void run() {
        try {
            getMessages();
        } catch (IOException | InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}