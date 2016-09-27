package prob.procon.dmcc2016.myapplication;

import android.content.Intent;
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

public class AddLocation_Info extends AppCompatActivity {

    private String[] mStr_Info_Type = {"野生生物", "落石","滑落"};
    private String[] mStr_Location = {"タップした位置", "現在地"};

    private double latitude_tap;
    private double longitude_tap;
    private double latitude_user;
    private double longitude_user;

    private double select_latitude;
    private double select_longitude;
    private int Info_type;

    private Spinner InfoSpinner;
    private Spinner LocSpinner;

    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location__info);

        InfoSpinner = (Spinner)findViewById(R.id.info_type_spinner);
        LocSpinner = (Spinner)findViewById(R.id.location_spinner);

        ArrayAdapter<String> adapter_info_type
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStr_Info_Type);
        InfoSpinner.setAdapter(adapter_info_type);

        ArrayAdapter<String> adapter_loc
                = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStr_Location);
        LocSpinner.setAdapter(adapter_loc);

        Intent intent = getIntent();
        latitude_tap = intent.getDoubleExtra("Latitude_Tap", 0.0);
        longitude_tap = intent.getDoubleExtra("Longitude_Tap", 0.0);
        latitude_user = intent.getDoubleExtra("Latitude_User", 0.0);
        longitude_user = intent.getDoubleExtra("Longitude_User", 0.0);

        InfoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                if(item.equals("野生生物")){
                    Info_type = 0;
                }
                else if(item.equals("落石")){
                    Info_type = 1;
                }
                else if(item.equals("滑落")){
                    Info_type = 2;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                if(item.equals("タップした位置")){
                    select_latitude = latitude_tap;
                    select_longitude = longitude_tap;
                }
                else if(item.equals("現在地")){
                    select_latitude = latitude_user;
                    select_longitude = longitude_user;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_button = (Button)findViewById(R.id.marker_add_button);
        add_button.setOnClickListener(new View.OnClickListener(){

            EditText edit = (EditText)findViewById(R.id.editText_comment);
            SpannableStringBuilder sp = (SpannableStringBuilder)edit.getText();

            @Override
            public void onClick(View v) {
                Log.d("Info_type", String.valueOf(Info_type));
                Log.d("Comment", sp.toString());
                Intent intent = new Intent();
                intent.putExtra("Info_Type", Info_type);
                intent.putExtra("Latitude", select_latitude);
                intent.putExtra("Longitude", select_longitude);
                intent.putExtra("Comment", sp.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}
