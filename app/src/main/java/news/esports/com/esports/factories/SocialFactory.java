package news.esports.com.esports.factories;

import news.esports.com.esports.constants.SocialNetwork;
import news.esports.com.esports.socialobject.Facebook;
import news.esports.com.esports.socialobject.Network;
import news.esports.com.esports.socialobject.Twitter;

/**
 * Created by Renso on 10/11/2015.
 */
public class SocialFactory {

    public static Network getSocialNetwork(SocialNetwork socialNetwork){
        Network network = null;
        switch (socialNetwork){
            case TWITTER:
                network = new Twitter(socialNetwork);
                break;
            case FACEBOOK:
                network = new Facebook(socialNetwork);
                break;
            default:
                //should trow an exception because network does not exist
                break;
        }
        return network;
    }
}
