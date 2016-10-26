package prob.procon.dmcc2016.myapplication;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Maeda Hiromu on 2016/10/25.
 */

public class Ground_Tile_Info_Control {
    private String mount_data;
    private double[] lat = new double[2];
    private double[] border = new double[2];
    private double[] lon = new double[2];

    public Ground_Tile_Info_Control(String mount_name){
        if(mount_name.equals("Onanji")){
            mount_data = "Ground_Info/FG-GML-5737";
            lat[0] = 36.5;
            lat[1] = 36.666666667;
            lon[0] = 137.6;
            lon[1] = 137.75;
            border[0] = 36.583333333;
            border[1] = 137.625;
        }
    }
    public double Ground_Tile_Access(LatLng location){
        String path_num;
        if(location.latitude < lat[1] && location.latitude > lat[0]
                && location.longitude < lon[1] && location.longitude > lon[0]){
            if(location.latitude > border[0]){
                if(location.longitude < border[1]){
                    int path = tile_num(location, lat[1],border[1], border[0], lon[0]);
                    path_num = String.valueOf(path);
                    Ground_Tile_FileLoad gtf = new Ground_Tile_FileLoad(mount_data+"-"+74+"/"+path_num+".txt");
                    return gtf.elevation_info(location);
                }
                else{
                    int path = tile_num(location, lat[1],lon[1], border[0], border[1]);
                    path_num = String.valueOf(path);
                    Ground_Tile_FileLoad gtf = new Ground_Tile_FileLoad(mount_data+"-"+75+"/"+path_num+".txt");
                    return gtf.elevation_info(location);
                }
            }
            else{
                if(location.longitude < border[1]){
                    int path = tile_num(location, border[0],border[1], lat[0], lon[0]);
                    path_num = String.valueOf(path);
                    Ground_Tile_FileLoad gtf = new Ground_Tile_FileLoad(mount_data+"-"+64+"/"+path_num+".txt");
                    return gtf.elevation_info(location);
                }
                else {
                    int path = tile_num(location, border[0],lon[1], lat[0], border[1]);
                    path_num = String.valueOf(path);
                    Ground_Tile_FileLoad gtf = new Ground_Tile_FileLoad(mount_data+"-"+65+"/"+path_num+".txt");
                    return gtf.elevation_info(location);
                }
            }
        }
        else return -9999;
    }



    public int tile_num(LatLng location, double max_lat, double max_lon, double min_lat, double min_lon){
        int y = (int)((location.latitude-min_lat)/(max_lat-min_lat))*10;
        int x = (int)((location.longitude-min_lon)/(max_lon-min_lon))*10;
        return y*10+x;
    }
}
