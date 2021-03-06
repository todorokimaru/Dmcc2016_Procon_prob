package prob.procon.dmcc2016.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class StartActivity extends AppCompatActivity {

    private String[] mStr_Mount = {"呉羽山","大汝山"};

    private Spinner nSpinner;
    private TCP_Client_Thread tcp_client_thread;
    private GetUrlPicture getUrlPicture;
    private String IP = "153.126.176.44";
    private int Port = 4000;

    private String select_mount_name;
    private String select_mount_func;
    private int  select_mount_x;
    private int  select_mount_y;
    private int zoom;
    private String User_id = "Dummy_User";
    private String mount_str;

    private static MakerHelper mDBHelper;

    Mount_Location mount_loc = new Mount_Location();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tcp_client_thread = new TCP_Client_Thread();
        getUrlPicture = new GetUrlPicture();
        nSpinner = (Spinner)findViewById(R.id.spinner);

        mDBHelper = MakerHelper.getInstance(getApplicationContext());
        mDBHelper.getWritableDatabase();

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
        findViewById(R.id.button_DL).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mapData_DL();
            }
        });
    }

    private void starting_app(View v){

        zoom = 15;

        EditText edit = (EditText)findViewById(R.id.editText);
        SpannableStringBuilder sp = (SpannableStringBuilder)edit.getText();

        Intent intent = new Intent(getApplication(), MapsActivity.class);
        intent.putExtra("Mount_name", select_mount_name);
        intent.putExtra("Mount_name_db", select_mount_func);
        intent.putExtra("Mount_x", mount_loc.locate_x(select_mount_func));
        intent.putExtra("Mount_y", mount_loc.locate_y(select_mount_func));
        intent.putExtra("zoom", zoom);
        intent.putExtra("User_id", sp.toString());

        startActivity(intent);
    }

    private void mapData_DL(){
        if(tcp_client_thread.connectTh(IP, Port)){
            Log.d("TCP", "Connection");
            byte[] send = (select_mount_func+","+User_id+",0").getBytes();
            tcp_client_thread.sendTh(send);
            byte[] data;
            data = tcp_client_thread.recvTh(2000);
            String request_url = data.toString();
            getUrlPicture.saveFile(request_url, 10, 902, 399, select_mount_func, getApplicationContext());
        }
    }
}
