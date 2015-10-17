package news.esports.com.esports.socialobject;

import android.content.Context;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;
import news.esports.com.esports.constants.SocialNetwork;

/**
 * Created by Renso on 10/11/2015.
 */
public class Twitter extends Network{
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YOUR KEY HERE";
    private static final String TWITTER_SECRET = "YOUR SECRET HERE";

    public Twitter(SocialNetwork socialNetwork) {
        super(socialNetwork);
    }

    @Override
    public void login(Context context) {
        //Setting twitter account to share articles
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new com.twitter.sdk.android.Twitter(authConfig));
    }

    @Override
    public void publishArticle(Context context, ShareArticle article) {
        StringBuilder articleToShare = new StringBuilder();
        articleToShare.append(article.getTitle()).append("\n").append(article.getLink());

        TweetComposer.Builder builder = new TweetComposer.Builder(context).text(articleToShare.toString());
        builder.show();
    }
}
