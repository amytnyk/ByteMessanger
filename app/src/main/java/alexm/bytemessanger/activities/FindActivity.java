package alexm.bytemessanger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import alexm.bytemessanger.R;
import alexm.bytemessanger.adapters.FoundAdapter;
import alexm.bytemessanger.utils.FoundItem;

public class FindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);


        mName = (EditText) findViewById(R.id.name);
        users = new ArrayList<>();
        fa = new FoundAdapter(this, users);
        lv = (ListView) findViewById(R.id.found);
        lv.setAdapter(fa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> userIDS = new ArrayList<>();
                userIDS.add(SendBird.getCurrentUser().getUserId());
                userIDS.add(users.get(position).id);
                GroupChannel.createChannelWithUserIds(userIDS, true, users.get(position).name, null, null, new GroupChannel.GroupChannelCreateHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        finish();
                    }
                });
            }
        });

        Button find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputStream os = null;
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                    socket = new Socket(serverAddr, SERVERPORT);
                    socket.setTcpNoDelay(true);
                    os = socket.getOutputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(os);
                    bos.write(("f" + mName.getText().toString() + "\n").getBytes());
                    bos.flush();
                    new Thread(new ClientThread()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                is = socket.getInputStream();
                BufferedReader br =  new BufferedReader(new InputStreamReader(is));
                String answer = br.readLine();
                String[] us = answer.split("/:");
                users.clear();
                String id = SendBird.getCurrentUser().getUserId();
                for (int i = 0;i < us.length;i += 2) {
                    if (!us[i].equals(id))
                        users.add(new FoundItem(us[i], us[i + 1]));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fa.notifyDataSetChanged();
                    }
                });
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public ListView lv;
    public List<FoundItem> users;
    public FoundAdapter fa;
    public FoundItem fi;
    public EditText mName;
    public Socket socket;
    public InputStream is;
    private static final int SERVERPORT = 8080;
    private static final String SERVER_IP = "domatur.com";
}
