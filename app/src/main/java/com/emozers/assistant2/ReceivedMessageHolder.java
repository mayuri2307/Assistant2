package com.emozers.assistant2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sendbird.android.UserMessage;

public class ReceivedMessageHolder extends RecyclerView.ViewHolder {

    TextView messageText, timeText, nameText;

    ImageView profileImage;



    ReceivedMessageHolder(View itemView) {

        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.text_message_body);

        timeText = (TextView) itemView.findViewById(R.id.text_message_time);

        nameText = (TextView) itemView.findViewById(R.id.text_message_name);

        profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);

    }



    void bind(UserMessage message) {

        messageText.setText(message.getMessage());



        // Format the stored timestamp into a readable String using method.

        //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));

        nameText.setText(message.getSender().getNickname());



        // Insert the profile image from the URL into the ImageView.

        //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);

    }

}