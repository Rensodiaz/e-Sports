package news.esports.com.esports.fragments;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import news.esports.com.esports.Helpers.ArticleHelper;
import news.esports.com.esports.Helpers.ArticleInterface;
import news.esports.com.esports.Helpers.Environments;
import news.esports.com.esports.MainActivity;
import news.esports.com.esports.R;
import news.esports.com.esports.models.ArticleContent;
/**
 * Created by Renso on 9/13/2015.
 */
public class TitleArticle extends Fragment implements ArticleInterface ,Environments{

    private final String tag = this.getClass().getSimpleName();
    final String MIME = "text/html";
    final String ENCODING = "utf-8";
    private ProgressBar progressBar;
    private String link;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        link = getArguments().getString("link");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Font
        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidSerif_Regular.ttf");
        // Inflate the layout for this fragment
        final View articleView = inflater.inflate(R.layout.title_content, container, false);
        webView = (WebView)articleView.findViewById(R.id.articleWebView);
        this.webView.getSettings().setBuiltInZoomControls(false);
        this.webView.getSettings().setSupportZoom(false);
        this.webView.getSettings().setJavaScriptEnabled(true);

        progressBar = (ProgressBar)articleView.findViewById(R.id.progressBar);
        //get my article
        new ArticleHelper(this, link);

        //Ads layout
        AdView mAdView = (AdView) articleView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return articleView;
    }
    /*
        Allow to know if the actual fragment can back up
     */
    public boolean allowBackPressed(){
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(tag, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(tag, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(tag, "onDestroy");
    }

    @Override
    public void getContentData(ArticleContent article) {
        progressBar.setVisibility(View.GONE);
        MainActivity mainActivity = (MainActivity)getActivity();
        if (mainActivity!=null){
            mainActivity.finishGettingData(false);
        }
        openWebView(article.getArticleData().toString());
    }

    @SuppressLint("NewApi")
    private void openWebView(String data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webView.loadDataWithBaseURL(GOSU_ENPOINT, getHtmlData(data), MIME, ENCODING, null);
    }

    /**
     * Preparing the header of my data
     * @param bodyHTML
     * @return
     */
    private String getHtmlData(String bodyHTML) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point myPoint = getDisplaySize(display);
        final int width = myPoint.x;
        bodyHTML = bodyHTML.replaceAll("<img", "<img style=\\\"max-width:100%\\\"")
                    .replaceAll("<iframe", "<iframe style=\\\"max-width:100%\\\"");
        String head = "<head><style>img{max-width: "+width+"; width: auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private static Point getDisplaySize(final Display display) {
        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

}
