package prob.procon.dmcc2016.myapplication;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static prob.procon.dmcc2016.myapplication.MapsActivity.RESULT_SUBACTIVITY;

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
    private String mount_name;

    Button add_button;
    Button camera_button;
    Button twitter_OAuth_button;
    Button twitter_tweet_button;

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
        mount_name = intent.getStringExtra("Mount_name");

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

        twitter_OAuth_button = (Button)findViewById(R.id.twitter_oauth_button);
        twitter_OAuth_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("Twitter", "OAuth認証");

                Intent intent = new Intent(getApplication(), TwitterOAuthActivity.class);

                startActivity(intent);
            }
        });

        twitter_tweet_button = (Button)findViewById(R.id.twitter_button);
        twitter_tweet_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("Twitter", "Call Tweet Activity");
                if(Twitter_Util.hasAccessToken(getApplicationContext())) {
                    Intent intent = new Intent(getApplication(), Tweet_Activity.class);
                    intent.putExtra("Mount_name" , mount_name);
                    startActivity(intent);
                } else{
                    showToast("OAuth認証がされていません！");
                }
            }
        });

        TextView tv = (TextView)findViewById(R.id.OAuth_permisson_text);
        if(Twitter_Util.hasAccessToken(this)) {
            tv.setText("認証済み");
        } else{
            tv.setText("未認証");
        }
    }

    private void showToast(String text){ Toast.makeText(this, text, Toast.LENGTH_SHORT).show(); }
}
