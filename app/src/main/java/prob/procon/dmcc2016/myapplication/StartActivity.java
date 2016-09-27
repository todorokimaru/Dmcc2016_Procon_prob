package prob.procon.dmcc2016.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StartActivity extends AppCompatActivity {

    private String[] mStr_Mount = {"呉羽山","大汝山"};

    private final String TABLE_NAME = "sql_makers";
    private final String FIELD_ID = "_id";
    private final String FIELD_MOUNT_NAME = "mount_name";
    private final String FIELD_DATE = "date";
    private final String FIELD_INFO_TYPE = "info_type";
    private final String FIELD_USER_ID = "user_id";
    private final String FIELD_COMMENT = "comment";
    private final String FIELD_GRAPH = "graph";

    private final String CREATE_TABLE_SQL = "CREATE TABLE "+TABLE_NAME+" ( "+FIELD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FIELD_MOUNT_NAME+" TEXT NOT NULL, "
                                                +FIELD_DATE+" TEXT NOT NULL, "+FIELD_INFO_TYPE+" INTEGER, "+FIELD_USER_ID+" TEXT NOT NULL, "+FIELD_COMMENT+" TEXT, "
                                                +FIELD_GRAPH+" TEXT);";

    private Spinner nSpinner;

    private String select_mount_name;
    private String select_mount_func;
    private int  select_mount_x;
    private int  select_mount_y;
    private int zoom;

    private static MakerHelper singleton;

    Mount_Location mount_loc = new Mount_Location();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        nSpinner = (Spinner)findViewById(R.id.spinner);

        MakerHelper hlpr = new MakerHelper(getApplicationContext(), CREATE_TABLE_SQL);
        SQLiteDatabase mydb = hlpr.getWritableDatabase();

        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStr_Mount);
        nSpinner.setAdapter(adapter);

        nSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner)adapterView;
                String item = (String)spinner.getSelectedItem();
                if(item.equals("呉羽山")){
                    select_mount_name = item;
                    select_mount_func = "Kureha";
                }
                else if(item.equals("大汝山")){
                    select_mount_name = item;
                    select_mount_func = "Onanji";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                starting_app(v);
            }
        });
    }

    private void starting_app(View v){

        zoom = 15;

        Intent intent = new Intent(getApplication(), MapsActivity.class);
        intent.putExtra("Mount_name", select_mount_name);
        intent.putExtra("Mount_x", mount_loc.locate_x(select_mount_func));
        intent.putExtra("Mount_y", mount_loc.locate_y(select_mount_func));
        intent.putExtra("zoom", zoom);

        startActivity(intent);
    }

}
