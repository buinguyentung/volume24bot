package com.blogspot.pyimlife.firebasefilestoretut;

public class CEntity {

    public String volume_usd;
    public String available_supply;
    public String updated_date;
    public String id;
    public String price_btc;
    public String price_usd;
    public String percent_change_24h;

    public CEntity(String volume_usd, String available_supply, String updated_date, String id, String price_btc, String price_usd, String percent_change_24h) {
        this.volume_usd = volume_usd;
        this.available_supply = available_supply;
        this.updated_date = updated_date;
        this.id = id;
        this.price_btc = price_btc;
        this.price_usd = price_usd;
        this.percent_change_24h = percent_change_24h;
    }

    public CEntity() {
        // Default receiving data from firebase
    }

}
