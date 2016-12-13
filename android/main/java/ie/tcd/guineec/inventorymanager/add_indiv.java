package ie.tcd.guineec.inventorymanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.os.AsyncTask;
public class add_indiv extends AppCompatActivity {

    //side bar vars
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //variable for the sidebar to change screens
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_indiv_screen);
        final Button bOK = (Button) findViewById(R.id.ok_button);

        //side bar code
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //code to allow side bar to change screens onclick
        navigationView= (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //code to change screens on clicks
                switch (item.getItemId()) {
                    case R.id.nav_projects:
                        Intent intent = new Intent(add_indiv.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(add_indiv.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(add_indiv.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(add_indiv.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(add_indiv.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function
        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User CurrentUser = MainActivity.getUser();
                EditText firstName, lastName;
                firstName = (EditText) findViewById(R.id.first_name);
                lastName = (EditText) findViewById(R.id.last_name);

                String firstName1 = firstName.getText().toString();
                String lastName1 = lastName.getText().toString();
                final String indivName = firstName1 + " " + lastName1;
                AsyncTask<Void,Void,Void> addIndiv = new AsyncTask(){
                    @Override
                    protected void onPreExecute() {
                        Intent intent = new Intent(add_indiv.this,add_indiv.class);
                        startActivity(intent);
                    }

                    @Override
                    protected Object doInBackground(Object[] params) {
                        APICalls.addIndividual(indivName, CurrentUser);
                        return null;
                    }
                };
                addIndiv.execute();
            }
        });
    }

    //side bar function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //results of user input for add indiv
    //onclick function to save add indiv inputs


}
