package com.blogspot.pyimlife.firebasefilestoretut;

public class CEntity {

    public String Timestamp;
    public String bValue;
    public String uValue;

    public CEntity() {
        // Default receiving data from firebase
    }

    public CEntity(String timestamp, String bValue, String uValue) {
        this.Timestamp = timestamp;
        this.bValue = bValue;
        this.uValue = uValue;
    }

}
