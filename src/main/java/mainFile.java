import com.github.kusaanko.youtubelivechat.ChatItem;
import com.github.kusaanko.youtubelivechat.YouTubeLiveChat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainFile {

    static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static void getChannelMessages(YouTubeLiveChat chat) throws IOException {
        int liveStatusCheckCycle = 0;

        chat.update();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            chat.update();

            for (ChatItem item : chat.getChatItems()) {
                System.out.println(format.format(new Date(item.getTimestamp() / 1000)) + " " + item.getType() + "[" + item.getAuthorName() + "] " +item.getAuthorChannelID() +" " + item.getAuthorType() + " " + item.getMessage() );
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

    public static void main(String[] args) throws IOException {

       /* String channelId = YouTubeLiveChat.getChannelIdFromURL("https://www.youtube.com/@Wynnsanity");
        YouTubeLiveChat chat = new YouTubeLiveChat(channelId, true, IdType.CHANNEL);

        getChannelMessages(chat);*/

        ChannelThread t1 = new ChannelThread("Wynnsanity");
        ChannelThread t2 = new ChannelThread("SnaxGaming");
        new Thread(t1).start();
        new Thread(t2).start();
    }
}
