package prob.procon.dmcc2016.myapplication;

/**
 * Created by Maeda Hiromu on 2016/10/25.
 */

public class Ground_Info {
    private double altitude_difference;
    private double inclination;
    private double slope;
    private double distance;
    private double finally_alt_diff;
    private double latitude;
    private double longitude;

    public Ground_Info(double altitude_difference, double slope, double finally_alt_diff,
                       double distance , double inclination, double latitude, double longitude){
        this.altitude_difference = altitude_difference;
        this.slope = slope;
        this.finally_alt_diff = finally_alt_diff;
        this.distance = distance;
        this.inclination = inclination;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double returnAltirude_difference(){
        return altitude_difference;
    }
    public double returnSlope(){
        return slope;
    }

    public double returnFinally_alt_diff(){
        return finally_alt_diff;
    }
    public double returnDistance(){
        return distance;
    }
    public double returnInclination(){
        return inclination;
    }
    public double returnLatitude(){
        return latitude;
    }
    public double returnLongitude(){
        return longitude;
    }
}
