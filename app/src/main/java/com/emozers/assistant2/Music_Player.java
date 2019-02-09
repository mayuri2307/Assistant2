package com.emozers.assistant2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class Music_Player extends AppCompatActivity implements View.OnClickListener
{
    Button play,pause,stop;
    MediaPlayer mMediaPlayer;
    int pauseCurrentPosition;
    private String MOOD;
    @Override
    public void onBackPressed()
    {
        Intent i=new Intent(Music_Player.this,Rating_Activity.class);
        startActivity(i);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music__player);
        Intent i = getIntent();
        MOOD= i.getStringExtra("Mood");
        play=(Button)findViewById(R.id.btn_play);
        pause=(Button)findViewById(R.id.btn_pause);
        stop=(Button)findViewById(R.id.btn_stop);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        pause.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_play:
                if(mMediaPlayer==null)
                {
                    //mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.all_is_well);

                    Random rand = new Random();
                    int random_index = rand.nextInt(5);
                    if(MOOD.equalsIgnoreCase("sadness")||MOOD.equalsIgnoreCase("disgust")
                            ||MOOD.equalsIgnoreCase("fear"))
                    {
                    switch (random_index)
                    {
                        case 0:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.all_is_well);
                            break;
                        case 1:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dil_dhadakne_do);
                            break;
                        case 2:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hai_junoon);
                            break;
                        case 3:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sooraj_ki_baahon_mein);
                            break;
                        case 4:
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.paathshala);
                            break;

                    }
                    }
                    else
                    {
                        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.paathshala);
                        switch (random_index)
                        {
                            case 0:
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.yun_hi_chala_chal);
                                break;
                            case 1:
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.chor_bazaari);
                                break;
                            case 2:
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.senorita);
                                break;
                            case 3:
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.badal_pe_paon_hai);
                                break;
                            case 4:
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.aap_se_milkar);
                                break;

                        }
                    }
                    mMediaPlayer.start();

                }
                else if(!(mMediaPlayer.isPlaying()))
                {
                    mMediaPlayer.seekTo(pauseCurrentPosition);
                    mMediaPlayer.start();
                }
                break;
            case R.id.btn_pause:
                if(mMediaPlayer!=null)
                {
                    mMediaPlayer.pause();
                    pauseCurrentPosition=mMediaPlayer.getCurrentPosition();
                }
                break;
            case R.id.btn_stop:
                if(mMediaPlayer!=null)
                {
                    mMediaPlayer.stop();
                    mMediaPlayer=null;
                }
                break;
        }
    }
}
