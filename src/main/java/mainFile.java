import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
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
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager
                    .getConnection("jdbc:mysql://"+databaseURL,
                            databaseUsername,databasePassword);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        String [] channels = {"Wynnsanity","DesiArmyy","satvik"};
        ExecutorService service = Executors.newFixedThreadPool(channels.length);

        for (String s : channels) {
            service.submit(new ChannelThread(s,c));
        }
    }
}
