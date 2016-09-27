package prob.procon.dmcc2016.myapplication;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.tan;

/**
 * Created by Maeda Hiromu on 2016/09/25.
 */

public class Mount_Location {
    public int lon_to_locate_x(String mt_name, int zoom){
        int x ;
        double lon = 0.0f;
        if(mt_name.equals("Kureha"))    lon = 137.183472;
        else if(mt_name.equals("Onanji"))    lon = 137.619592;
        x = (int)(pow(2,zoom)*((lon+180)/360));
        return x;
    }
    public double locate_x(String mt_name){
        double lon = 0.0f;
        if(mt_name.equals("Kureha"))    lon = 137.183472;
        else if(mt_name.equals("Onanji"))    lon = 137.619592;
        return lon;
    }
    public int lan_to_locate_y(String mt_name, int zoom){
        int y ;
        double lat = 0.0f;
        if(mt_name.equals("Kureha"))    lat = 36.711502;
        else if(mt_name.equals("Onanji"))    lat = 36.575910;
        y = (int)(pow(2,zoom)*(1-(log(tan(lat)+(1/cos(lat)))/PI))/2);
        return y;
    }
    public double locate_y(String mt_name){
        double lat = 0.0f;
        if(mt_name.equals("Kureha"))    lat = 36.711502;
        else if(mt_name.equals("Onanji"))    lat = 36.575910;
        return lat;
    }
}
