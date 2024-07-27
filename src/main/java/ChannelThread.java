import com.github.kusaanko.youtubelivechat.ChatItem;
import com.github.kusaanko.youtubelivechat.IdType;
import com.github.kusaanko.youtubelivechat.YouTubeLiveChat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChannelThread implements Runnable {

    String channelName;
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public ChannelThread(String channelName) {
        this.channelName = channelName;
    }

    public void getMessages() throws IOException {
        String channelId = YouTubeLiveChat.getChannelIdFromURL("https://www.youtube.com/@" +channelName);
        YouTubeLiveChat chat = new YouTubeLiveChat(channelId, true, IdType.CHANNEL);
        int liveStatusCheckCycle = 0;

        chat.update();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            chat.update();

            for (ChatItem item : chat.getChatItems()) {
                System.out.println(chat.getChannelId() +" " +format.format(new Date(item.getTimestamp() / 1000)) + " " + item.getType() + "[" + item.getAuthorName() + "] " +item.getAuthorChannelID() +" " + item.getAuthorType() + " " + item.getMessage() );
            }
            liveStatusCheckCycle++;

            if(liveStatusCheckCycle >= 60) {
                if(!chat.getBroadcastInfo().isLiveNow) {
                    break;
                }
                liveStatusCheckCycle = 0;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            getMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}