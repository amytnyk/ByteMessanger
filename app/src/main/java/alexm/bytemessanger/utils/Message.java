package alexm.bytemessanger.utils;

import java.util.Date;

/**
 * Created by alexm on 18.04.2018.
 */

public class Message {
    public String text;
    public String owner;
    public String ownerID;
    public long time;
    public String avatar_URL;


    public Message(String text, String owner,String ownerID, long time, String avatar_URL) {
        this.text = text;
        this.owner = owner;
        this.ownerID = ownerID;
        this.time = time;
        this.avatar_URL = avatar_URL;
    }
}
