package ie.tcd.guineec.inventorymanager;

/**
 * Created by DDunne on 08/12/2016.
 */

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


import java.util.ArrayList;

/**
 * Created by dywal on 09/12/2016.
 */

public class ObjectOptions extends AppCompatActivity {
    //side bar vars
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //variable for the sidebar to change screens
    NavigationView navigationView;
    //search function and list
    private Equipment[] objectsList=null;
    private User currentUser;
    private ArrayAdapter<String> adapter;
    private static String barcode;
    private TextView text;
    private ArrayList<Equipment> objectArrayList= new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.objects_options_screen);

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
                        Intent intent = new Intent(ObjectOptions.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(ObjectOptions.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(ObjectOptions.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(ObjectOptions.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(ObjectOptions.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function

        text=(TextView)findViewById(R.id.objectoptionstext) ;
        currentUser = MainActivity.getUser();
        getObjects();
        registerClickCallback();
        text.setText("Search Started");
        // registerClickCallback();
    }

    private void getObjects() {
        AsyncTask<Void, Void, Void> getObj = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o){
                text.setText("Barcode matches:");
                if(objectsList != null) {
                    ListView list = (ListView) findViewById(R.id.listViewMainObject);
                    list.setAdapter(adapter);
                }
                else
                {
                    text.setText("No barcode matches");
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                objectsList = APICalls.getAllObjects(currentUser);
                if(objectsList != null) {
                    int matches=0;
                    for(int j=0; j<objectsList.length; j++)
                    {
                        if (objectsList[j].getBarcode().equals(barcode)) {
                            matches++;
                        }
                    }
                    String[] s = new String[matches];
                    int n=0;
                    for (int i = 0; i < objectsList.length; i++) {
                        if (objectsList[i].getBarcode().equals(barcode)) {
                            s[n] = objectsList[i].getId()+"";
                            objectArrayList.add(objectsList[i]);
                            n++;
                        }

                    }
                    adapter = new ArrayAdapter<String>(ObjectOptions.this, android.R.layout.simple_list_item_1, s);
                }
                return null;
            }
        };
        getObj.execute();
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listViewMainObject);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                TextView textview = (TextView) viewClicked;
                String message = "You click: " + position +","+ objectArrayList.size();
                Toast.makeText(ObjectOptions.this, message, Toast.LENGTH_LONG).show();
                ObjectOptionsSelected.setObjectSelected(objectArrayList.get(position));
                Intent switchToSelectedObject = new Intent(ObjectOptions.this, ObjectOptionsSelected.class);
                startActivity(switchToSelectedObject);
            }
        });
    }



    public static void setBarcode(String barcodeScanned){
        barcode=barcodeScanned;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

