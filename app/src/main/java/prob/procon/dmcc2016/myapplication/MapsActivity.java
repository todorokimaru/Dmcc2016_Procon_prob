package prob.procon.dmcc2016.myapplication;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * This demonstrates tile overlay coordinates.
 */
public abstract class MapsActivity extends AppCompatActivity implements OnMapReadyCallback ,LocationListener {

    private static final String MAP_URL_FORMAT =
            "http://cyberjapandata.gsi.go.jp/xyz/relief/%d/%d/%d.png";
    private TileOverlay jMaps;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void enableMyLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                 == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        } else {
            //show rationale and request permission
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        TileProvider tileProvider = new UrlTileProvider(256,256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                // The moon tile coordinate system is reversed.  This is not normal.
                String s = String.format(Locale.US, MAP_URL_FORMAT, zoom, x, y);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };

        jMaps = map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
        map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }
}
