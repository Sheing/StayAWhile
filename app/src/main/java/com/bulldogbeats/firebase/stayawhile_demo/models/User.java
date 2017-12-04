package com.bulldogbeats.firebase.stayawhile_demo.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String name;
    private String phone;
    private String profile_image;
    private String user_id;
    //private String security_level;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String username, String name, String email,String phone, String profile_image, String user_id) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.profile_image = profile_image;
        this.user_id = user_id;
        this.name = name;
        //this.security_level = security_level;
    }
    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", user_id='" + user_id + '\'' +

                '}';
    }

}
// [END blog_user_class]
