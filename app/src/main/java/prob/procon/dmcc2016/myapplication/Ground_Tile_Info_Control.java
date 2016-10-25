package prob.procon.dmcc2016.myapplication;

import android.location.Location;

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
    private double[] lat;
    private double[] border;
    private double[] lon;

    private double mesh_num;
    private double[] start_location;
    private double[] end_location;
    private int[] end_point;
    private double[][] ground_ele;

    public Ground_Tile_Info_Control(String mount_name){
        if(mount_name.equals("Onanji")){
            mount_data = "FG-GML-5737";
            lat[0] = 36.5;
            lat[1] = 36.666666667;
            lon[0] = 137.6;
            lon[1] = 137.75;
            border[0] = 36.583333333;
            border[1] = 137.625;
        }
    }
    public double Ground_Tile_Access(Location location){
        String path_num;
        if(location.getLatitude() < lat[1] && location.getLatitude() > lat[0]
                && location.getLongitude() < lon[1] && location.getLongitude() > lon[0]){
            if(location.getLatitude() > border[0]){
                if(location.getLongitude() < border[1]){
                    int path = tile_num(location, lat[1],border[1], border[0], lon[0]);
                    path_num = String.valueOf(path);
                    Ground_Tile_Elevation(mount_data+"-"+64+"-"+path_num+".txt");
                    return elevation_info(location);
                }
                else{
                    int path = tile_num(location, lat[1],border[1], border[0], lon[0]);
                    path_num = String.valueOf(path);
                    Ground_Tile_Elevation(mount_data+"-"+64+"-"+path_num+".txt");
                    return elevation_info(location);

                }
            }
            else{
                if(location.getLongitude() < border[1]){
                    int path = tile_num(location, lat[1],border[1], border[0], lon[0]);
                    path_num = String.valueOf(path);
                    Ground_Tile_Elevation(mount_data+"-"+64+"-"+path_num+".txt");
                    return elevation_info(location);

                }
                else {
                    int path = tile_num(location, lat[1],border[1], border[0], lon[0]);
                    path_num = String.valueOf(path);
                    Ground_Tile_Elevation(mount_data+"-"+64+"-"+path_num+".txt");
                    return elevation_info(location);

                }
            }
        }
        else return -9999;
    }

    public void Ground_Tile_Elevation(String path){
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            mesh_num = Double.parseDouble(br.readLine());
            start_location[0] = Double.parseDouble(br.readLine());
            start_location[1] = Double.parseDouble(br.readLine());
            end_location[0] = Double.parseDouble(br.readLine());
            end_location[1] = Double.parseDouble(br.readLine());
            end_point[0] = Integer.parseInt(br.readLine());
            end_point[1] = Integer.parseInt(br.readLine());
            ground_ele = new double[end_point[0]][end_point[1]];
            if(br.readLine().equals("Data_Start")){
                for(int i = 0; i <= end_point[1]; i++){
                    for(int j = 0; j <= end_point[0]; j++){
                        String str = br.readLine();
                        if(!str.equals("Data_End")){
                            ground_ele[i][j] = Double.parseDouble(str);
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        catch(IOException e){

        }
    }

    public double elevation_info(Location location){
        int x,y;
        y = (int)((location.getLatitude()-start_location[1])
                /(start_location[1]-end_location[1]))*end_point[1];
        x = (int)((location.getLatitude()-end_location[0])
                /(end_location[0]-start_location[0]))*end_point[0];
        return ground_ele[y][x];
    }

    public int tile_num(Location location, double max_lat, double max_lon, double min_lat, double min_lon){
        int y = (int)((location.getLatitude()-min_lat)/(max_lat-min_lat))*10;
        int x = (int)((location.getLongitude()-min_lon)/(max_lon-min_lon))*10;
        return y*10+x;
    }
}
