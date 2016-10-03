package prob.procon.dmcc2016.myapplication;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This demonstrates tile overlay coordinates.
 */
public class MapsActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        CompoundButton.OnCheckedChangeListener,
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowCloseListener {
    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }


        private void render(Marker marker, View view) {

            boolean flag_window = false;
            int window_num = 0;
            String info_Str = "";

            String title = marker.getTitle();
            for(int i = 0; i < Comment_List.size(); i++){
                if((Comment_List.get(i).returnUser_id()+Comment_List.get(i).returnDate()).equals(title)) {
                    flag_window = true;
                    window_num = i;
                }
            }
            if(flag_window == true) {
                if (Comment_List.get(window_num).returnInfo_type() == 0) info_Str = "野生生物";
                else if (Comment_List.get(window_num).returnInfo_type() == 1) info_Str = "落石";
                else if (Comment_List.get(window_num).returnInfo_type() == 2) info_Str = "滑落";
                else if (Comment_List.get(window_num).returnInfo_type() == 3) info_Str = "ベストスポット";
                TextView titleUi = ((TextView) view.findViewById(R.id.Info_type));
                if (title != null) {
                    // Spannable string allows us to edit the formatting of the text.
                    SpannableString titleText = new SpannableString(info_Str);
                    if(info_Str.equals("ベストスポット")){
                        titleText.setSpan(new ForegroundColorSpan(Color.GREEN), 0, titleText.length(), 0);
                    }
                    else {
                        titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                    }
                    titleUi.setText(titleText);
                } else {
                    titleUi.setText("No Info");
                }

                String snippet = marker.getSnippet();
                TextView snippetUi = ((TextView) view.findViewById(R.id.Comment_str));
                if (snippetUi != null) {
                    //SpannableString snippetText = new SpannableString(Comment_List.get(window_num).returnComment());
                    //snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, snippet.length(), 0);
                    snippetUi.setText(Comment_List.get(window_num).returnComment());
                    Log.d("output:comment",Comment_List.get(window_num).returnComment());
                    snippetUi.setTextColor(Color.BLACK);
                } else {
                    snippetUi.setText("No Info");
                }

                TextView user_idUI = ((TextView) view.findViewById(R.id.User_id));
                if (user_idUI != null) {
                    //SpannableString snippetText = new SpannableString(Comment_List.get(window_num).returnUser_id());
                    //snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, user_idUI.length(), 0);
                    user_idUI.setText(Comment_List.get(window_num).returnUser_id());
                    user_idUI.setTextColor(Color.BLACK);
                } else {
                    user_idUI.setText("No Info");
                }
                TextView dateUI = ((TextView) view.findViewById(R.id.Date));
                if (dateUI != null) {
                    //SpannableString snippetText = new SpannableString(Comment_List.get(window_num).returnDate());
                    //snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, dateUI.length(), 0);
                    dateUI.setText(Comment_List.get(window_num).returnDate());
                    dateUI.setTextColor(Color.BLACK);
                    dateUI.setTextSize(10);
                } else {
                    dateUI.setText("No Info");
                }
            }
        }
    }

    private static final String MAP_URL_FORMAT =
            "http://cyberjapandata.gsi.go.jp/xyz/relief/%d/%d/%d.png";

    private TileOverlay jMaps;
    private Marker mLastSelectedMarker;
    private MakerHelper mDBHelper;
    private TCP_Client_Thread tcp_client;
    private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
    private final List<Marker> mMarker_AddUser = new ArrayList<Marker>();
    private final List<Marker> mMarker_List = new ArrayList<Marker>();
    private final List<MarkerInfo> Comment_List = new ArrayList<MarkerInfo>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    static final int RESULT_SUBACTIVITY = 1000;

    private double location_user_x;
    private double location_user_y;
    private int befor_zoom;
    private String mount_name;
    private String mount_name_db;
    private String user_id;
    private TextView mTagText;
    private SQLiteDatabase writableDB;
    private String server_ip;
    private int server_port;
    private Add_MarkerMethod add_marker;
    private CompoundButton switch_info;
    private ToggleButton switch_network;
    private boolean switch_markerInfo;
    private boolean switch_networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDBHelper = MakerHelper.getInstance(getApplicationContext());
        writableDB = mDBHelper.getWritableDatabase();
        tcp_client = new TCP_Client_Thread();

        Intent intent = getIntent();
        mount_name = intent.getStringExtra("Mount_name");
        mount_name_db = intent.getStringExtra("Mount_name_db");
        location_user_x = intent.getDoubleExtra("Mount_x", 0.0);
        location_user_y = intent.getDoubleExtra("Mount_y", 0.0);
        befor_zoom = intent.getIntExtra("zoom", 0);
        user_id = intent.getStringExtra("User_id");

        Log.d("Initialize", "名称は:" + mount_name + "座標はx:" + location_user_x + ", y:" + location_user_y + ", zoom" + befor_zoom);

        mTagText = (TextView) findViewById(R.id.User_id);
        add_marker = new Add_MarkerMethod();
        switch_info = (CompoundButton) findViewById(R.id.switch_marker);
        switch_info.setOnCheckedChangeListener(this);
        switch_network = (ToggleButton)findViewById(R.id.toggle_net);
        switch_network.setOnCheckedChangeListener(this);
        switch_markerInfo = false;
        switch_networkInfo = true;

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void enableMyLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                 != PackageManager.PERMISSION_GRANTED){
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null){
            //show rationale and request permission
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        map.setMapType(GoogleMap.MAP_TYPE_NONE);

        // Add lots of markers to the map.
        //addMarkersToMap();

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);

        if(TCP_Client_Thread.netWorkCheck(this.getApplicationContext()) || switch_networkInfo) {
            Log.d("dsadsa","test");
            TileProvider tileProvider = new UrlTileProvider(256, 256) {
                @Override
                public synchronized URL getTileUrl(int x, int y, int zoom) {
                    // The moon tile coordinate system is reversed.  This is not normal.
                    String s = String.format(Locale.JAPAN, MAP_URL_FORMAT, zoom, x, y);
                    URL url = null;
                    if(switch_networkInfo) {
                        try {
                            url = new URL(s);
                        } catch (MalformedURLException e) {
                            throw new AssertionError(e);
                        }
                    }

                    Log.d("getTile", "座標はx:" + x + ", y:" + y + ", zoom" + zoom);

                    return url;
                }
            };
            jMaps = map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
            map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
        }
        else{
            jMaps = map.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(getResources().getAssets())));
            map.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(getResources().getAssets())));
        }



        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        moveToStartLocotion();

        mMap.setMaxZoomPreference(15);
        mMap.setMinZoomPreference(10);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        final Location mylocate = locationManager.getLastKnownLocation("gps");

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if(switch_markerInfo) {
                    Intent intent1 = new Intent(getApplicationContext(), AddSpot_Info.class);
                    intent1.putExtra("Latitude_Tap", latLng.latitude);
                    intent1.putExtra("Longitude_Tap", latLng.longitude);
                    intent1.putExtra("Latitude_User", mylocate.getLatitude());
                    intent1.putExtra("Longitude_User", mylocate.getLongitude());
                    intent1.putExtra("Higher_User", mylocate.getAltitude());
                    intent1.putExtra("Mount_name", mount_name);
                    int requestCode = RESULT_SUBACTIVITY;
                    startActivityForResult(intent1, requestCode);
                }
                else {
                    Intent intent1 = new Intent(getApplicationContext(), AddLocation_Info.class);
                    intent1.putExtra("Latitude_Tap", latLng.latitude);
                    intent1.putExtra("Longitude_Tap", latLng.longitude);
                    intent1.putExtra("Latitude_User", mylocate.getLatitude());
                    intent1.putExtra("Longitude_User", mylocate.getLongitude());
                    intent1.putExtra("Higher_User", mylocate.getAltitude());
                    int requestCode = RESULT_SUBACTIVITY;
                    startActivityForResult(intent1, requestCode);
                }
            }
        });

        //addMarkersStartUp();
    }

    private void addMarkersStartUp(){
        Cursor cursor = writableDB.query(DatabaseManager.TABLE_NAME, new String[] {
                DatabaseManager.FIELD_LOCATION, DatabaseManager.FIELD_DATE, DatabaseManager.FIELD_INFO_TYPE,
                DatabaseManager.FIELD_COMMENT, DatabaseManager.FIELD_GRAPH
        }, null, null, null, null, DatabaseManager.FIELD_ID + " DESC");
        boolean isEof = cursor.moveToFirst();
        while(isEof){
            String[] str = cursor.getString(cursor.getColumnIndex(DatabaseManager.FIELD_LOCATION)).split("-");
            String date = cursor.getString(cursor.getColumnIndex(DatabaseManager.FIELD_DATE));
            String user_db = cursor.getString(cursor.getColumnIndex(DatabaseManager.FIELD_USER_ID));
            String comment = cursor.getString(cursor.getColumnIndex(DatabaseManager.FIELD_COMMENT));
            double latitude = Double.parseDouble(str[0]);
            double longitude = Double.parseDouble(str[1]);
            int info_type = cursor.getInt(cursor.getColumnIndex(DatabaseManager.FIELD_INFO_TYPE));

            LatLng location = new LatLng(latitude, longitude);

            add_marker.add_Marker_maps(location, info_type, mMap, Comment_List, mMarker_List, date, user_db, comment);
        }
    }
