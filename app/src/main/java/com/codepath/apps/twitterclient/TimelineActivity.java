package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private User self;
    private Tweet newTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimelineMore();
                return true;
            }
        });

        client = TwitterApplication.getRestClient();//singleton
        getSelfUser();
        populateTimeline();
    }

    private void getSelfUser() {
        client.getUser(28515992, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                self = User.fromJson(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                //Toast.makeText(TimelineActivity.this, json.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Debug", json.toString());
                aTweets.clear();
                aTweets.addAll(Tweet.fromJsonArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

    private void populateTimelineMore() {
        long last = tweets.get(tweets.size()-1).getUid();
        client.getHomeTimelineMore(last, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                //Toast.makeText(TimelineActivity.this, json.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Debug", json.toString());

                aTweets.addAll(Tweet.fromJsonArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
        sendTweet(tweet.getBody());
        if (newTweet == null){
            //Toast.makeText(TimelineActivity.this, "Tweeting failed!", Toast.LENGTH_SHORT).show();
            //return;
        }
        //aTweets.insert(newTweet, 0);

    }

    private void sendTweet(String status) {
        client.postTweet(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                newTweet = Tweet.fromJson(response);
                //Toast.makeText(TimelineActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(TimelineActivity.this, "Tweeting succeeded!", Toast.LENGTH_SHORT).show();
                Log.d("Debug", response.toString());
                tweets.clear();
                aTweets.notifyDataSetChanged();
                populateTimeline();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(TimelineActivity.this, "Tweeting failed!", Toast.LENGTH_SHORT).show();
                Log.d("Debug", errorResponse.toString());
            }
        });
    }

    public void onComposeAction(MenuItem item) {
        Toast.makeText(this, "Compose!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("user", self);
        startActivityForResult(i, 200);

        populateTimeline();
    }
}
