package alexm.bytemessanger.utils;

/**
 * Created by alexm on 18.04.2018.
 */

public class Contact {
    public String contact_name;
    public String contact_ID;
    public int new_messages;

    public Contact(String contact_ID, String contact_name, int new_messages) {
        this.new_messages = new_messages;
        this.contact_name = contact_name;
        this.contact_ID = contact_ID;
    }
}
