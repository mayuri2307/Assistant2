package com.emozers.assistant2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class video_player extends AppCompatActivity
{
    private int mCurrentPosition = 0;
    boolean engaging;
    String activity_selected;
    String username;
    private static final String PLAYBACK_TIME = "play_time";
    private static final String VIDEO_SAMPLE = "https://developers.google.com/training/images/tacoma_narrows.mp4";
    private VideoView mVideoView;
    private TextView mBufferingTextView;
    @Override
    public void onBackPressed()
    {
        Intent i=new Intent(video_player.this,Rating_Activity.class);
        i.putExtra("Engaging",engaging);
        i.putExtra("Username",username);
        i.putExtra("Activity",activity_selected);
        startActivity(i);
        finish();
    }
    private Uri getMedia(String mediaName)
    {
        if (URLUtil.isValidUrl(mediaName))
        {
            // media name is an external URL
            return Uri.parse(mediaName);
        } else { // media name is a raw resource embedded in the app
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + mediaName);
        }
    }
    private void initializePlayer()
    {
        mBufferingTextView.setVisibility(VideoView.VISIBLE);
        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);
        if (mCurrentPosition > 0)
        {
            mVideoView.seekTo(mCurrentPosition);
        }
        else
        {
            // Skipping to 1 shows the first frame of the video.
            mVideoView.seekTo(1);
        }
        mVideoView.start();
        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener()
                {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer)
                    {
                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                        if (mCurrentPosition > 0) {
                            mVideoView.seekTo(mCurrentPosition);
                        } else {
                            mVideoView.seekTo(1);
                        }

                        mVideoView.start();
                    }
                });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                // Implementation here.
                Toast.makeText(video_player.this, "Playback completed",
                        Toast.LENGTH_SHORT).show();
                mVideoView.seekTo(1);

            }
        });
    }
    private void releasePlayer()
    {
        mVideoView.stopPlayback();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        initializePlayer();
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        releasePlayer();
    }
    @Override
    protected void onPause()
    {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
        {
            mVideoView.pause();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mVideoView = findViewById(R.id.videoview);
        Intent i=getIntent();
        activity_selected=i.getStringExtra("Activity");
        username=i.getStringExtra("Username");
        engaging=i.getBooleanExtra("Engaging",false);
        mBufferingTextView = findViewById(R.id.buffering_textview);
        if (savedInstanceState != null)
        {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);
    }
}
