package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.codepath.apps.twitterclient.EndlessScrollListener;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by towu on 11/11/15.
 */
public class TweetsListFragment extends Fragment {
    protected ArrayList<Tweet> tweets;
    public TweetsArrayAdapter aTweets;
    protected ListView lvTweets;

    protected TwitterClient client;
    private User self;



    // inflation logic


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimelineMore();
                return true;
            }
        });



        return v;
    }

    // creation lifecycle event

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

        client = TwitterApplication.getRestClient();//singleton


    }

    public  void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
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

    protected void populateTimelineMore() {
    }

    protected void populateTimeline() {
    }

    public void populateTimelinePublic() {
        //populateTimeline();
        ((BaseAdapter) lvTweets.getAdapter()).notifyDataSetChanged();
    }

}
