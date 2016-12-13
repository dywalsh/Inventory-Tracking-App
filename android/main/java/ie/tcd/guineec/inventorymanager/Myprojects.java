package ie.tcd.guineec.inventorymanager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Myprojects extends AppCompatActivity implements OnClickListener {
    //side bar vars
    private User currentUser;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //variable for the sidebar to change screens
    NavigationView navigationView;
    //button to add a project
    private Button addProject_Button;
    private ArrayAdapter<String> adapter;
    private Project[] projects = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprojects_screen);


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
                        Intent intent = new Intent(Myprojects.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(Myprojects.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(Myprojects.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(Myprojects.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(Myprojects.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function
        addProject_Button = (Button) findViewById(R.id.add_projectBtn);
        addProject_Button.setOnClickListener(this);
        currentUser = MainActivity.getUser();
        Log.d("Proj", currentUser.getId());
        getProjects();
        registerClickCallback();

    }

    private void getProjects(){
        AsyncTask<Void, Void, Void> getProj = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o){
               if(projects != null) {
                   ListView list = (ListView) findViewById(R.id.listViewMain);
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

                    adapter = new ArrayAdapter<String>(Myprojects.this, android.R.layout.simple_list_item_1, s);
                }
                    return null;
            }
        };
        getProj.execute();
    }



    //registers when item in list is clicked
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listViewMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                ProjectOptions.setProj(projects[position]);
                Intent goToProject = new Intent(Myprojects.this, ProjectOptions.class);
                startActivity(goToProject);
                TextView textview = (TextView) viewClicked;
                String message = "You click: " + position;
                Toast.makeText(Myprojects.this, message, Toast.LENGTH_LONG).show();
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


    // add project code
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_projectBtn) {
            Intent intent = new Intent(Myprojects.this, AddProject.class);
            startActivity(intent);
        }
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
                        Intent intent = new Intent(Myprojects.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(Myprojects.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(Myprojects.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(Myprojects.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(Myprojects.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


    }



}
