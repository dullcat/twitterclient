package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends AppCompatActivity {
    Tweet tweet;
    ImageView ivProfileImage;
    TextView tvName;
    EditText etTweet;
    TwitterClient client;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient();

        tweet = new Tweet();

        String screenName = getIntent().getStringExtra("screen_name");
        //user
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                populateHeader(user);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                Log.d("DEBUG", response.toString());
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateHeader(User user) {
        etTweet = (EditText) findViewById(R.id.etTweet);
        tvName = (TextView) findViewById(R.id.tvName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tweet.setUser(user);

        tvName.setText(user.getName());
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
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

    public void sendTweet(View view) {
        tweet.setBody(etTweet.getText().toString());
        Intent result = new Intent();
        result.putExtra("tweet", tweet);
        setResult(RESULT_OK, result);

        finish();

    }
}
