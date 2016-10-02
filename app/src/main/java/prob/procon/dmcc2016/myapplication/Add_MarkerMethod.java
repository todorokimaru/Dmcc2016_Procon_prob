package prob.procon.dmcc2016.myapplication;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Maeda Hiromu on 2016/10/02.
 */

public class Add_MarkerMethod {
    public void add_Marker_maps(LatLng location, int info_type, GoogleMap mMap, List Comment_List, List mMarker_List, String date, String user_id, String comment) {
        MarkerOptions options = new MarkerOptions();
        options.position(location);
        options.title(user_id+date);

        BitmapDescriptor icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_denger_resize);
        if (info_type == 0)
            icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_denger_animal);
        else if (info_type == 1)
            icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_denger_stonefall);
        else if (info_type == 2)
            icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_denger_srip);
        else if (info_type == 3)
            icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_spot_resize);
        options.icon(icon_over);
        Marker marker = mMap.addMarker(options);

        mMarker_List.add(marker);
        Comment_List.add(new MarkerInfo(date, info_type, user_id, comment, ""));
    }

    public void add_Marker_user(LatLng location, int info_type, GoogleMap mMap, List Comment_List, List mMarker_AddUser,List mMarker_List,  String date, String user_id, String comment) {
        MarkerOptions options = new MarkerOptions();
        options.position(location);
        options.title(user_id+date);

        BitmapDescriptor icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_denger_resize);
        if (info_type == 0)
            icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_denger_animal);
        else if (info_type == 1)
            icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_denger_stonefall);
        else if (info_type == 2)
            icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_denger_srip);
        else if (info_type == 3)
            icon_over = BitmapDescriptorFactory.fromResource(R.drawable.pin_spot_resize);
        options.icon(icon_over);
        Marker marker = mMap.addMarker(options);

        mMarker_AddUser.add(marker);
        mMarker_List.add(marker);
        Comment_List.add(new MarkerInfo(date, info_type, user_id, comment, ""));
    }
}
