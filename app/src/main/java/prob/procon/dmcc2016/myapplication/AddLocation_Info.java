package prob.procon.dmcc2016.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddLocation_Info extends AppCompatActivity {

    private String[] mStr_Info_Type = {"野生生物", "落石","滑落"};
    private String[] mStr_Location = {"タップした位置", "現在地"};
    private double latitude_tap;
    private double longitude_tap;
    private double latitude_user;
    private double longitude_user;
    private double higher_user;
    private double select_latitude;
    private double select_longitude;
    private double select_higher;
    private int Info_type;
    private String date_str;
    private Spinner InfoSpinner;
    private int REQUEST_GALLERY = 1000;
    private Spinner LocSpinner;
    private String user_id;
    private Bitmap bmp;
    private ImageView imgView;
    private File imageFile;
    private String imageName;
    private boolean image_flag = false;

    private Date info_date;
    Button add_button;
    Button camera_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location__info);

        info_date = new Date();

        imgView = (ImageView)findViewById(R.id.imageView_location) ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-kkmmss");
        date_str = sdf.format(info_date);
        imageName = user_id+sdf.format(info_date)+".png";


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
        higher_user = intent.getDoubleExtra("Higher_User", 0.0);
        user_id = intent.getStringExtra("User_id");

        imageName = user_id+sdf.format(info_date)+".png";

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

        add_button = (Button)findViewById(R.id.marker_add_button);
        add_button.setOnClickListener(new View.OnClickListener(){

            EditText edit = (EditText)findViewById(R.id.editText_comment);
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
                if(image_flag)
                    intent.putExtra("Image", imageFile.getAbsolutePath());
                intent.putExtra("Img_flag", image_flag);
                intent.putExtra("Date", date_str);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        camera_button = (Button)findViewById(R.id.picture_button);
        camera_button.setOnClickListener(new View.OnClickListener(){

            EditText edit = (EditText)findViewById(R.id.editText_comment);
            SpannableStringBuilder sp = (SpannableStringBuilder)edit.getText();

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                bmp = BitmapFactory.decodeStream(is);
                is.close();
                imgView.setImageBitmap(bmp);
                imageFile = new File(getFilesDir(), imageName);
                FileOutputStream out;
                try{
                    out = new FileOutputStream(imageFile);
                    bmp.compress(Bitmap.CompressFormat.PNG, 0 , out);
                    image_flag = true;
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            } catch (Exception e) {

            }
        }
    }
}
