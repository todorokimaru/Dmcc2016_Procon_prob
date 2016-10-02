package prob.procon.dmcc2016.myapplication;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSpot_Info extends AppCompatActivity {

    private String[] mStr_Location = {"タップした位置", "現在地"};

    private double latitude_tap;
    private double longitude_tap;
    private double latitude_user;
    private double longitude_user;
    private double higher_user;
    private double select_latitude;
    private double select_longitude;
    private double select_higher;
    private int Info_type = 3;
    private String date_str;
    private Spinner LocSpinner;
    private Camera mCam = null;
    private CameraPreview mCamPreview = null;
    private Date info_date;

    Button add_button;
    Button camera_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot__info);

        info_date = new Date();
        LocSpinner = (Spinner)findViewById(R.id.spot_spinner);

        ArrayAdapter<String> adapter_loc
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStr_Location);
        LocSpinner.setAdapter(adapter_loc);

        Intent intent = getIntent();
        latitude_tap = intent.getDoubleExtra("Latitude_Tap", 0.0);
        longitude_tap = intent.getDoubleExtra("Longitude_Tap", 0.0);
        latitude_user = intent.getDoubleExtra("Latitude_User", 0.0);
        longitude_user = intent.getDoubleExtra("Longitude_User", 0.0);
        higher_user = intent.getDoubleExtra("Higher_User", 0.0);


        LocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                if(item.equals("タップした位置")){
                    select_latitude = latitude_tap;
                    select_longitude = longitude_tap;
                    select_higher = 0.0f;
                }
                else if(item.equals("現在地")){
                    select_latitude = latitude_user;
                    select_longitude = longitude_user;
                    select_higher = higher_user;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_button = (Button)findViewById(R.id.marker_add_spot);
        add_button.setOnClickListener(new View.OnClickListener(){

            EditText edit = (EditText)findViewById(R.id.editText_comment_spot);
            SpannableStringBuilder sp = (SpannableStringBuilder)edit.getText();

            @Override
            public void onClick(View v) {
                Log.d("Info_type", String.valueOf(Info_type));
                Log.d("Comment", sp.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-kkmmss");

                Intent intent = new Intent();
                intent.putExtra("Info_Type", Info_type);
                intent.putExtra("Latitude", select_latitude);
                intent.putExtra("Longitude", select_longitude);
                intent.putExtra("Higher", select_higher);
                intent.putExtra("Comment", sp.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        camera_button = (Button)findViewById(R.id.picture_button_spot);
        camera_button.setOnClickListener(new View.OnClickListener(){

            EditText edit = (EditText)findViewById(R.id.editText_comment_spot);
            SpannableStringBuilder sp = (SpannableStringBuilder)edit.getText();

            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-kkmmss");
                date_str = sdf.format(info_date);

                Intent intent = new Intent(getApplication(), SimpleCameraActivity.class);
                intent.putExtra("date", date_str);

                startActivity(intent);
            }
        });
    }
}
