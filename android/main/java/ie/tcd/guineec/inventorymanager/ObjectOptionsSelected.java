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
import android.widget.TextView;

/**
 * Created by dywal on 12/12/2016.
 */

public class ObjectOptionsSelected extends AppCompatActivity implements View.OnClickListener {
    //side bar vars
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //variable for the sidebar to change screens
    NavigationView navigationView;
    private TextView setDamaged=null;
    private static Equipment currentObj=null;
    private Button markAsDmged, markAsFixed;
    private Individual indivRes=null;
    private TextView setIndivId=null, setProjId=null, setProjName=null, setIndivName=null;
    private Project projUsing=null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_options_display_obj_details);

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
                        Intent intent = new Intent(ObjectOptionsSelected.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(ObjectOptionsSelected.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(ObjectOptionsSelected.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_search:
                        Intent intent4 = new Intent(ObjectOptionsSelected.this, Search.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(ObjectOptionsSelected.this, MainActivity.class);
                        startActivity(intent5);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function
        setProjId = (TextView) findViewById(R.id.project_idShow);
        setProjName = (TextView) findViewById(R.id.project_name);
        setIndivId = (TextView) findViewById(R.id.individual_res_id);
        setIndivName = (TextView) findViewById(R.id.individual__res_name);

        setDamaged=(TextView) findViewById(R.id.is_damaged);
        markAsDmged = (Button) findViewById(R.id.damagedBtn);
        markAsDmged.setOnClickListener(this);
        markAsFixed = (Button) findViewById(R.id.notDamagedBtn);
        markAsFixed.setOnClickListener(this);

        String damaged="";
        if (currentObj.getDamaged()) {
            damaged = "Object is damaged";
        }
        else {
            damaged = "Object is not damaged";
        }

        setDamaged.setText(damaged);
        setIndivRes();
        getProjUsing();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.damagedBtn) {
            final AsyncTask<Void, Void, Void> setDamagedObj = new AsyncTask() {
                @Override
                protected void onPostExecute(Object o) {
                    setDamaged.setText("Object is damaged.");
                }

                @Override
                protected Object doInBackground(Object[] objects) {
                    APICalls.markAsDamaged(currentObj,MainActivity.getUser());
                    return null;
                }
            };
            setDamagedObj.execute();
        }
        else if (v.getId() == R.id.notDamagedBtn) {
            setDamaged.setText("Object not damaged.");
            final AsyncTask<Void, Void, Void> setNotDamagedObj = new AsyncTask() {
                @Override
                protected void onPostExecute(Object o) {
                    setDamaged.setText("Object is not damaged.");
                }

                @Override
                protected Object doInBackground(Object[] objects) {
                    APICalls.markAsFixed(currentObj,MainActivity.getUser());
                    return null;
                }
            };
            setNotDamagedObj.execute();
        }
    }

    public static void setObjectSelected(Equipment objSelected){
        currentObj=objSelected;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setIndivRes(){
        AsyncTask<Void,Void,Void> getindiv_responsible=new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                if(indivRes!=null) {
                    setIndivId.setText("Individual Id: "+ indivRes.getId());
                    setIndivName.setText("Individual Name: "+indivRes.getName());
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                indivRes=APICalls.getIndResponsibleFor(currentObj, MainActivity.getUser());
                return null;
            }
        };
        getindiv_responsible.execute();
    }

    private void getProjUsing(){
        AsyncTask<Void,Void,Void> getProject=new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                if(projUsing!=null) {
                    setProjId.setText("Project Id: "+ projUsing.getId());
                    setProjName.setText("Project Id: "+ projUsing.getName());
                }
                else
                {
                    setProjId.setText("Project Id: NOT ATTACHED TO A PROJECT");
                    setProjName.setText("Project Name: NOT ATTACHED TO A PROJECT");
                }
            }
            @Override
            protected Object doInBackground(Object[] objects) {
                projUsing=APICalls.getProjUsing(currentObj, MainActivity.getUser());
                return null;
            }
        };
        getProject.execute();
    }
}


