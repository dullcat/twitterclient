package com.codepath.apps.twitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by towu on 11/9/15.
 */
public class User implements Serializable {
    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagLine() {
        return tagLine;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagLine;
    private int followersCount;
    private int followingCount;

    public static User fromJson(JSONObject json) {
        User user = new User();

        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.tagLine = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
