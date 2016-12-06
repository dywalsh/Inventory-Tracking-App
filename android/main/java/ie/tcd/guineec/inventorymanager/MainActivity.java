package ie.tcd.guineec.inventorymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent switchToMyProjects = new Intent(MainActivity.this, Myprojects.class);
        startActivity(switchToMyProjects);
    }
}
