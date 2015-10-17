package news.esports.com.esports.socialobject;

import android.content.Context;

import news.esports.com.esports.constants.SocialNetwork;

/**
 * Created by Renso on 10/11/2015.
 */
public abstract class Network {

    private SocialNetwork network;

    public Network(SocialNetwork socialNetwork){
        this.network = socialNetwork;
    }

    public abstract void login(Context context);
    public abstract void publishArticle(Context context, ShareArticle article);
}
