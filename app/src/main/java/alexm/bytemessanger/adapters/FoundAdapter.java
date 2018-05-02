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
import alexm.bytemessanger.utils.FoundItem;

public class FoundAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<FoundItem> users;

    public FoundAdapter(Context context, List<FoundItem> users) {
        ctx = context;
        this.users = users;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return users.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return users.get(position);
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
            view = lInflater.inflate(R.layout.found_item, parent, false);
        }

        FoundItem s = getFoundItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
       // // и картинка
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(s.name);
        //TextView new_messages = (TextView) view.findViewById(R.id.new_messages);
        //new_messages.setText(String.valueOf(s.new_messages));
        //view.findViewById(R.id.ivImage)).setImageResource(p.image);
        return view;
    }

    // товар по позиции
    FoundItem getFoundItem(int position) {
        return ((FoundItem) getItem(position));
    }

}

