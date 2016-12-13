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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by dywal on 09/12/2016.
 */

public class useScannerResultAddIndividual extends AppCompatActivity {
    private Individual[] individuals=null;
    private ArrayAdapter<String> adapter;
    //side bar vars
    private User currentUser;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //variable for the sidebar to change screens
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_add_individual);
        currentUser = MainActivity.getUser();

        //side bar code
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //code to allow side bar to change screens onclick
        navigationView= (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                //code to change screens on clicks
                switch(item.getItemId()) {

                    case R.id.nav_projects:
                        Intent intent = new Intent(useScannerResultAddIndividual.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(useScannerResultAddIndividual.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(useScannerResultAddIndividual.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(useScannerResultAddIndividual.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(useScannerResultAddIndividual.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function

        getIndividual();
        registerClickCallback();
    }


    private void getIndividual(){
        AsyncTask<Void, Void, Void> getProj = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o){
                if(individuals != null) {
                    ListView list = (ListView) findViewById(R.id.listViewScannerIndividuals);
                    list.setAdapter(adapter);
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                individuals = APICalls.getAllIndividuals(currentUser);
                if(individuals != null) {
                    String[] s = new String[individuals.length];

                    for (int i = 0; i < individuals.length; i++) {
                        s[i] = individuals[i].getName();

                    }

                    adapter = new ArrayAdapter<String>(useScannerResultAddIndividual.this, android.R.layout.simple_list_item_1, s);
                }
                return null;
            }
        };
        getProj.execute();
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listViewScannerIndividuals);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                useScannerResult.setIndividual(individuals[position]);
                TextView textview = (TextView) viewClicked;
                String message = "You click: " + parent;
                Toast.makeText(useScannerResultAddIndividual.this, message, Toast.LENGTH_LONG).show();

                Intent Switch = new Intent(useScannerResultAddIndividual.this, useScannerResult.class);
                startActivity(Switch);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
