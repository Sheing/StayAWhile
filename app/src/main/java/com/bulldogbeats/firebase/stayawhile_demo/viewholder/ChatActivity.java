package com.bulldogbeats.firebase.stayawhile_demo.viewholder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bulldogbeats.firebase.stayawhile_demo.R;
import com.bulldogbeats.firebase.stayawhile_demo.SignInActivity;
import com.bulldogbeats.firebase.stayawhile_demo.models.ChatMessage;
import com.bulldogbeats.firebase.stayawhile_demo.models.Chatroom;
import com.bulldogbeats.firebase.stayawhile_demo.utility.ChatroomListAdapter;
import com.bulldogbeats.firebase.stayawhile_demo.utility.DeleteChatroomDialog;
import com.bulldogbeats.firebase.stayawhile_demo.utility.NewChatRoomDialog;
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

/**
 * Created by StanlyNg on 12/3/17.
 */

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    //widgets
    private ListView mListView;
    private FloatingActionButton mFob;


    //vars
    private ArrayList<Chatroom> mChatrooms;
    private ChatroomListAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chat);
        mListView = (ListView) findViewById(R.id.listView);
        mFob = (FloatingActionButton) findViewById(R.id.fob);

        init();

    }

    public void init(){

        getChatrooms();

        /*mFob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewChatRoomDialog dialog = new NewChatRoomDialog();
                dialog.show(getSupportFragmentManager(), getString(R.string.dialog_new_chatroom));
            }
        });*/

    }

    private void setupChatroomList(){
        Log.d(TAG, "setupChatroomList: setting up chatroom listview");
        mAdapter = new ChatroomListAdapter(ChatActivity.this, R.layout.layout_chatroom_listitem, mChatrooms);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: selected chatroom: " + mChatrooms.get(i).toString());
                Intent intent = new Intent(ChatActivity.this, ChatroomActivity.class);
                intent.putExtra(getString(R.string.intent_chatroom), mChatrooms.get(i));
                startActivity(intent);
            }
        });

    }

    private void getChatrooms(){
        Log.d(TAG, "getChatrooms: retrieving chatrooms from firebase database.");
        mChatrooms = new ArrayList<>();
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
                mAdapter = new ChatroomListAdapter(ChatActivity.this, R.layout.layout_chatroom_listitem, mChatrooms);
                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d(TAG, "onItemClick: selected chatroom: " + mChatrooms.get(i).toString());
                        Intent intent = new Intent(ChatActivity.this, ChatroomActivity.class);
                        intent.putExtra(getString(R.string.intent_chatroom), mChatrooms.get(i));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showDeleteChatroomDialog(String chatroomId){
        DeleteChatroomDialog dialog = new DeleteChatroomDialog();
        Bundle args = new Bundle();
        args.putString(getString(R.string.field_chatroom_id), chatroomId);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog_delete_chatroom));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState(){
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(ChatActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }


}












