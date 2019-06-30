package com.emozers.assistant2;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class  ChatListAdapter extends BaseAdapter {
    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;

    private ArrayList<DataSnapshot> mSnapshotList; // datasnapshot is a type that is used by firebase for passing the data back to our app
    // whenever we read the data from the cloud it comes as datasnapshot
    private ChildEventListener mListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
        {
            // it will get fired when a new chat is added to the database. and when this is triggered we will get datasnapshots from the frebase
            Log.d("xyz1234","Data snapshot"+dataSnapshot.getKey());
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged(); // in order to tell the list view that it needs to refresh itself
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    public ChatListAdapter(Activity activity,DatabaseReference ref,String name){
        mActivity=activity;
        mDisplayName=name;
        //Make changes here
        mDatabaseReference=ref.child("Users").child(mDisplayName).child("Messages");//child(mDisplayName);
        mDatabaseReference.addChildEventListener(mListener);
        mSnapshotList=new ArrayList<>();
    }
    static class ViewHolder{  // it will hold all the views in a single row
        TextView authorname;
        TextView body;
        LinearLayout.LayoutParams params;
    }
    @Override
    public int getCount()
    {
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int i)
     {
        DataSnapshot snapshot=mSnapshotList.get(i);
        return snapshot.getValue(InstantMessage.class); // converts the json from snapshot to the instant message object
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            LayoutInflater inflater=(LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.chat_msg_row,viewGroup,false);
            final ViewHolder holder=new ViewHolder();
            holder.authorname=(TextView)view.findViewById(R.id.author);
            holder.body=(TextView)view.findViewById(R.id.message);
            holder.params=(LinearLayout.LayoutParams) holder.authorname.getLayoutParams();
            view.setTag(holder);
        }
        final InstantMessage message=getItem(i);
        final ViewHolder holder =(ViewHolder)view.getTag();
        boolean isMe=message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe,holder);
        String author=message.getAuthor();
        holder.authorname.setText(author); // updating the current vlaues of author and message body
        String msg=message.getMessage();
        holder.body.setText(msg);
        return view;
    }
    // to do the styling
    private void setChatRowAppearance(boolean isItMe,ViewHolder holder){
        if(isItMe)
        {
            holder.params.gravity= Gravity.END;
            holder.authorname.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }
        else{
            holder.params.gravity=Gravity.START;
            holder.authorname.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);

        }
        holder.authorname.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }
    // it has the job of providing the data to the list view
    // t will retrieve data from the firebase and the provide it to the list view
    // t acts as the bridge between the two
    // when the app leaves the foreground we need to stop the adapter from futhur listenning of the events from the firebase database. This freedup resources in te memory
    public void cleanup()
    {
        mDatabaseReference.removeEventListener(mListener);

    }
}
