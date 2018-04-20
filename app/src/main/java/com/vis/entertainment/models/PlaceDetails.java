package com.vis.entertainment.models;



import com.google.android.gms.maps.model.LatLng;

public class PlaceDetails {

    private LatLng latLng;

    private String placeId;

    private String address;

    private String name;

    private String phoneNo;

    private String priceLevel;

    private String rating;

    private String websiteUri;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(String websiteUri) {
        this.websiteUri = websiteUri;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlaceDetails{");
        sb.append("placeId='").append(placeId).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", phoneNo='").append(phoneNo).append('\'');
        sb.append(", priceLevel='").append(priceLevel).append('\'');
        sb.append(", rating='").append(rating).append('\'');
        sb.append(", websiteUri='").append(websiteUri).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
