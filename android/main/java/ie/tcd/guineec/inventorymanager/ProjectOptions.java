package ie.tcd.guineec.inventorymanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.List;


/**
 * Created by DDunne on 07/12/2016.
 */

public class ProjectOptions extends AppCompatActivity implements OnClickListener {
    Spinner spinner;
    ArrayAdapter<String> adapter = null;
    ArrayAdapter<String> adapter2 = null;
    ArrayAdapter<Integer> adapter3 = null;
    Individual[] indiv = null;
    Individual selIndiv = null;
    Individual[] indivOnProject = null;
    Equipment[] objectsOnProject = null;
    private Button addInd;

    //side bar vars
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //variable for the sidebar to change screens
    NavigationView navigationView;
    //button to add a project
    private Button addProject_Button;
    private static Project project;
    private User currentUser = MainActivity.getUser();
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_options_screen);

        addInd = (Button) findViewById(R.id.addIndivToProj);
        addInd.setOnClickListener(this);

//side bar code
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //code to allow side bar to change screens onclick
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //code to change screens on clicks
                switch (item.getItemId()) {

                    case R.id.nav_projects:
                        Intent intent = new Intent(ProjectOptions.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(ProjectOptions.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(ProjectOptions.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(ProjectOptions.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(ProjectOptions.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function
        newSpinner();

        TextView txtView=(TextView)findViewById(R.id.projName);
        txtView.setText(project.getName());
        txtView = (TextView)findViewById(R.id.end_date);
        txtView.setText(project.getEndDate());

    }

    private void newSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        getIndividuals();
    }

    private void getIndividuals() {
        AsyncTask<Void, Void, Void> getIndiv = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
               if(indiv != null) {
                   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   spinner.setAdapter(adapter);
                   spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           selIndiv = indiv[position];
                       }

                       @Override
                       public void onNothingSelected(AdapterView<?> adapterView) {

                       }
                   });
               }

                if(indivOnProject != null) {
                    ListView list = (ListView) findViewById(R.id.listOfIndivOnProj);
                    list.setAdapter(adapter2);
                }
                if(objectsOnProject != null){
                    ListView list = (ListView) findViewById(R.id.listOfObjectsOnProj);
                    list.setAdapter(adapter3);
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                indiv = APICalls.getAllIndividuals(currentUser);
                indivOnProject = APICalls.getIndsAttachedToProj(project, currentUser);
                objectsOnProject = APICalls.getObjsAttachedToProj(project, currentUser);
                if (indiv != null) {
                    String[] s = new String[indiv.length];
                    for (int i = 0; i < indiv.length; i++) {
                        s[i] = indiv[i].getName();

                    }
                    adapter = new ArrayAdapter<String>(ProjectOptions.this, android.R.layout.simple_spinner_item, s);
                }
                if(indivOnProject != null){
                    String[] p = new String[indivOnProject.length];
                    for (int i = 0; i < indivOnProject.length; i++) {
                        p[i] = indivOnProject[i].getName();
                    }
                    adapter2 = new ArrayAdapter<String>(ProjectOptions.this, android.R.layout.simple_list_item_1, p);
                }
                if(objectsOnProject != null){
                    Integer[] q = new Integer[objectsOnProject.length];
                    for (int i = 0; i < objectsOnProject.length; i++) {
                        q[i] = objectsOnProject[i].getId();
                    }
                    adapter3 = new ArrayAdapter<Integer>(ProjectOptions.this, android.R.layout.simple_list_item_1, q);
                }
                return null;

            }
        };
        
        getIndiv.execute();
    }

    private void addToProj() {
        AsyncTask<Void, Void, Void> addIndiv = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
                selIndiv = null;
                Intent intent = new Intent(ProjectOptions.this, Myprojects.class);
                startActivity(intent);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                APICalls.attachIndToProj(selIndiv, project);
                return null;

            }
        };

        addIndiv.execute();
    }


    public static void setProj(Project proj){
        project = proj;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // add project code
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addIndivToProj ){
            if(selIndiv != null) {
                addToProj();
            }

        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //code to allow side bar to change screens onclick
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //code to change screens on clicks
                switch (item.getItemId()) {

                    case R.id.nav_projects:
                        Intent intent = new Intent(ProjectOptions.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(ProjectOptions.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(ProjectOptions.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent4 = new Intent(ProjectOptions.this, MainActivity.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


    }
}
