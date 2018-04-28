package alexm.bytemessanger.utils;

/**
 * Created by alexm on 12.04.2018.
 */

public class Server {
    public String last_message;
    public String server_name;
    public String ID;
    public int new_messages;

    public Server(String last_message, String ID, String server_name, int new_messages) {
        this.ID = ID;
        this.last_message = last_message;
        this.server_name = server_name;
        this.new_messages = new_messages;
    }
}
