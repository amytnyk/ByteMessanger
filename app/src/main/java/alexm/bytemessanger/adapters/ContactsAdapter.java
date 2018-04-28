package alexm.bytemessanger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alexm.bytemessanger.R;
import alexm.bytemessanger.utils.Contact;

/**
 * Created by alexm on 18.04.2018.
 */

public class ContactsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<Contact> contacts;

    public ContactsAdapter(Context context, List<Contact> contacts) {
        ctx = context;
        this.contacts = contacts;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return contacts.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.contact, parent, false);
        }

        Contact s = getContact(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        TextView contact_name = (TextView) view.findViewById(R.id.contact_name);
        contact_name.setText(s.contact_name);
        TextView new_messages = (TextView) view.findViewById(R.id.new_messages);
        new_messages.setText(String.valueOf(s.new_messages));
        //view.findViewById(R.id.ivImage)).setImageResource(p.image);
        return view;
    }

    // товар по позиции
    Contact getContact(int position) {
        return ((Contact) getItem(position));
    }

}
