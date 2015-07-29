package edu.cmu.mobileapp.picocale.util;

/**
 * Created by Jayakumaur on 28-07-2015.
 */
public class DistanceUtils {
    public static double getLatitudeCorrectionFactor(double distance){
        double earthRadius = 3960.0;
        double degrees_to_radians = 3.14/180.0;
        double radians_to_degrees = 180.0/Math.PI;
        return (distance/earthRadius)*radians_to_degrees;
    }

    public static double getLongitudeCorrectionFactor(double distance,double latitude){
        double modifiedRadius,earthRadius = 3960.0;
        double degrees_to_radians = 3.14/180.0;
        double radians_to_degrees = 180.0/Math.PI;
        modifiedRadius = earthRadius*Math.cos(latitude*degrees_to_radians);
        return (distance/modifiedRadius)*radians_to_degrees;
    }
}
