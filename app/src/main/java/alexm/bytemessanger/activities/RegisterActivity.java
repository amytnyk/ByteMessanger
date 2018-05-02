package alexm.bytemessanger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import alexm.bytemessanger.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        SendBird.init("CC6CDA7B-EA0B-41CC-B520-251A4A4D137D", this.getApplicationContext());
        Button register_button = (Button) findViewById(R.id.register);
        register_button.setOnClickListener(new View.OnClickListener() {
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
                    bos.write(("r" + email.getText().toString() + "/:" + password.getText().toString() + "/:" + name.getText().toString() +"\n").toString().getBytes());
                    bos.flush();
                    new Thread(new ClientThread()).start();
                    er.setText("Wrong login or password");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button back_to_login = (Button) findViewById(R.id.login);
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        er = (TextView) findViewById(R.id.er);
    }

    private void connectToSendBird(final String userId, final String userNickname, final String answer) {
        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    return;
                }
                updateCurrentUserInfo(userNickname, answer);

            }
        });
    }

    private void updateCurrentUserInfo(String userNickname, final String answer) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("key", answer);
                startActivity(intent);
            }
        });
    }

    public Socket socket;
    private static final int SERVERPORT = 8080;
    private static final String SERVER_IP = "domatur.com";
    private TextView er;
    public EditText email;
    public EditText password;
    public EditText name;
    public InputStream is;


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
                if (!answer.equals("UN")) {
                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                    editor.putString("EMAIL", email.getText().toString());
                    editor.putString("PASSWORD", password.getText().toString());
                    editor.commit();
                    connectToSendBird(answer, name.getText().toString(), answer);
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
