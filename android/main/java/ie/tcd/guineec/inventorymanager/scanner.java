package ie.tcd.guineec.inventorymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class scanner extends AppCompatActivity implements OnClickListener {
    //side bar vars
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //variable for the sidebar to change screens
    NavigationView navigationView;

    //scanner vars
    private Button scanSearchBtn,scanAddBtn,scanAssignBtn;
    String scanner_content, scanner_format;

    /*
        integer to save which button was pressed:
        0 = scanner_scanAndAddBtn
        1 = scanner_scanAndAssginBtn
        2 = scanner_scanAndSearchBtn
    */
    private int buttonPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_screen);

        scanSearchBtn = (Button) findViewById(R.id.scanner_scanAndSearchBtn);
        scanAddBtn = (Button) findViewById(R.id.scanner_scanAndAddBtn);
        scanAssignBtn = (Button) findViewById(R.id.scanner_scanAndAssginBtn);

        //scanner listener for when the scan buttons are clicked
        scanSearchBtn.setOnClickListener(this);
        scanAddBtn.setOnClickListener(this);
        scanAssignBtn.setOnClickListener(this);

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
                switch(item.getItemId()){

                    case R.id.nav_projects:
                        Intent intent = new Intent(scanner.this, Myprojects.class);
                        startActivity(intent);
                        item.setChecked(true);
                        break;
                    case R.id.nav_indiv:
                        Intent intent2 = new Intent(scanner.this, add_indiv.class);
                        startActivity(intent2);
                        item.setChecked(true);
                        break;
                    case R.id.nav_scan:
                        Intent intent3 = new Intent(scanner.this, scanner.class);
                        startActivity(intent3);
                        item.setChecked(true);
                        break;
                    case R.id.nav_logout:
                        Intent intent4 = new Intent(scanner.this, MainActivity.class);
                        startActivity(intent4);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });
        //end of sider bar code except onOpitionsItemSelected function


    }
    //respond to clicks
    public void onClick(View v){
        if(v.getId()==R.id.scanner_scanAndAddBtn){
            buttonPressed=0;
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        else if(v.getId()==R.id.scanner_scanAndAssginBtn){
            buttonPressed=1;
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        else if(v.getId()==R.id.scanner_scanAndSearchBtn){
            buttonPressed=2;
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    //retrieve scan result
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            scanner_content=scanContent;
            scanner_format=scanFormat;
            useScannerResult.getContent(scanContent, buttonPressed);

            if(buttonPressed==2)
            {
                ObjectOptions.setBarcode(scanContent);
                Intent switchToViewObjects=new Intent(scanner.this, ObjectOptions.class);
                startActivity(switchToViewObjects);
            }
            else {
                Intent useContent = new Intent(scanner.this, useScannerResult.class);
                startActivity(useContent);
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data recieved!", Toast. LENGTH_SHORT);
            toast.show();
        }
    }
    //side bar function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
