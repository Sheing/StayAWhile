/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bulldogbeats.firebase.stayawhile_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bulldogbeats.firebase.stayawhile_demo.fragment.ChatFragment;
import com.bulldogbeats.firebase.stayawhile_demo.fragment.MyPostsFragment;
import com.bulldogbeats.firebase.stayawhile_demo.fragment.MyTopPostsFragment;
import com.bulldogbeats.firebase.stayawhile_demo.fragment.RecentPostsFragment;
import com.bulldogbeats.firebase.stayawhile_demo.utility.NewChatRoomDialog;
import com.bulldogbeats.firebase.stayawhile_demo.utility.UniversalImageLoader;
import com.bulldogbeats.firebase.stayawhile_demo.viewholder.ChatActivity;
import com.bulldogbeats.firebase.stayawhile_demo.viewholder.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;


public class  MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader();
        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPostsFragment(),
                    new MyPostsFragment(),
                    new MyTopPostsFragment(),
                    new ChatFragment(),
            };

            private final String[] mFragmentNames = new String[] {
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_my_top_posts),
                    "Chat Room"
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }

        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewChatRoomDialog dialog = new NewChatRoomDialog();
                dialog.show(getSupportFragmentManager(), "dialog_new_chatroom");
                //for(int j = 0; j<=4; j++){
                    //if(mPagerAdapter.getPageTitle(j)!= "Chat Room") {
                        //startActivity(new Intent(MainActivity.this, NewPostActivity.class));
                    //}else{
                     //   startActivity(new Intent(MainActivity.this, NewPostActivity.class));
                    //}
                //}

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            case R.id.optionAccountSettings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.optionChat:
                intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
    }



    /**
     * init universal image loader
     */
    private void initImageLoader(){
        UniversalImageLoader imageLoader = new UniversalImageLoader(MainActivity.this);
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }

}
