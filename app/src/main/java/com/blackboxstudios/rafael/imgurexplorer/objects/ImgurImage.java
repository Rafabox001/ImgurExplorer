package com.blackboxstudios.rafael.imgurexplorer.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by darkgeat on 21/07/15.
 */
public class ImgurImage implements Parcelable{
    private String title;
    private String id;
    private String description;
    private String link;
    private long ups;
    private long downs;
    private long points;
    private long score;
    private String is_album;

    public ImgurImage(String title, String id, String description, String link, long ups, long downs, long points, long score, String is_album) {
        super();
        this.title = title;
        this.id = id;
        this.description = description;
        this.link = link;
        this.ups = ups;
        this.downs = downs;
        this.points = points;
        this.score = score;
        this.is_album = is_album;
    }

    public ImgurImage(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getUps() {
        return ups;
    }

    public void setUps(long ups) {
        this.ups = ups;
    }

    public long getDowns() {
        return downs;
    }

    public void setDowns(long downs) {
        this.downs = downs;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getIs_album() {
        return is_album;
    }

    public void setIs_album(String is_album) {
        this.is_album = is_album;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeLong(ups);
        dest.writeLong(downs);
        dest.writeLong(points);
        dest.writeLong(score);
        dest.writeString(is_album);
    }

    public static final Parcelable.Creator<ImgurImage> CREATOR
            = new Parcelable.Creator<ImgurImage>() {
        public ImgurImage createFromParcel(Parcel in) {
            return new ImgurImage(in);
        }

        public ImgurImage[] newArray(int size) {
            return new ImgurImage[size];
        }
    };

    public ImgurImage(Parcel in) {

        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        title = in.readString();
        id = in.readString();
        description = in.readString();
        link = in.readString();
        ups = in.readLong();
        downs = in.readLong();
        points = in.readLong();
        score = in.readLong();
        is_album = in.readString();
    }
}
