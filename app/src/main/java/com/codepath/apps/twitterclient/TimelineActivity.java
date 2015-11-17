package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends AppCompatActivity {

    private Tweet newTweet;
    TwitterClient client;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();

        // Get the viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager) ;
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);
    }

    public void onProfileView (MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        String screenName = "dullcat2008";
        i.putExtra("screen_name", screenName);
        startActivity(i);
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

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
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

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }
    private void sendTweet(String status) {
        client.postTweet(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(TimelineActivity.this, "Tweeting succeeded!", Toast.LENGTH_SHORT).show();
                Log.d("Debug", response.toString());
                //tweets.clear();
                //aTweets.notifyDataSetChanged();
                //int id = viewPager.getCurrentItem();
                //TweetsListFragment fragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(id);
                //fragment.populateTimelinePublic();
                //FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
                // Replace the content of the container
                //fts.replace(R.id.flContainer, new HomeTimelineFragment());
                // Append this transaction to the backstack
                //fts.addToBackStack("optional tag");
                // Commit the changes
                //fts.commit();

                String name = makeFragmentName(viewPager.getId(), 0);
                HomeTimelineFragment fragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentByTag(name);
                getSupportFragmentManager().findFragmentById(0);

                FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) viewPager.getAdapter();
                //HomeTimelineFragment fragment = (HomeTimelineFragment) fragmentPagerAdapter.getItem(0);
                fragment.populateTimeline();

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
        String screenName = "dullcat2008";
        i.putExtra("screen_name", screenName);

        startActivityForResult(i, 200);

        //populateTimeline();
    }
}
