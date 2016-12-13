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


public class LoginActivity extends AppCompatActivity {
    private static User currentUser;
    private EditText email;
    private EditText pass;
    private TextView logStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.etUsername);
        pass = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);
        logStat = (TextView) findViewById(R.id.loginStatus);


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    LoginActivity.this.startActivity(registerIntent);
                }

        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    public void login() {
        final String mail = email.getText().toString();
        final String password = pass.getText().toString();
        AsyncTask<Void, Void, Void> loginAttempt = new AsyncTask<Void, Void, Void>() {
            ProgressDialog pd = new ProgressDialog(LoginActivity.this);;
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
        Intent switchToMyProjects = new Intent(LoginActivity.this, Myprojects.class);
        startActivity(switchToMyProjects);
    }

    public static User getUser() {
        return currentUser;
    }
}


