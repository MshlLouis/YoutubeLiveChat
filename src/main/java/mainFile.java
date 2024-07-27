
public class mainFile {

    public static void main(String[] args) {

        ChannelThread t1 = new ChannelThread("Wynnsanity");
        ChannelThread t2 = new ChannelThread("SnaxGaming");
        new Thread(t1).start();
        new Thread(t2).start();
    }
}
