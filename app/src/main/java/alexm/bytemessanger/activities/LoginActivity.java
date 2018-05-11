package alexm.bytemessanger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sendbird.android.SendBird;

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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SendBird.init("CC6CDA7B-EA0B-41CC-B520-251A4A4D137D", this.getApplicationContext());

        Button login_btn = (Button) findViewById(R.id.sign_in_button);
        login_btn.setOnClickListener(new OnClickListener() {
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
                    bos.write(("l" + email.getText().toString() + "/:" + password.getText() + "\n").toString().getBytes());
                    bos.flush();
                    new Thread(new ClientThread()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button fp = (Button) findViewById(R.id.fp);
        fp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
            }
        });

        Button register = (Button) findViewById(R.id.register_btn);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email_login);

        er = (TextView) findViewById(R.id.er);
    }

    public Socket socket;
    private static final int SERVERPORT = 8080;
    private static final String SERVER_IP = "domatur.com";
    private EditText password;
    private EditText email;
    public TextView er;
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
                if (!answer.equals("UN")){
                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                    editor.putString("EMAIL", email.getText().toString());
                    editor.putString("PASSWORD", password.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("key", answer);
                    startActivity(intent);
                }
                else {
                    er.setText(getString(R.string.wrong_login_or_password));
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}



