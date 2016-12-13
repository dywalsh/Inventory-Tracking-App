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

public class useScannerResultAddProject extends AppCompatActivity {
    private Project[] projects=null;
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
        setContentView(R.layout.scanner_add_projects);

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
                        Intent intent = new Intent(useScannerResultAddProject.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(useScannerResultAddProject.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(useScannerResultAddProject.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(useScannerResultAddProject.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(useScannerResultAddProject.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function
        currentUser = MainActivity.getUser();
        getProjects();
        registerClickCallback();
    }

    private void getProjects(){
        AsyncTask<Void, Void, Void> getProj = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o){
                if(projects != null) {
                    ListView list = (ListView) findViewById(R.id.listViewScannerProjects);
                    list.setAdapter(adapter);
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                projects = APICalls.getAllProjects(currentUser);
                if(projects != null) {
                    String[] s = new String[projects.length];

                    for (int i = 0; i < projects.length; i++) {
                        s[i] = projects[i].getName();

                    }

                    adapter = new ArrayAdapter<String>(useScannerResultAddProject.this, android.R.layout.simple_list_item_1, s);
                }
                return null;
            }
        };
        getProj.execute();
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listViewScannerProjects);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                useScannerResult.setProject(projects[position]);
                TextView textview = (TextView) viewClicked;
                String message = "You click: " + parent;
                Toast.makeText(useScannerResultAddProject.this, message, Toast.LENGTH_LONG).show();

                Intent Switch = new Intent(useScannerResultAddProject.this, useScannerResult.class);
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
