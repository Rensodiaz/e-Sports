package news.esports.com.esports.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import news.esports.com.esports.Helpers.Environments;
import news.esports.com.esports.MainActivity;
import news.esports.com.esports.R;
import news.esports.com.esports.adapters.GridAdapter;
import news.esports.com.esports.interfaces.ApiManager;
import news.esports.com.esports.models.Collection1;
import news.esports.com.esports.models.DataCollection;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

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
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiManager service = retrofit.create(ApiManager.class);

            Call<DataCollection> lolNews = service.dataCollection(game);
            lolNews.enqueue(new Callback<DataCollection>() {
                @Override
                public void onResponse(final Response<DataCollection> response) {
                    final Runnable r = new Runnable() {
                        public void run() {
                            //remove the loading and display content
                            MainActivity mainActivity = (MainActivity) getActivity();
                            if (mainActivity != null)
                                mainActivity.finishGettingData(false);
                            try {
                                loadingData(response.body().getResults().getCollection1());
                            }catch (NullPointerException ex){
                                Log.e(tag, "This exception need to be handle in a better way");
                            }
                        }
                    };
                    handler.postDelayed(r, 1000);
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.e(tag, "failed to get title game: " + t.toString());
                    failRequest();
                }
            });
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
