package com.jasonkurniadj.moviecatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {
    private int itemID;
    private String itemExternalID, itemType;
    private String itemPoster, itemTrailer, itemName, itemDescription, itemRating, itemReleaseDate;

    public ItemModel() {}

    protected ItemModel(Parcel in) {
        itemID = in.readInt();
        itemExternalID = in.readString();
        itemType = in.readString();
        itemPoster = in.readString();
        itemTrailer = in.readString();
        itemName = in.readString();
        itemDescription = in.readString();
        itemRating = in.readString();
        itemReleaseDate = in.readString();
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemExternalID() {
        return itemExternalID;
    }

    public void setItemExternalID(String itemExternalID) {
        this.itemExternalID = itemExternalID;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemPoster() {
        return itemPoster;
    }

    public void setItemPoster(String itemPoster) {
        this.itemPoster = itemPoster;
    }

    public String getItemTrailer() {
        return itemTrailer;
    }

    public void setItemTrailer(String itemTrailer) {
        this.itemTrailer = itemTrailer;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemRating() {
        return itemRating;
    }

    public void setItemRating(String itemRating) {
        this.itemRating = itemRating;
    }

    public String getItemReleaseDate() {
        return itemReleaseDate;
    }

    public void setItemReleaseDate(String itemReleaseDate) {
        this.itemReleaseDate = itemReleaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemID);
        dest.writeString(itemExternalID);
        dest.writeString(itemType);
        dest.writeString(itemPoster);
        dest.writeString(itemTrailer);
        dest.writeString(itemName);
        dest.writeString(itemDescription);
        dest.writeString(itemRating);
        dest.writeString(itemReleaseDate);
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };
}
