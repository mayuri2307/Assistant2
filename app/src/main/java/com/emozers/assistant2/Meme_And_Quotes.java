package com.emozers.assistant2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Random;

public class Meme_And_Quotes extends AppCompatActivity
{
    private String MOOD="";
    @Override
    public void onBackPressed()
    {
        Intent i=new Intent(Meme_And_Quotes.this,Rating_Activity.class);
        startActivity(i);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme__and__quotes);
        Intent i = getIntent();
        MOOD= i.getStringExtra("Mood");
        Random rand = new Random();
        int random_index = rand.nextInt(5);
        ImageView iv=(ImageView)findViewById(R.id.iv_for_memes);
        final int[] memes={R.drawable.meme1,R.drawable.meme2,R.drawable.meme3,R.drawable.meme4,R.drawable.meme5};
        final int[] quotes={R.drawable.quote1,R.drawable.quote2,R.drawable.quote3,R.drawable.quote4,R.drawable.quote5};
        if(MOOD.equalsIgnoreCase("happiness")||MOOD.equalsIgnoreCase("neutral"))
        {
            iv.setImageResource(quotes[random_index]);
        }
        else
        {
            iv.setImageResource(memes[random_index]);
        }
    }
}
