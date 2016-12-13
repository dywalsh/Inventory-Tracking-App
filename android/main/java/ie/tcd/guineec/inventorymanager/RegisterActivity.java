package ie.tcd.guineec.inventorymanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;


public class RegisterActivity extends AppCompatActivity {
    private EditText username, email, password;
    private static boolean duplicate;
    private TextView logStat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        logStat = (TextView) findViewById(R.id.loginStatus);
        AlertDialog.Builder build = new AlertDialog.Builder(RegisterActivity.this);
        build.setTitle("WARNING");
        build.setMessage("This application is a proof of concept, and as such information security is far from guaranteed. DO NOT use any meaningful" +
                " or sensitive information while using this app.");
        build.setPositiveButton("Agree", null);
        build.show();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    public void register() {
        username = (EditText) findViewById(R.id.etUsername);
        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);

        final String etUsername1 = username.getText().toString();
        final String etPassword1 = password.getText().toString();
        final String etEmail1 = email.getText().toString();

        AsyncTask<Void, Void, Void> addUser = new AsyncTask<Void, Void, Void>() {
            ProgressDialog pd = new ProgressDialog(RegisterActivity.this);;

            @Override
            protected void onPreExecute() {
                Log.d("Main", "onpre");
                super.onPreExecute();
                pd.setMessage("Registering...");
                pd.setIndeterminate(false);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCancelable(true);
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... voids){
                Log.d("Main", "background");
                duplicate = APICalls.addUser(etUsername1,etEmail1,etPassword1);
                return null;
            }

            protected void onPostExecute(Void unused) {
                Log.d("Main", "onpost");
                checkLogin(duplicate);
                pd.dismiss();
            }
        };
        addUser.execute();
    }

    private void checkLogin(boolean userIsInit) {
        if(userIsInit) {
            Log.d("Main", "Succ");
            switchWindow();
        } else {
            Log.d("Main", "Unsucc");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    logStat.setText("Registering Failed");
                }
            });
        }
    }

    private void switchWindow() {
        Intent switchToMyProjects = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(switchToMyProjects);
    }

}