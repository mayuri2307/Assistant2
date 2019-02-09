package com.emozers.assistant2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity
{
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent i = getIntent();Log.d("abcd123","value is");
        //Bundle bundle = getIntent().getExtras();
        String value1= i.getStringExtra("Neutral_Mood");
        //String value1 = bundle.getString("Neutral_Mood");
        Log.d("abcd123",value1);
        tv=(TextView)(findViewById(R.id.Mood));
        tv.setText(String.valueOf(value1));

    }
}