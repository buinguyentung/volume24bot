package com.blogspot.pyimlife.a11_ggcloud;

public class CEntity {
    //public String id;
    public String Timestamp;
    public String bValue;
    public String uValue;

    public CEntity() {
        // Default receiving data from firebase
    }

    public CEntity(String timestamp, String bValue, String uValue) {
        //this.id = id;
        this.Timestamp = timestamp;
        this.bValue = bValue;
        this.uValue = uValue;
    }
}
