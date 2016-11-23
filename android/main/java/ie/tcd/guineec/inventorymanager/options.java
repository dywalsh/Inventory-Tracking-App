package ie.tcd.guineec.inventorymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);

        Button scanner=(Button) findViewById(R.id.scan_button);
        scanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(options.this, scanner.class);
                startActivity(intent);
            }
        });

        Button addIndividual=(Button) findViewById(R.id.add_indiv_button);
        addIndividual.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(options.this, add_indiv.class);
                startActivity(intent);
            }
        });
    }
}