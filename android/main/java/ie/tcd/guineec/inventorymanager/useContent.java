package ie.tcd.guineec.inventorymanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by dywal on 20/11/2016.
 */

public class useContent extends AppCompatActivity {
    private TextView textviewResult;
    static String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_screen);

        if(content!=null && !content.isEmpty())
        {
            textviewResult=(TextView)findViewById(R.id.result);
            textviewResult.setText(content);
        }
    }


    public static void getContent(String contents){
        content=contents;
    }
}

