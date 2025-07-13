package news.esports.com.esports.fragments;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import news.esports.com.esports.Helpers.Environments;
import news.esports.com.esports.R;
import news.esports.com.esports.adapters.GridAdapter;
import news.esports.com.esports.interfaces.ApiManager;
import news.esports.com.esports.models.Collection1;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Renso on 9/13/2015.
 */
public class ListTitles extends Fragment implements Environments {

    private final String tag = this.getClass().getSimpleName();
    @VisibleForTesting
    private RecyclerView mRecyclerView;
    @VisibleForTesting
    private RecyclerView.LayoutManager mLayoutManager;
    @VisibleForTesting
    private GridAdapter mAdapter;
    @VisibleForTesting
    private String game = null;
    @VisibleForTesting
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = getArguments().getString("game");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View listTitleView = inflater.inflate(R.layout.list_titles, container, false);
        // Calling the RecyclerView
        mRecyclerView = (RecyclerView)listTitleView.findViewById(R.id.recycler_view);
        //getting feeds
        getFeeds();
        return listTitleView;
    }

    /*
      Loading data
   */
    private void loadingData(List<Collection1> data){
        //set data
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GridAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);
    }
    /*
        Getting data
     */
    private void getFeeds() {
        if (game!=null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiManager service = retrofit.create(ApiManager.class);
            Call<JsonArray> eSportNews = service.getEsportNews();
            eSportNews.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    Log.w(tag, "response: " + response.body());
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.e(tag, "problems: " + t.getMessage());
                }
            });
//            Call<DataCollection> lolNews = service.dataCollection(game);
//            lolNews.enqueue(new Callback<DataCollection>() {
//                @Override
//                public void onResponse(final Response<DataCollection> response) {
//                    final Runnable r = new Runnable() {
//                        public void run() {
//                            //remove the loading and display content
//                            MainActivity mainActivity = (MainActivity) getActivity();
//                            if (mainActivity != null)
//                                mainActivity.finishGettingData(false);
//                            try {
//                                loadingData(response.body().getResults().getCollection1());
//                            }catch (NullPointerException ex){
//                                Log.e(tag, "This exception need to be handle in a better way");
//                            }
//                        }
//                    };
//                    handler.postDelayed(r, 1000);
//                }
//                @Override
//                public void onFailure(Throwable t) {
//                    Log.e(tag, "failed to get title game: " + t.toString());
//                    failRequest();
//                }
//            });
        }else {
            Log.w(tag, "gae variable should never be null here.");
        }
    }

    private void failRequest(){//TODO:Check this out because null exception here in the mean time a Log works
        Log.e(tag, "http issue getting the titles");
        //Toast.makeText(getActivity(), "Fail to get data, Please try again...", Toast.LENGTH_LONG).show();
    }
    /*
    Allow to know if the actual fragment can back up
    */
    public boolean allowBackPressed(){
        return false;
    }

    /**
     * Refresh the content of the fragment
     */
    public void refreshContent(){
        getFeeds();
    }

}
