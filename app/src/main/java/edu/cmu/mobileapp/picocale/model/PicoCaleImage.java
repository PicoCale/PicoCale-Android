package edu.cmu.mobileapp.picocale.model;

/**
 * Created by srikrishnan_suresh on 07/31/2015.
 */
public class PicoCaleImage {

    public PicoCaleImage(String filePath) {
        this(filePath, 0.0, 0.0);
    }

    public PicoCaleImage(String filePath, double latitude, double longitude) {
        this.filePath = filePath;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private String filePath;
    double latitude;
    double longitude;
}
