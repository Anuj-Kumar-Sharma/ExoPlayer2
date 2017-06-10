package com.example.anujsharma.yoblunt_exoplayer2.dataStructures;

import android.content.Context;

import java.util.ArrayList;

public class DataUrls {
    public ArrayList<String> dataUrls;
    public Context context;
    public int dataCount;

    public DataUrls(Context context, int dataCount) {
        this.context = context;
        this.dataCount = dataCount;
    }
    public ArrayList<String> getDataUrls() {
        dataUrls = new ArrayList<>();
        //String uriPath = "http://playertest.longtailvideo.com/adaptive/bbbfull/bbbfull.m3u8";
        //String uriPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        String uriPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        for (int i = 0; i < dataCount; i++) {
            dataUrls.add(uriPath);
        }
        return dataUrls;
    }

}
