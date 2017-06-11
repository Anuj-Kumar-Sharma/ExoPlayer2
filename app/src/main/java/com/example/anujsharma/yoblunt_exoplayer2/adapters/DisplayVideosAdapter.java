package com.example.anujsharma.yoblunt_exoplayer2.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
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
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayVideosAdapter extends RecyclerView.Adapter<DisplayVideosAdapter.ViewHolder>{

    private Context context;
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
        final String videoUrl = dataUrls.get(position);

        PostVideoBitmapWorkerTask task = new PostVideoBitmapWorkerTask(holder.simpleExoPlayerView);
        task.execute(videoUrl);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent  = new Intent(context, ExoPlayer2Activity.class);
                intent.putExtra("videoUrl", videoUrl);
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private SimpleExoPlayerView simpleExoPlayerView;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            simpleExoPlayerView = (SimpleExoPlayerView) itemView.findViewById(R.id.pvPlayerView);
        }
    }


    public class PostVideoBitmapWorkerTask extends AsyncTask<String, Void, Void> {

        private String videoUrl;
        private SimpleExoPlayerView simpleExoPlayerView;
        private SimpleExoPlayer player;

        public PostVideoBitmapWorkerTask(SimpleExoPlayerView simpleExoPlayerView) {


            Handler mainHandler = new Handler();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            this.simpleExoPlayerView = new SimpleExoPlayerView(context);
            this.simpleExoPlayerView = simpleExoPlayerView;

            this.simpleExoPlayerView.setUseController(true);
            this.simpleExoPlayerView.requestFocus();

            this.simpleExoPlayerView.setPlayer(player);
        }

        @Override
        protected Void doInBackground(String... params) {
            videoUrl = params[0];
            ////////////////////////////////////////////////////////////////////////////////////////////////

            Uri mp4VideoUri =Uri.parse("http://playertest.longtailvideo.com/adaptive/bbbfull/bbbfull.m3u8");

            DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "exoplayer2example"), bandwidthMeterA);

            MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
            final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);

            player.prepare(loopingSource);


            player.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Log.v(TAG,"Listener-onLoadingChanged...");

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Log.v(TAG,"Listener-onPlayerStateChanged...");

                }

                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {
                    Log.v(TAG,"Listener-onTimelineChanged...");

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Log.v(TAG,"Listener-onPlayerError...");
                    player.stop();
                    player.prepare(loopingSource);
                    player.setPlayWhenReady(true);
                }

                @Override
                public void onPositionDiscontinuity() {
                    Log.v(TAG,"Listener-onPositionDiscontinuity...");

                }
            });


            /////////////////////////////////////////////////////////////////////////////////////////////////
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            player.setPlayWhenReady(true);
        }

        public VideoData retrieveVideoData(String videoPath) throws Throwable
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
}
