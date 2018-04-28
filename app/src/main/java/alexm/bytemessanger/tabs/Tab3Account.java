package alexm.bytemessanger.tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sendbird.android.SendBird;

import alexm.bytemessanger.R;

/**
 * Created by alexm on 11.04.2018.
 */

public class Tab3Account extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3account, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.name);
        tv.setText(SendBird.getCurrentUser().getNickname());
        return rootView;
    }
}
