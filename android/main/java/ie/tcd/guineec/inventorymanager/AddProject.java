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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * Created by DDunne on 09/12/2016.
 */

public class AddProject extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //variable for the sidebar to change screens
    NavigationView navigationView;
    Spinner spinner;
    ArrayAdapter<String> adapter = null;
    Individual[] indiv = null;
    Individual selIndiv = null;
    private Button addProjectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);



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
                        Intent intent = new Intent(AddProject.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(AddProject.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(AddProject.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(AddProject.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(AddProject.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function
        addProjectButton = (Button) findViewById(R.id.addProj);
        addProjectButton.setOnClickListener(this);
        newSpinner();
    }

    //side bar function
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
        if(v.getId() == R.id.addProj){
            if(selIndiv != null) {
                ETprojName = (EditText)findViewById(R.id.project_name);
                ETendDate = (EditText)findViewById(R.id.end_date);
                projName = ETprojName.getText().toString();
                endDate = ETendDate.getText().toString();
                endDate.replace('/', '-');
                endDate.replace('.', '-');
                endDate.replace('_', '-');
                addProject();
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
                        Intent intent = new Intent(AddProject.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(AddProject.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(AddProject.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(AddProject.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(AddProject.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


    }

    private void newSpinner() {
        spinner = (Spinner) findViewById(R.id.addProjSpinner);
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
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                indiv = APICalls.getAllIndividuals(MainActivity.getUser());
                if (indiv != null) {
                    String[] s = new String[indiv.length];
                    for (int i = 0; i < indiv.length; i++) {
                        s[i] = indiv[i].getName();
                    }
                    adapter = new ArrayAdapter<String>(AddProject.this, android.R.layout.simple_spinner_item, s);
                }
                return null;

            }
        };

        getIndiv.execute();
    }

    private void addProject() {
        AsyncTask<Void, Void, Void> addProj = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
                selIndiv = null;
                Intent intent = new Intent(AddProject.this, Myprojects.class);
                startActivity(intent);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                APICalls.addProject(endDate, selIndiv, projName, MainActivity.getUser());
                return null;

            }
        };

        addProj.execute();
    }

    private EditText ETprojName, ETendDate;
    private String projName, endDate;
}
