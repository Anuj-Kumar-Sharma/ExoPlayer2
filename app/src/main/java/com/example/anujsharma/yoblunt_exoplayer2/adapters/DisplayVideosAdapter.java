package com.example.anujsharma.yoblunt_exoplayer2.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anujsharma.yoblunt_exoplayer2.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class DisplayVideosAdapter extends RecyclerView.Adapter<DisplayVideosAdapter.ViewHolder>{

    private static String TAG = "myErrors";
    public int playPosition = -1;
    private Context context;
    private ArrayList<String> dataUrls;
    private SimpleExoPlayer player;

    public DisplayVideosAdapter(Context context, SimpleExoPlayer player, ArrayList<String> dataUrls) {
        this.context = context;
        this.dataUrls = dataUrls;
        this.player = player;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String videoUrl = dataUrls.get(position);

        holder.simpleExoPlayerView.setUseController(false);
        holder.simpleExoPlayerView.requestFocus();
        Log.d(TAG, "inside OnBindView");

        holder.simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (playPosition == position) {
                            if (player.getPlayWhenReady()) {
                                holder.ivPlayVideo.setVisibility(View.VISIBLE);
                                player.setPlayWhenReady(false);
                            } else {
                                holder.ivPlayVideo.setVisibility(View.GONE);
                                player.setPlayWhenReady(true);
                            }
                        } else {
                            playPosition = position;
                            holder.ivPlayVideo.setVisibility(View.GONE);
                            holder.simpleExoPlayerView.setPlayer(player);
                            PostVideoBitmapWorkerTask task = new PostVideoBitmapWorkerTask(holder.simpleExoPlayerView, player);
                            task.execute("dummy URL");
                        }
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
        private ImageView ivPlayVideo;

        public ViewHolder(View itemView) {
            super(itemView);

            simpleExoPlayerView = new SimpleExoPlayerView(context);
            simpleExoPlayerView = (SimpleExoPlayerView) itemView.findViewById(R.id.pvPlayerView);
            ivPlayVideo = (ImageView) itemView.findViewById(R.id.ivPlayVideo);
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
                    Log.d(TAG, playbackState + " " + playWhenReady);

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
