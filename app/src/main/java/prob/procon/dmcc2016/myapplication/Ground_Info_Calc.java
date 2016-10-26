package prob.procon.dmcc2016.myapplication;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import static java.lang.Math.abs;

/**
 * Created by Maeda Hiromu on 2016/10/25.
 */

public class Ground_Info_Calc {

    private Ground_Tile_Info_Control gtic;

    public Ground_Info_Calc(String path){
        gtic = new Ground_Tile_Info_Control(path);
    }

    public double elevation_num(LatLng location){
        return gtic.Ground_Tile_Access(location);
    }

    public double calc_altitude_difference(LatLng loc1, LatLng loc2){
        double altitude1 = gtic.Ground_Tile_Access(loc1);
        double altitude2 = gtic.Ground_Tile_Access(loc2);
        return abs(altitude1 - altitude2);
    }
}
