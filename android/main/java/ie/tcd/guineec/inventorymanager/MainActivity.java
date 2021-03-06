package ie.tcd.guineec.inventorymanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static User currentUser;
    private EditText email;
    private EditText pass;
    private TextView logStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = (Button) findViewById(R.id.loginButton);
        email = (EditText) findViewById(R.id.loginEmail);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);
        pass = (EditText) findViewById(R.id.loginPass);
        logStat = (TextView) findViewById(R.id.loginStatus);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }

        });
    }

    public void login() {
        final String mail = email.getText().toString();
        final String password = pass.getText().toString();
        AsyncTask<Void, Void, Void> loginAttempt = new AsyncTask<Void, Void, Void>() {
            ProgressDialog pd = new ProgressDialog(MainActivity.this);;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd.setMessage("Signing In...");
                pd.setIndeterminate(false);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCancelable(true);
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                currentUser = APICalls.login(mail, password);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                checkLogin(currentUser == null);
                pd.dismiss();
            }
        };
        loginAttempt.execute();
    }

    private void checkLogin(boolean userIsInit) {
        if(!userIsInit) {
            Log.d("Main", "Succ");
            switchWindow();
        } else {
            Log.d("Main", "Unsucc");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    logStat.setText("Login Failed");
                }
            });
        }
    }

    private void switchWindow() {
        Intent switchToMyProjects = new Intent(MainActivity.this, Myprojects.class);
        startActivity(switchToMyProjects);
    }

    public static User getUser() {
        return currentUser;
    }
}
