package com.example.anujsharma.yoblunt_exoplayer2.dataStructures;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DataUrls implements Parcelable {
    public static final Creator<DataUrls> CREATOR = new Creator<DataUrls>() {
        @Override
        public DataUrls createFromParcel(Parcel in) {
            return new DataUrls(in);
        }

        @Override
        public DataUrls[] newArray(int size) {
            return new DataUrls[size];
        }
    };
    public ArrayList<String> dataUrls;
    public Context context;
    public int dataCount;

    public DataUrls(Context context, int dataCount) {
        this.context = context;
        this.dataCount = dataCount;
    }

    protected DataUrls(Parcel in) {
        dataUrls = in.createStringArrayList();
        dataCount = in.readInt();
    }

    public ArrayList<String> getDataUrls() {
        dataUrls = new ArrayList<>();
        //String uriPath = "http://playertest.longtailvideo.com/adaptive/bbbfull/bbbfull.m3u8";
        String uriPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        for (int i = 0; i < dataCount; i++) {
            dataUrls.add(uriPath);
        }
        return dataUrls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(dataUrls);
        dest.writeInt(dataCount);
    }
}
