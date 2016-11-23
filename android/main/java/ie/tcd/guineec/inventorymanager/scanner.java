package ie.tcd.guineec.inventorymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class scanner extends AppCompatActivity implements OnClickListener {
    private Button scanBtn;
    private TextView formatTxt,contentTxt;
    String scanner_content, scanner_format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_screen);

        scanBtn = (Button) findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt =(TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(this);

    }
    //respond to clicks
    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
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
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
            scanner_content=scanContent;
            scanner_format=scanFormat;
            useContent.getContent(scanContent);
            Intent useContent = new Intent(scanner.this, useContent.class);
            startActivity(useContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data recieved!", Toast. LENGTH_SHORT);
            toast.show();
        }
    }


}
