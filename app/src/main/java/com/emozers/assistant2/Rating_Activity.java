package com.emozers.assistant2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Rating_Activity extends AppCompatActivity
{
    RatingBar rt;
    String rating;
    boolean interested;
    String username;
    boolean engaging;
    String activity_selected;
    String hobby;
    DatabaseReference mDatabaseReference,ref;
    public void Call(View v)
    {
        // This function is called when button is clicked.
        // Display ratings, which is required to be converted into string first.
        TextView t = (TextView)findViewById(R.id.textView2);
        rating=String.valueOf(rt.getRating());
        t.setText("You Rated :"+String.valueOf(rt.getRating()));
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();
        ref=mDatabaseReference.child("Users").child(username).child("information");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                HashMap<String,user_information> hn=new HashMap<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    user_information user=snapshot.getValue(user_information.class);
                    hn.put(snapshot.getKey(),user);
                }
                Log.d("checking","Length="+hn.size());
                for(Map.Entry<String,user_information> it:hn.entrySet())
                {
                    if(it.getValue().gethobby()!=null)
                    {
                        hobby=it.getValue().gethobby();
                        Log.d("hooby",hobby+" "+activity_selected);
                        if(hobby.equalsIgnoreCase(activity_selected))
                            interested=true;
                        else
                            interested=false;
                    }
                }
                mDatabaseReference.child("Users").child(username).child("Review").push().setValue(new review(engaging,rating,activity_selected,interested));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        //mDatabaseReference.child("Users").child(username).child("Review").push().setValue(new review(engaging,rating,activity_selected,interested));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        //binding MainActivity.java with activity_main.xml file
        rt = (RatingBar) findViewById(R.id.ratingBar);
        Intent i=getIntent();

        activity_selected=i.getStringExtra("Activity");
        username=i.getStringExtra("Username");
        engaging=i.getBooleanExtra("Engaging",false);
        //finding the specific RatingBar with its unique ID
        LayerDrawable stars=(LayerDrawable)rt.getProgressDrawable();
        //Use for changing the color of RatingBar
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
    }
}
