package com.example.anujsharma.yoblunt_exoplayer2.adapters;

import android.content.Context;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.anujsharma.yoblunt_exoplayer2.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
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

public class DisplayVideosAdapter extends RecyclerView.Adapter<DisplayVideosAdapter.ViewHolder>{

    private static String TAG = "myErrors";
    private Context context;
    private ArrayList<String> dataUrls;
    private SimpleExoPlayer player;

    public DisplayVideosAdapter(Context context, ArrayList<String> dataUrls) {
        this.context = context;
        this.dataUrls = dataUrls;

        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String videoUrl = dataUrls.get(position);

        holder.simpleExoPlayerView.setUseController(false);
        holder.simpleExoPlayerView.requestFocus();
        Log.d(TAG, "inside OnBindView");

        holder.simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.simpleExoPlayerView.setPlayer(player);

                if (player.getPlaybackState() == PlaybackState.STATE_PLAYING) {
                    player.setPlayWhenReady(false);
                } else {
                    PostVideoBitmapWorkerTask task = new PostVideoBitmapWorkerTask(holder.simpleExoPlayerView, player);
                    task.execute("dummy URL");
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleExoPlayerView simpleExoPlayerView;

        public ViewHolder(View itemView) {
            super(itemView);

            simpleExoPlayerView = new SimpleExoPlayerView(context);
            simpleExoPlayerView = (SimpleExoPlayerView) itemView.findViewById(R.id.pvPlayerView);


        }

    }


    public class PostVideoBitmapWorkerTask extends AsyncTask<String, Void, Void> {

        private String videoUrl;
        private SimpleExoPlayerView simpleExoPlayerView;
        private SimpleExoPlayer player;

        public PostVideoBitmapWorkerTask(SimpleExoPlayerView simpleExoPlayerView, SimpleExoPlayer player) {

            this.player = player;
        }

        @Override
        protected Void doInBackground(String... params) {
            videoUrl = params[0];
            ////////////////////////////////////////////////////////////////////////////////////////////////

            Log.d(TAG, "starting doinBackground");
            Uri mp4VideoUri =Uri.parse("http://playertest.longtailvideo.com/adaptive/bbbfull/bbbfull.m3u8");

            DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "yoblunt_exoplayer2"), bandwidthMeterA);

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
            Log.d(TAG, "inside onPostExecute");
            player.setPlayWhenReady(true);
        }
    }
}
