package alexm.bytemessanger.fragments;

import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.util.ArrayList;
import java.util.List;

import alexm.bytemessanger.R;
import alexm.bytemessanger.pickers.DatePickerFragment;
import alexm.bytemessanger.pickers.TimePickerFragment;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.maps_layout, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    public MapView mMapView;
    public GoogleMap mGoogleMap;
    public AlertDialog ad;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.new_meet_point ,null);
                final EditText mName = (EditText) mView.findViewById(R.id.name);
                final TextView tvDate = (TextView) mView.findViewById(R.id.date);
                final TextView tvTime = (TextView) mView.findViewById(R.id.time);

                Button date = (Button) mView.findViewById(R.id.choose_date);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new DatePickerFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                    }
                });

                Button time = (Button) mView.findViewById(R.id.choose_time);
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new TimePickerFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker"
                        );
                    }
                });

                Button create = (Button) mView.findViewById(R.id.create);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Meet point").snippet("as"));

                    }
                });

                builder.setView(mView);
                AlertDialog dialog = builder.create();
                //dialog.show();
                ad = dialog;
                ad.show();

            }
        });
    }
}