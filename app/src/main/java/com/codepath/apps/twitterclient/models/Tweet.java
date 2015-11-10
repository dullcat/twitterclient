package com.codepath.apps.twitterclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by towu on 11/9/15.
 */
public class Tweet implements Serializable {
    private String body;
    private long uid;
    private long retweetCount;

    public User getUser() {
        return user;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setRetweetCount(long retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(User user) {

        this.user = user;
    }

    private User user;
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }


    public static Tweet fromJson(JSONObject json) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            tweet.user = User.fromJson(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray array) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                tweets.add(Tweet.fromJson(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}
