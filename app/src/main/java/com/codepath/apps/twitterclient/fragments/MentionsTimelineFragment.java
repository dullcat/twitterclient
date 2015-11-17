package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by towu on 11/11/15.
 */
public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();//singleton
        populateTimeline();
    }

    protected void populateTimeline() {
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                //Toast.makeText(getContext(), json.toString(), Toast.LENGTH_SHORT).show();
                //Log.d("Debug", json.toString());
                //aTweets.clear();
                addAll(Tweet.fromJsonArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

    protected void populateTimelineMore() {
        long last = tweets.get(tweets.size()-1).getUid();
        client.getMentionsTimelineMore(last-1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                //Toast.makeText(TimelineActivity.this, json.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Debug", json.toString());

                addAll(Tweet.fromJsonArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

}
