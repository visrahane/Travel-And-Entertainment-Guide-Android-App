package com.vis.entertainment.models;

public class Review {

    private String authorName;
    private String date;
    private String text;
    private String rating;
    private String photoUrl;
    private String authorUrl;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Review{");
        sb.append("authorName='").append(authorName).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", rating='").append(rating).append('\'');
        sb.append(", photoUrl='").append(photoUrl).append('\'');
        sb.append(", authorUrl='").append(authorUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

}
