import com.github.kusaanko.youtubelivechat.ChatItem;
import com.github.kusaanko.youtubelivechat.ChatItemDelete;
import com.github.kusaanko.youtubelivechat.IdType;
import com.github.kusaanko.youtubelivechat.YouTubeLiveChat;
import com.sun.jdi.ThreadReference;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class ChannelThread implements Runnable {

    String channelName;
    Connection c;
    int updateTimer;
    boolean switchedStatus = false;

    public ChannelThread(String channelName, Connection c, int updateTimer) {
        this.channelName = channelName;
        this.c = c;
        this.updateTimer = updateTimer;
    }

    private boolean isLive (String channelID) {
        try {
            new YouTubeLiveChat(channelID, true, IdType.CHANNEL);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void getMessages() throws IOException, InterruptedException, SQLException {

        String channelID = YouTubeLiveChat.getChannelIdFromURL("https://www.youtube.com/@" +channelName);
        YouTubeLiveChat chat = null;

        while (true) {
            if(!isLive(channelID)) {
                switchedStatus = false;
            //    System.out.println("Channel " +channelName +" is not live.");
                Thread.sleep(60000);
            }
            else {
                if(!switchedStatus) {
                    switchedStatus = true;
                    chat = new YouTubeLiveChat(channelID, true, IdType.CHANNEL);
                    chat.update();
                    Thread.sleep(1000);
                }
                else {
                    try {
                        chat.update();
                        for (ChatItem item : chat.getChatItems()) {
                        //    System.out.println(chat.getChannelId() +" " + mySQLFile.format.format(new Date(item.getTimestamp() / 1000)) + " " + item.getType() + "[" + item.getAuthorName() + "] " +item.getAuthorChannelID() +" " + item.getAuthorType() + " " + item.getMessage() );
                            mySQLFile.insertData(c, item, channelID, channelName);
                            mySQLFile.insertDataIDs(c, item);
                        }
//            for (ChatItemDelete delete : chat.getChatItemDeletes()) {
//                System.out.println(delete.getMessage() + " TargetId:" + delete.getTargetId());
//            }
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(updateTimer*1000L);
                }
            }
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