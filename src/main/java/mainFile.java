import com.github.kusaanko.youtubelivechat.ChatItem;
import com.github.kusaanko.youtubelivechat.IdType;
import com.github.kusaanko.youtubelivechat.YouTubeLiveChat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainFile {

    public static void main(String[] args) throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        YouTubeLiveChat chat = new YouTubeLiveChat("4zUmjNzOsgU", true, IdType.VIDEO);

        int liveStatusCheckCycle = 0;

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
}