/*
    private void addMarkersToMap() {
        int numMarkersInRainbow = 12;
        for (int i = 0; i < numMarkersInRainbow; i++) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            30 + 10 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)),
                            135 - 10 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
                    .title("Marker " + i)
                    .icon(BitmapDescriptorFactory.defaultMarker(i * 360 / numMarkersInRainbow))
                    .flat(true)
                    .rotation(0));
            mMarkerRainbow.add(marker);
        }
    }
*/


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Markers have a z-index that is settable and gettable.
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(this, marker.getTitle() + " z-index set to " + zIndex,
                Toast.LENGTH_SHORT).show();

        // Markers can store and retrieve a data object via the getTag/setTag methods.
        // Here we use it to retrieve the number of clicks stored for this marker.
        // Check if a click count was set.
        //mTagText.setText(marker.getTitle() + " has been clicked ") ;

        mLastSelectedMarker = marker;
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    protected void moveToStartLocotion(){
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(location_user_y,location_user_x) ,befor_zoom);
        mMap.moveCamera(cu);
    }

    protected void onActivityResult( int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK && requestCode == RESULT_SUBACTIVITY && null != intent) {
            int info_type = intent.getIntExtra("Info_Type", 0);
            double latitude = intent.getDoubleExtra("Latitude",0.0);
            double longitude = intent.getDoubleExtra("Longitude",0.0);
            double higher = intent.getDoubleExtra("Higher", 0.0);
            String comment = intent.getStringExtra("Comment");
            String location_str = latitude + "-"+longitude+"-"+higher;

            // 現在の時刻を取得
            Date date = new Date();
            // 表示形式を設定
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-kkmmss");

            LatLng location = new LatLng(latitude, longitude);

            add_marker.add_Marker_user(location, info_type, mMap, Comment_List, mMarker_AddUser, mMarker_List,sdf.format(date), user_id, comment);

            ContentValues values = new ContentValues();
            values.put(DatabaseManager.FIELD_MOUNT_NAME, mount_name_db);
            values.put(DatabaseManager.FIELD_LOCATION, location_str);
            values.put(DatabaseManager.FIELD_DATE, sdf.format(date));
            values.put(DatabaseManager.FIELD_INFO_TYPE, info_type);
            values.put(DatabaseManager.FIELD_USER_ID, user_id);
            values.put(DatabaseManager.FIELD_COMMENT, comment);
            values.put(DatabaseManager.FIELD_GRAPH, "");
            writableDB.insert(DatabaseManager.TABLE_NAME, null, values);

            if(tcp_client.connectTh(server_ip, server_port)){
                String data_str = mount_name_db +","+location_str+","+sdf.format(date)+","+info_type+","+user_id+","+comment+","+"";
                byte[] data = data_str.getBytes();
                tcp_client.sendTh(data);
                tcp_client.close();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if(buttonView.getId() == R.id.switch_marker) {
            Log.d("switch", "switch has been changed");
            if (isChecked) {
                Log.d("switch", "switch is true");
                for (Marker marker : mMarker_List) {
                    Log.d("switch", "in loop");
                    int marker_num = 0;
                    for (int i = 0; i < Comment_List.size(); i++) {
                        if ((Comment_List.get(i).returnUser_id() + Comment_List.get(i).returnDate()).equals(marker.getTitle())) {
                            marker_num = i;
                        }
                    }
                    Log.d("switch", "true marker_num:" + marker_num);
                    if (Comment_List.get(marker_num).returnInfo_type() == 3) {
                        marker.setVisible(true);
                    } else {
                        marker.setVisible(false);
                    }
                }
                switch_markerInfo = true;
            } else {
                for (Marker marker : mMarker_List) {
                    Log.d("switch", "switch is false");
                    Log.d("switch", "in loop");
                    int marker_num = 0;
                    for (int i = 0; i < Comment_List.size(); i++) {
                        if ((Comment_List.get(i).returnUser_id() + Comment_List.get(i).returnDate()).equals(marker.getTitle())) {
                            marker_num = i;
                        }
                    }
                    Log.d("switch", "false marker_num:" + marker_num);
                    if (Comment_List.get(marker_num).returnInfo_type() == 3) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
                switch_markerInfo = false;
            }
        } else if(buttonView.getId() == R.id.toggle_net){
            Log.d("network", "changed");
            if(isChecked)   switch_networkInfo = true;
            else switch_networkInfo = false;
        }
    }
}
