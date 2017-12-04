package com.bulldogbeats.firebase.stayawhile_demo.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bulldogbeats.firebase.stayawhile_demo.MainActivity;
import com.bulldogbeats.firebase.stayawhile_demo.R;
import com.bulldogbeats.firebase.stayawhile_demo.SignInActivity;
import com.bulldogbeats.firebase.stayawhile_demo.models.ChatMessage;
import com.bulldogbeats.firebase.stayawhile_demo.models.Chatroom;
import com.bulldogbeats.firebase.stayawhile_demo.utility.ChatroomListAdapter;
import com.bulldogbeats.firebase.stayawhile_demo.viewholder.ChatActivity;
import com.bulldogbeats.firebase.stayawhile_demo.viewholder.ChatroomActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by StanlyNg on 12/3/17.
 */

public class ChatFragment extends Fragment {
    private ListView mListView;
    private ArrayList<Chatroom> mChatrooms;
    private ChatroomListAdapter mAdapter;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat,container,false);
        goToAttract(view);
        //setContentView(R.layout.activity_chat);
        //mListView = view.findViewById(R.id.listView2);
        //getChatrooms();
        return view;
    }
    public void goToAttract(View v){
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        startActivity(intent);
    }

    private void getChatrooms(){
        Log.d(TAG, "getChatrooms: retrieving chatrooms from firebase database.");
        mChatrooms = new ArrayList<Chatroom>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child(getString(R.string.dbnode_chatrooms));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot:  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found chatroom: "
                            + singleSnapshot.getValue());

                    Chatroom chatroom = new Chatroom();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    chatroom.setChatroom_id(objectMap.get(getString(R.string.field_chatroom_id)).toString());
                    chatroom.setChatroom_name(objectMap.get(getString(R.string.field_chatroom_name)).toString());
                    chatroom.setCreator_id(objectMap.get(getString(R.string.field_creator_id)).toString());
                    //chatroom.setSecurity_level(objectMap.get(getString(R.string.field_security_level)).toString());

//                    chatroom.setChatroom_id(singleSnapshot.getValue(Chatroom.class).getChatroom_id());
//                    chatroom.setSecurity_level(singleSnapshot.getValue(Chatroom.class).getSecurity_level());
//                    chatroom.setCreator_id(singleSnapshot.getValue(Chatroom.class).getCreator_id());
//                    chatroom.setChatroom_name(singleSnapshot.getValue(Chatroom.class).getChatroom_name());

                    //get the chatrooms messages
                    ArrayList<ChatMessage> messagesList = new ArrayList<ChatMessage>();
                    for(DataSnapshot snapshot: singleSnapshot
                            .child(getString(R.string.field_chatroom_messages)).getChildren()){
                        ChatMessage message = new ChatMessage();
                        message.setTimestamp(snapshot.getValue(ChatMessage.class).getTimestamp());
                        message.setUser_id(snapshot.getValue(ChatMessage.class).getUser_id());
                        message.setMessage(snapshot.getValue(ChatMessage.class).getMessage());
                        messagesList.add(message);
                    }
                    chatroom.setChatroom_messages(messagesList);
                    mChatrooms.add(chatroom);
                    Log.d(TAG, "onDataChange: found chatroom: "+mChatrooms.toString());
                    Log.d(TAG, "onDataChange: found chatroom: "+mChatrooms.size());
                    //setupChatroomList();
                }
                setupChatroomList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState(){
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(context, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //finish();
        }else{
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }

    private void setupChatroomList(){

        Log.d(TAG, "setupChatroomList: setting up chatroom listview");
        Log.d(TAG, "Inside SETUP----->"+mChatrooms.toString());
        mAdapter = new ChatroomListAdapter(context, R.layout.layout_chatroom_listitem, mChatrooms);
        Log.d(TAG, "Inside SETUP----->"+mAdapter.getCount());
        Log.d(TAG, "Inside SETUP----->"+mAdapter.getContext().toString());

        mListView.setAdapter(mAdapter);

    }
}
