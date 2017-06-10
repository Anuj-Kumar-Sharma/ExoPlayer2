package com.example.anujsharma.yoblunt_exoplayer2.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anujsharma.yoblunt_exoplayer2.R;
import com.example.anujsharma.yoblunt_exoplayer2.activities.ExoPlayer2Activity;
import com.example.anujsharma.yoblunt_exoplayer2.dataStructures.VideoData;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayVideosAdapter extends RecyclerView.Adapter<DisplayVideosAdapter.ViewHolder>{

    private Context context;
    private VideoData singleVideoData;
    private ArrayList<String> dataUrls;
    private static String TAG = "myErrors";

    public DisplayVideosAdapter(Context context, ArrayList<String> dataUrls) {
        this.context = context;
        this.dataUrls = dataUrls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String videoPath = dataUrls.get(position);
        try {
            Log.d(TAG, "43");
            singleVideoData = retrieveVideoData(videoPath);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        holder.ivVideoFrame.setImageBitmap(singleVideoData.getVideoFrame());
        holder.tvDuration.setText(singleVideoData.getDuration());
        holder.tvCaption.setText(singleVideoData.getCaption());
        holder.tvDate.setText(singleVideoData.getDate());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, ExoPlayer2Activity.class);
                intent.putExtra("videoUrl", singleVideoData.getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private ImageView ivVideoFrame;
        private TextView tvDate, tvCaption, tvDuration;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ivVideoFrame = (ImageView) itemView.findViewById(R.id.ivVideoFrame);
            tvDate = (TextView) itemView.findViewById(R.id.tvVideoDate);
            tvCaption = (TextView) itemView.findViewById(R.id.tvVideoCaption);
            tvDuration = (TextView) itemView.findViewById(R.id.tvVideoDuration);
        }
    }

    public static VideoData retrieveVideoData(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        String url, duration, date, caption;
        VideoData videoData;
        Log.d(TAG, "inside Video Data method");
        Log.d(TAG, videoPath);
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14) {
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                Log.d(TAG, "inside if statement");
            }
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
            url = videoPath;
            duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            date = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
            caption = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            Log.d(TAG, duration+date+caption+url);
            videoData = new VideoData(url, date, duration, bitmap, caption);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return videoData;
    }
}
