package prob.procon.dmcc2016.myapplication;

import android.*;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Maeda Hiromu on 2016/10/05.
 */

public class BackGroundService extends Service{
    private Timer mTimer = null;
    Handler mHandler = new Handler();
    private MakerHelper mDBHelper;
    private SQLiteDatabase readableDB;
    private LocationManager locationManager;


    @Override
    public void onCreate() {
        Log.i("TestService", "onCreate");

        mDBHelper = MakerHelper.getInstance(getApplicationContext());
        readableDB = mDBHelper.getReadableDatabase();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TestService", "onStartCommand");

        // タイマーの設定 1秒毎にループ
        mTimer = new Timer(true);
        mTimer.schedule( new TimerTask(){
            @Override
            public void run(){
                mHandler.post( new Runnable(){
                    public void run(){
                        Log.d( "TestService" , "Timer run" );
                        Cursor cursor = readableDB.query(DatabaseManager.TABLE_NAME, new String[] {
                                DatabaseManager.FIELD_LOCATION, DatabaseManager.FIELD_DATE, DatabaseManager.FIELD_INFO_TYPE,
                                DatabaseManager.FIELD_COMMENT, DatabaseManager.FIELD_GRAPH
                        }, null, null, null, null, DatabaseManager.FIELD_ID + " DESC");
                        boolean isEof = cursor.moveToFirst();
                        while(isEof) {
                            String[] str = cursor.getString(cursor.getColumnIndex(DatabaseManager.FIELD_LOCATION)).split("-");
                            String date = cursor.getString(cursor.getColumnIndex(DatabaseManager.FIELD_DATE));
                            String user_db = cursor.getString(cursor.getColumnIndex(DatabaseManager.FIELD_USER_ID));
                            String comment = cursor.getString(cursor.getColumnIndex(DatabaseManager.FIELD_COMMENT));
                            double latitude = Double.parseDouble(str[0]);
                            double longitude = Double.parseDouble(str[1]);
                            int info_type = cursor.getInt(cursor.getColumnIndex(DatabaseManager.FIELD_INFO_TYPE));

                            LatLng location = new LatLng(latitude, longitude);
                            Location dblocation = new Location("DB");
                            dblocation.setLatitude(latitude);
                            dblocation.setLongitude(longitude);
                            dblocation.setTime(new Date().getTime());
                            final Location mylocate = locationManager.getLastKnownLocation("gps");

                            float[] results = new float[3];
                            Location.distanceBetween(mylocate.getLatitude(), mylocate.getLongitude(), dblocation.getLatitude(), dblocation.getLongitude(), results);
                            if(results[0] < 10.0f){
                                Log.d("BackGround", "Runnable!!");
                            }
                        }
                    }
                });
            }
        }, 5000, 5000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("TestService", "onDestroy");

        // タイマー停止
        if( mTimer != null ){
            mTimer.cancel();
            mTimer = null;
        }
        Toast.makeText(this, "MyService　onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i("TestService", "onBind");
        return null;
    }
}
