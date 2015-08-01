package edu.cmu.mobileapp.picocale.model;

import edu.cmu.mobileapp.picocale.constants.PicoCaleImageConstants;

/**
 * Created by srikrishnan_suresh on 07/31/2015.
 */
public class PicoCaleImage {


    public PicoCaleImage(String string, int type, double latitude, double longitude) {
        if(type == PicoCaleImageConstants.DEVICE_IMAGE)
            this.filePath = string;
        else if(type == PicoCaleImageConstants.URL_IMAGE)
            this.urlPath = string;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type=type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    private String urlPath;
    double latitude;
    double longitude;
    int type;
}
