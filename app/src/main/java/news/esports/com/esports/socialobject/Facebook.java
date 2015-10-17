package news.esports.com.esports.socialobject;

import android.content.Context;
import android.net.Uri;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import news.esports.com.esports.MainActivity;
import news.esports.com.esports.constants.SocialNetwork;

/**
 * Created by Renso on 10/11/2015.
 */
public class Facebook extends Network {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    public Facebook(SocialNetwork socialNetwork) {
        super(socialNetwork);
    }

    @Override
    public void login(Context context) {
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

        MainActivity mainActivity = (MainActivity) context;
        shareDialog = new ShareDialog(mainActivity);
    }

    @Override
    public void publishArticle(Context context, ShareArticle article) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(article.getTitle())
                    .setContentUrl(Uri.parse(article.getLink()))
                    .build();
            shareDialog.show(linkContent);
        }
    }
}
