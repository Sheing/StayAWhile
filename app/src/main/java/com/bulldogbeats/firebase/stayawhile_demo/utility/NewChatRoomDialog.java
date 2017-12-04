package com.bulldogbeats.firebase.stayawhile_demo.utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bulldogbeats.firebase.stayawhile_demo.R;
import com.bulldogbeats.firebase.stayawhile_demo.models.ChatMessage;
import com.bulldogbeats.firebase.stayawhile_demo.models.Chatroom;
import com.bulldogbeats.firebase.stayawhile_demo.viewholder.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by StanlyNg on 12/3/17.
 */

public class NewChatRoomDialog extends DialogFragment {

    private static final String TAG = "NewChatroomDialog";

    private SeekBar mSeekBar;
    private EditText mChatroomName;
    private TextView mCreateChatroom, mSecurityLevel;
    private int mUserSecurityLevel;
    private int mSeekProgress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_chatroom, container, false);
        mChatroomName = (EditText) view.findViewById(R.id.input_chatroom_name);
        //mSeekBar = (SeekBar) view.findViewById(R.id.input_security_level);
        mCreateChatroom = (TextView) view.findViewById(R.id.create_chatroom);
        //mSecurityLevel = (TextView) view.findViewById(R.id.security_level);
        //mSeekProgress = 0;
        //mSecurityLevel.setText(String.valueOf(mSeekProgress));
        //getUserSecurityLevel();

        mCreateChatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mChatroomName.getText().toString().equals("")){
                    Log.d(TAG, "onClick: creating new chat room");


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        //get the new chatroom unique id
                        String chatroomId = reference
                                .child(getString(R.string.dbnode_chatrooms))
                                .push().getKey();

                        //create the chatroom
                        Chatroom chatroom = new Chatroom();
                        //chatroom.setSecurity_level(String.valueOf(mSeekBar.getProgress()));
                        chatroom.setChatroom_name(mChatroomName.getText().toString());
                        chatroom.setCreator_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        chatroom.setChatroom_id(chatroomId);


                        //insert the new chatroom into the database
                        reference
                                .child(getString(R.string.dbnode_chatrooms))
                                .child(chatroomId)
                                .setValue(chatroom);

                        //create a unique id for the message
                        String messageId = reference
                                .child(getString(R.string.dbnode_chatrooms))
                                .push().getKey();

                        //insert the first message into the chatroom
                        ChatMessage message = new ChatMessage();

                        message.setMessage("Welcome to the new chatroom!");
                        message.setTimestamp(getTimestamp());
                        reference
                                .child(getString(R.string.dbnode_chatrooms))
                                .child(chatroomId)
                                .child(getString(R.string.field_chatroom_messages))
                                .child(messageId)
                                .setValue(message);
                        ((ChatActivity)getActivity()).init();
//                    Intent intent;
//                    intent = new Intent(context, ChatActivity.class);
//                    startActivity(intent);
                        getDialog().dismiss();


                }

            }
        });


        return view;
    }
/*
    private void getUserSecurityLevel(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child(getString(R.string.dbnode_users))
                .orderByKey()
                //OR could use ->.orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot:  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: users security level: "
                            + singleSnapshot.getValue(User.class).getSecurity_level());

                    mUserSecurityLevel = Integer.parseInt(String.valueOf(
                            singleSnapshot.getValue(User.class).getSecurity_level()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Return the current timestamp in the form of a string
     * @return
     */
    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }

}
