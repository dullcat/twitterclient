package com.codepath.apps.twitterclient;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by towu on 11/9/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    private Context context;
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
        this.context = context;
    }

    // TODO: ViewHolder pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvUserId = (TextView) convertView.findViewById(R.id.tvUserId);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);

        tvUserName.setText(tweet.getUser().getName());
        tvUserId.setText("@"+tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        tvRelativeTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        ivProfileImage.setTag(tweet.getUser().getScreenName());

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String screenName = v.getTag().toString();
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("screen_name", screenName);
                context.startActivity(i);
            }
        });

        return convertView;

    }


    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
