package ie.tcd.guineec.inventorymanager;

import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;


/**
 * Created by dywal on 20/11/2016.
 */

//DISPLAYS THE BARCODE VALUE AS A RESULT OF THE BARCODE SCANNER APP
public class useScannerResult extends AppCompatActivity implements OnClickListener {
    /*
       integer to save which operation to execute:
       0 = Add Object To Project, then individual
       1 = Assign Object To Individual
       2 = Search For Object
   */
    private static int operationToExecute;

    //side bar vars
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //variable for the sidebar to change screens
    NavigationView navigationView;
    //test
    TextView displayBarcode;
    private Button addProj,addInd,saveObj;
    //search vars
    int[] respsonsibleId, assignedToId;
    String[] respsonsibleName, assignedToName;

    //after scanning barcode-> object vars
    private Equipment[] equipment = null;
    private static Project project;
    private static Individual individual;
    private String description;
    private static String barcode;

    private EditText descrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_result_screen);

        displayBarcode = (TextView) findViewById(R.id.testView);

        descrip = (EditText) findViewById(R.id.obj_description);

        addInd=(Button)findViewById(R.id.object_add_individual);
        addProj=(Button)findViewById(R.id.object_add_project);
        saveObj=(Button)findViewById(R.id.object_save);

        addInd.setOnClickListener(this);
        addProj.setOnClickListener(this);
        saveObj.setOnClickListener(this);

        if(operationToExecute==2)
        {
            displayBarcode.setText("checking api for barcode...");
            addProj.setVisibility(View.GONE);
            addInd.setVisibility(View.GONE);
            saveObj.setVisibility(View.GONE);
            descrip.setVisibility(View.GONE);

        }
        else if(operationToExecute == 1)
        {
            displayBarcode.setText("Add description and individual.");
            addProj.setVisibility(View.GONE);
            addInd.setVisibility(View.VISIBLE);
            saveObj.setVisibility(View.VISIBLE);
            descrip.setVisibility(View.VISIBLE);
        }
        else
        {
            displayBarcode.setText("Add desciption, individual and project ");
            addInd.setVisibility(View.VISIBLE);
            addProj.setVisibility(View.VISIBLE);
            saveObj.setVisibility(View.VISIBLE);
            descrip.setVisibility(View.VISIBLE);
        }

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
                        Intent intent = new Intent(useScannerResult.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(useScannerResult.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(useScannerResult.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent4 = new Intent(useScannerResult.this, MainActivity.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function
    }

    //side bar function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //To set the scanned barcode value
    public static void getContent(String contents, int buttonPressed) {
        operationToExecute = buttonPressed;
        barcode = contents;
    }


    private void setIndividualRespsonsible() {
        //search for the object in database with this barcode / add to database

                //if add to project
                if (operationToExecute == 0) {
                    //list of projects using

                    AsyncTask<Void, Void, Void> addObj = new AsyncTask() {
                        @Override
                        protected void onPostExecute(Object o) {
                            displayBarcode.setText("Added obj with project");
                        }

                        @Override
                        protected Object doInBackground(Object[] objects) {
                            APICalls.addObject(barcode, individual, project, description, MainActivity.getUser());

                            return null;
                        }
                    };
                    addObj.execute();
                }
                //if no project selected
                else {
                    project = null;
                    AsyncTask<Void, Void, Void> addObj = new AsyncTask() {
                        @Override
                        protected void onPostExecute(Object o) {
                           displayBarcode.setText("Added obj without project");
                        }
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            APICalls.addObject(barcode, individual, project, description, MainActivity.getUser());
                            return null;
                        }
                    };
                    addObj.execute();
                }
    }


    public void onClick(View v) {
        if (v.getId() == R.id.object_add_individual) {
            Intent Switch = new Intent(useScannerResult.this, useScannerResultAddIndividual.class);
            startActivity(Switch);
        } else if (v.getId() == R.id.object_add_project) {
            Intent Switch = new Intent(useScannerResult.this, useScannerResultAddProject.class);
            startActivity(Switch);
        }
        else if(v.getId() == R.id.object_save)
        {
            description=descrip.getText().toString();
            displayBarcode.setText("save clicked" );
            //aslong as the barcode string is not null or empty and an operation has been selected
            if (barcode != null && !barcode.isEmpty()) {
                displayBarcode.setText("attempting to send obj"+description);
                setIndividualRespsonsible();
            }
        }
    }

    public static void setProject(Project selectedProject)
    {
        project=selectedProject;
    }

    public static void setIndividual(Individual selectedIndividual)
    {
        individual=selectedIndividual;
    }
}
