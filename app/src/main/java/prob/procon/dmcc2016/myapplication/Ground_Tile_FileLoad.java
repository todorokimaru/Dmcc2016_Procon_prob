package prob.procon.dmcc2016.myapplication;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Maeda Hiromu on 2016/10/26.
 */

public class Ground_Tile_FileLoad {

    private double mesh_num;
    private double[] min_location = new double[2];
    private double[] max_location = new double[2];
    private int[] end_point = new int[2];
    private double[][] ground_ele;

    public Ground_Tile_FileLoad(String path){
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            mesh_num = Double.parseDouble(br.readLine());
            min_location[0] = Double.parseDouble(br.readLine());
            min_location[1] = Double.parseDouble(br.readLine());
            max_location[0] = Double.parseDouble(br.readLine());
            max_location[1] = Double.parseDouble(br.readLine());
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
            br.close();
            file.exists();
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        catch(IOException e){

        }
    }

    public double elevation_info(LatLng location){
        int x,y;
        y = end_point[1] - (int)((location.latitude-min_location[1])
                /(max_location[1]-min_location[1]))*end_point[1];
        x = (int)((location.longitude-max_location[0])
                /(max_location[0]-min_location[0]))*end_point[0];
        return ground_ele[y][x];
    }
    public double[] elevation_info_4(LatLng location){
        int x,y;
        y = end_point[1] - (int)((location.latitude-min_location[1])
                /(max_location[1]-min_location[1]))*end_point[1];
        x = (int)((location.longitude-max_location[0])
                /(max_location[0]-min_location[0]))*end_point[0];
        double[] ground_info = new double[5];
        ground_info[0] = ground_ele[y][x];
        ground_info[1] = ground_ele[y-1][x];
        ground_info[2] = ground_ele[y][x+1];
        ground_info[3] = ground_ele[y+1][x];
        ground_info[4] = ground_ele[y][x-1];
        return ground_info;
    }
}
