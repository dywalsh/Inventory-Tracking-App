package ie.tcd.guineec.inventorymanager;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class ObjsByDate extends AppCompatActivity {
    private ListView list;
    private EditText date;
    private String d;
    private User currentUser = MainActivity.getUser();
    private ArrayAdapter<String> adapter;
    private Equipment[] equipments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objs_by_date);
        date = (EditText) findViewById(R.id.dateObjsByDate);
        date.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                d = date.getText().toString();
                getObjects();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
    }

    private void getObjects(){
        AsyncTask<Void, Void, Void> getProj = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o){
                if(equipments != null) {
                    list = (ListView) findViewById(R.id.objsDateList);
                    list.setAdapter(adapter);
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                equipments = APICalls.getObjectsByDate(d, currentUser);
                if(equipments != null) {
                    String[] s = new String[equipments.length];

                    for (int i = 0; i < equipments.length; i++) {
                        if(equipments[i].getDescription() != null) {
                            s[i] = equipments[i].getDescription();
                        } else {
                            s[i] = "Object " + (i + 1);
                        }

                    }

                    adapter = new ArrayAdapter<String>(ObjsByDate.this, android.R.layout.simple_list_item_1, s);
                }
                return null;
            }
        };
        getProj.execute();
    }
}
