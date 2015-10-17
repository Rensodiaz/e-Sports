package news.esports.com.esports.interfaces;

import news.esports.com.esports.models.DataCollection;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Renso on 9/11/2015.
 */
public interface ApiManager {

    //@GET("/en/rss.xml")
    //Call<LolNewsFeed> latestLolNews();

    //@GET("/feed/")
    //Call<LolNewsFeed> dota2News();

    //@GET("/r/{subReddit}/hot/.json")
    //Call<Data> redditHot(@Path("subReddit") String subReddit);

    //https://www.kimonolabs.com/api/ this is for titles and images
    @GET("/api/{game}?apikey=YOUR KIMONO APP KEY HERE")
    Call<DataCollection> dataCollection(@Path("game") String game);

}
