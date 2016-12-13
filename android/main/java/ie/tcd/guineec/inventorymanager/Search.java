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
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * Created by DDunne on 12/12/2016.
 */

public class Search extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner, spinner2;
    ArrayAdapter<String> adapter = null;
    ArrayAdapter<String> adapter2 = null;
    ArrayAdapter<String> adapter3 = null;
    Individual[] indiv = null;
    Individual selIndiv = null;
    Project[] proj;
    Project selProj;
    Equipment[] e = null;
    Equipment[] i = null;
    Equipment[] p = null;
    Equipment[] damaged = null;
    ArrayList<Equipment> shared = new ArrayList<>();
    private Button searchDate, searchDateIndiv, searchDateProj;

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
        setContentView(R.layout.search_screen);

        searchDate = (Button) findViewById(R.id.button_search_date);
        searchDate.setOnClickListener(this);
        searchDateIndiv = (Button) findViewById(R.id.button_search_indiv_date);
        searchDateIndiv.setOnClickListener(this);
        searchDateProj = (Button) findViewById(R.id.button_search_project_date);
        searchDateProj.setOnClickListener(this);

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
                        Intent intent = new Intent(Search.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(Search.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(Search.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(Search.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(Search.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function

        newSpinner();

    }


    private void newSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_search_indiv);
        spinner2 = (Spinner) findViewById(R.id.spinner_search_project);
        getIndivAndProj();
    }

    private void getIndivAndProj() {
        AsyncTask<Void, Void, Void> getIndivProj = new AsyncTask() {

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
                if (proj != null){
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selProj = proj[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }


            }

            @Override
            protected Object doInBackground(Object[] objects) {
                indiv = APICalls.getAllIndividuals(currentUser);
                proj = APICalls.getAllProjects(currentUser);
                if (indiv != null) {
                    String[] s = new String[indiv.length];
                    for (int i = 0; i < indiv.length; i++) {
                        s[i] = indiv[i].getName();

                    }
                    adapter = new ArrayAdapter<String>(Search.this, android.R.layout.simple_spinner_item, s);
                }
                if(proj != null){
                    String[] p = new String[proj.length];
                    for (int i = 0; i < proj.length; i++) {
                        p[i] = proj[i].getName();
                    }
                    adapter2 = new ArrayAdapter<String>(Search.this, android.R.layout.simple_spinner_item, p);
                }
                return null;

            }
        };

        getIndivProj.execute();
    }

    private void getByDate() {
        AsyncTask<Void, Void, Void> byDate = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
                SearchResult.getAdapter(adapter3);
                Intent results = new Intent(Search.this, SearchResult.class);
                startActivity(results);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                e = APICalls.getObjectsByDate(date, currentUser);
                p = APICalls.getAllDamaged(currentUser);
                if(e != null) {
                    String[] s = new String[e.length];
                    if(p != null){
                        for(int i = 0; i < e.length; i++){
                            for(int j = 0; j < p.length; j++){
                                if(e[i].getId() == p[j].getId()){
                                    e[i] = null;
                                }
                            }
                        }
                    }
                    for (int i = 0, j =0; i < e.length; i++) {
                        if(e[i] != null) {
                            s[j] = e[i].getBarcode();
                            j++;
                        }
                    }

                    adapter3 = new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, s);
                } else{
                    String[] s = {"No objects"};
                    adapter3 = new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, s);
                }
                return null;

            }
        };

        byDate.execute();
    }

    private void getByDateIndiv() {
        AsyncTask<Void, Void, Void> byDateIndiv = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
                if(shared.size() == 0) {
                    String[] s = {"No objects"};
                    adapter3 = new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, s);
                }
                selIndiv = null;
                SearchResult.getAdapter(adapter3);
                Intent results = new Intent(Search.this, SearchResult.class);
                startActivity(results);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                e = APICalls.getObjectsByDate(date, currentUser);
                i = APICalls.getObjsAttachedToInd(selIndiv, currentUser);
                damaged = APICalls.getAllDamaged(currentUser);
                if(e != null && i != null) {
                    for(int p = 0; p < e.length; p++){
                        for(int q = 0; q < i.length; q++){
                            if(e[p].getId() == i[q].getId()){
                                shared.add(e[p]);
                            }
                        }
                    }
                    String[] s = new String[shared.size()];
                    for (int i = 0; i < shared.size(); i++) {
                        s[i] = shared.get(i).getBarcode();
                    }
                    adapter3 = new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, s);
                }
                return null;

            }
        };

        byDateIndiv.execute();
    }

    private void getByDateProj() {
        AsyncTask<Void, Void, Void> byDateProj = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
                if(shared.size() == 0) {
                    String[] s = {"No objects"};
                    adapter3 = new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, s);
                }
                selProj = null;
                SearchResult.getAdapter(adapter3);
                Intent results = new Intent(Search.this, SearchResult.class);
                startActivity(results);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                e = APICalls.getObjectsByDate(date, currentUser);
                p = APICalls.getObjsAttachedToProj(selProj, currentUser);
                if(e != null && p != null) {
                    for(int i = 0; i < e.length; i++){
                        for(int q = 0; q < p.length; q++){
                            if(e[i].getId() == p[q].getId()){
                                shared.add(e[i]);
                            }
                        }
                    }
                    String[] s = new String[shared.size()];
                    for (int i = 0; i < shared.size(); i++) {
                        s[i] = shared.get(i).getBarcode();
                    }
                    adapter3 = new ArrayAdapter<String>(Search.this, android.R.layout.simple_list_item_1, s);
                }
                return null;

            }
        };

        byDateProj.execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    EditText endDate;
    String date;
    // add project code
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_search_date ){
            endDate = (EditText) findViewById(R.id.search_date);
            date = endDate.getText().toString();
            if(date != null) {
                getByDate();
            }
        } else if(v.getId() == R.id.button_search_indiv_date){
            if(selIndiv != null) {
                endDate = (EditText) findViewById(R.id.search_date_indiv);
                date = endDate.getText().toString();
                if(date != null) {
                    getByDateIndiv();
                }
            }
        } else if(v.getId() == R.id.button_search_project_date){
            if(selProj != null) {
                endDate = (EditText) findViewById(R.id.search_date_project);
                date = endDate.getText().toString();
                if (date != null) {
                    getByDateProj();
                }
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
                        Intent intent = new Intent(Search.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(Search.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(Search.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(Search.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(Search.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });


    }



}
