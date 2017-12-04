package com.bulldogbeats.firebase.stayawhile_demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bulldogbeats.firebase.stayawhile_demo.R;

import static android.content.ContentValues.TAG;

/**
 * Created by StanlyNg on 12/3/17.
 */

public class ChatFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat,container,false);
        Log.d(TAG, "FRAGMENT@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return view;
    }
}
