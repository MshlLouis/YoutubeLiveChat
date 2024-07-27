import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class mainFile {

    static Connection c = null;
    static String databaseURL;
    static String databaseUsername;
    static String databasePassword;

    public static void setCredentials() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("credentials.txt"));
        String line;

        while ((line = br.readLine()) != null) {
            list.add(line);
        }

        if(list.size() != 3) {
            System.out.println("3 Credentials required, please check credentials.txt for inputs. Exiting program!");
            System.exit(1);
        }

        databaseURL = list.get(0);
        databaseUsername = list.get(1);
        databasePassword = list.get(2);
    }

    public static void main(String[] args) {

          try {
            setCredentials();
            c = DriverManager
                    .getConnection("jdbc:mariadb://"+databaseURL,
                            databaseUsername,databasePassword);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        HashMap<String,Integer> channels = new HashMap<>();

        try (FileInputStream fis = new FileInputStream("channels.txt");
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            String line;

            while ((line = br.readLine()) != null) {
                String [] split = line.split(",");
                channels.put(split[0],Integer.parseInt(split[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ExecutorService service = Executors.newFixedThreadPool(channels.size());
        channels.forEach((channelname,timer) -> service.submit(new ChannelThread(channelname,c,timer)));

    }
}
