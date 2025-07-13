package news.esports.com.esports;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;

import news.esports.com.esports.Helpers.GameSelector;
import news.esports.com.esports.Helpers.InternetConnectionHelper;
import news.esports.com.esports.constants.SocialNetwork;
import news.esports.com.esports.factories.SocialFactory;
import news.esports.com.esports.fragments.ListTitles;
import news.esports.com.esports.fragments.TitleArticle;
import news.esports.com.esports.fragments.animation.ZoomOutPageTransformer;
import news.esports.com.esports.preferences.Preferences;
import news.esports.com.esports.socialobject.Network;
import news.esports.com.esports.socialobject.ShareArticle;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener{

    private final String tag = this.getClass().getSimpleName();
    private final static String TITLE_CONTENT_TAG = "titleContent";
    private final static String LIST_TITLE_TAG = "listTitle";
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private List<Fragment> myFragments = new ArrayList<>();
    private TextView drawerTextLine;
    private Menu menu;
    private ShareArticle shareArticle;
    private int myOrientation;
    private int orientationState = 1;//by default is portrait mode
    private boolean loadingContent = false;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(tag, "onCreate");
        //select the screen actual orientation
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        myOrientation = display.getOrientation();

        //TextView for the text on drawer
        drawerTextLine = (TextView)findViewById(R.id.drawerTextLine);

        //set the fragment of feeds
        if (savedInstanceState==null && InternetConnectionHelper.isAnyNetworkConnected(this)) {
            getGame(Preferences.getSelectedGame(this));
        }else {
            Toast.makeText(this, getString(R.string.not_internet), Toast.LENGTH_SHORT).show();
        }
        //Toolbar for the hamburger icon and animation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer to select game to see news from.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //View pager for the animation
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.addOnPageChangeListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation != myOrientation){
            orientationState = newConfig.orientation;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "onResume");
        // This is a facebook analytics tool
        AppEventsLogger.activateApp(this);
        //Registering the receiver for the connections changes
        IntentFilter connectionChangesFilters = new IntentFilter();
        connectionChangesFilters.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        connectionChangesFilters.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(connectionChangeReceiver, connectionChangesFilters);//TODO: This receiver need to be unregistered from the service
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(tag, "onPause");
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    /**
     * This method is called within the fragment to go to the selected article content
     * @param link String with the article url.
     */
    public void switchFragment(String title, String link){
        shareArticle = new ShareArticle(title, link);
        Fragment titleArticle = new TitleArticle();
        Bundle linkBundle = new Bundle();
        linkBundle.putString("link", link);
        titleArticle.setArguments(linkBundle);
        setFragment(titleArticle, TITLE_CONTENT_TAG);
    }

    /**
     * Change news subject
     * @param sub Object with an int that belong to the game to display
     */
    public void gameNews(Object sub){
        Fragment titlesFrag = new ListTitles();
        Bundle subBundle = new Bundle();
        subBundle.putString("game", GameSelector.getGame(this, (int) sub));
        titlesFrag.setArguments(subBundle);
        setFragment(titlesFrag, LIST_TITLE_TAG);
    }

    @Override
    public void onBackPressed() {
        //finish getting data
        finishGettingData(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mPager.getCurrentItem() == 0 && !drawer.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final int position = mPager.getCurrentItem();
            myFragments.remove(position);
            mPagerAdapter.notifyDataSetChanged();
            mPager.setCurrentItem(position - 1, true);
        }
    }

    /**
     * This method change fragments eg: Titles and content of those titles
     * @param fragment fragment to be add to our pagerAdapter
     * @param fragmentTag tag of the fragment to let know to which category does this fragment belong to.
     */
    public void setFragment(final Fragment fragment, String fragmentTag){
        if (fragmentTag.equals(LIST_TITLE_TAG)){
            if (myFragments.isEmpty()) {
                myFragments.add(fragment);
            }else {
                myFragments.clear();
                myFragments.add(fragment);
                mPagerAdapter.notifyDataSetChanged();
                mPager.setCurrentItem(0, true);
            }
        }else if (fragmentTag.equals(TITLE_CONTENT_TAG)){
            final int listSize = myFragments.size();
            if (listSize>1){
                myFragments.remove(listSize - 1);
                myFragments.add(fragment);
            }else {
                myFragments.add(fragment);
            }
            mPagerAdapter.notifyDataSetChanged();
            //Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.animator.fragment_slide_right);
            //mPager.startAnimation(fadeInAnimation);
            mPager.setCurrentItem(myFragments.size() - 1, true);
        }
        //remove the loading progress from main screen
        finishGettingData(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //hiding the share bottom
        MenuItem share = menu.findItem(R.id.action_share);
        share.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_refresh:
                if (!myFragments.isEmpty()) {
                    if (mPager.getCurrentItem() == 0) {
                        //Show loading again
                        finishGettingData(true);
                        ListTitles fragment = (ListTitles) myFragments.get(0);
                        fragment.refreshContent();
                    }
                }
//        }else if (id == R.id.twitter){TODO: Twitter will be add in the future with others social network
//            Network twitter = SocialFactory.getSocialNetwork(SocialNetwork.TWITTER);
//            twitter.login(this);
//            twitter.publishArticle(this, shareArticle);
                break;
            case R.id.facebook:
                Network facebook = SocialFactory.getSocialNetwork(SocialNetwork.FACEBOOK);
                facebook.login(this);
                facebook.publishArticle(this, shareArticle);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Getting the news from each game
        int id = item.getItemId();
        //get the game available
//        orientationState = false;
        if (InternetConnectionHelper.isAnyNetworkConnected(this)) {//only fetch titles if there is internet connection
            getGame(id);
        }else{
            Toast.makeText(this, getString(R.string.not_internet), Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * game selection from the drawer menu
     * @param game int to the game we want to read news from.
     */
    private void getGame(int game){
        if (!loadingContent) {
            loadingContent=true;
            switch (game) {
                case R.id.headLines:
                    gameNews(R.string.headlinesKey);
                    drawerTextLine.setText(getResources().getString(R.string.headLines));
                    changeDrawerHeaderImage(R.drawable.headlines);
                    break;
                case R.id.Lol:
                    gameNews(R.string.lolSubKey);
                    drawerTextLine.setText(getResources().getString(R.string.LOL));
                    changeDrawerHeaderImage(R.drawable.lol);
                    break;
                case R.id.Dota2:
                    gameNews(R.string.dota2Key);
                    drawerTextLine.setText(getResources().getString(R.string.Dota2));
                    changeDrawerHeaderImage(R.drawable.dota2);
                    break;
                case R.id.counter_strike:
                    gameNews(R.string.counter_strikeKey);
                    drawerTextLine.setText(getResources().getString(R.string.Counter_Strike));
                    changeDrawerHeaderImage(R.drawable.counter_strike);
                    break;
                case R.id.starCraft:
                    gameNews(R.string.starcraftKey);
                    drawerTextLine.setText(getResources().getString(R.string.StarCraft));
                    changeDrawerHeaderImage(R.drawable.starcraft2);
                    break;
                case R.id.hearth_stone:
                    gameNews(R.string.hearthStoneKey);
                    drawerTextLine.setText(getResources().getString(R.string.HearthStone));
                    changeDrawerHeaderImage(R.drawable.hearthstone);
                    break;
                case R.id.heros_of_the_storm:
                    gameNews(R.string.herosofthestormKey);
                    drawerTextLine.setText(getResources().getString(R.string.Heroes_of_the_Storm));
                    changeDrawerHeaderImage(R.drawable.heroes_of_the_storm);
                    break;
                case R.id.overWatch:
                    gameNews(R.string.overWatchKey);
                    drawerTextLine.setText(getResources().getString(R.string.OverWatch));
                    changeDrawerHeaderImage(R.drawable.overwatch);
                    break;
            }
            Preferences.setSelectedGame(this, game);
        }else {
            Toast.makeText(this, getString(R.string.loading_message), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * changing the image of the drawer layout for each game
     * @param backgroundImage int of the image for the game selected.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changeDrawerHeaderImage(int backgroundImage){
        //drawer header layout
        final LinearLayout drawerHeaderLayout = (LinearLayout)findViewById(R.id.drawerHeaderLayout);
        drawerHeaderLayout.setBackground(ContextCompat.getDrawable(this, backgroundImage));
    }

    /**
     * This remove the loading object from the main screen
     * @param show boolean to remove or add the loading animation.
     */
    public void finishGettingData(boolean show){
        loadingContent = false;
        FrameLayout loadingLayout = (FrameLayout)findViewById(R.id.progressWraper);
        if (show){
            loadingLayout.setVisibility(View.VISIBLE);
        }else {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    //Here we change the refresh button and the share button in the action bar
    @Override
    public void onPageSelected(int position) {
        //hiding the share bottom
        MenuItem share = menu.findItem(R.id.action_share);
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        switch (position){
            case 1:
                share.setVisible(true);
                refresh.setVisible(false);
                break;
            default:
                share.setVisible(false);
                refresh.setVisible(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A simple pager adapter slider to switch fragment with a nice animation
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(androidx.fragment.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
            return myFragments.get(position);
        }

        @Override
        public int getCount() {
            return myFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment frag = (Fragment)object;
            final int position = myFragments.indexOf(frag);
            if (position >= 0) {
                return position;
            } else {
                return POSITION_NONE;
            }
        }
    }
    /**
     * This receiver will help to control the connection of data with in the app.
     */
    public BroadcastReceiver connectionChangeReceiver = new  BroadcastReceiver() {
        final String tag = this.getClass().getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                final int state = intent.getExtras().getInt(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                switch (state) {
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.d(tag, "WIFI_STATE_ENABLED");

                        break;

                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.d(tag, "WIFI_STATE_DISABLED");
//                        if (!ConnectivityHelper.isAnyNetworkConnected(context)) {
//                            if (bluetoothDataDialog == null) {
//                                buildAlertDialog(R.string.bluetoothData_network, R.string.bluetoothData_network_msg);
//                            }
//                        }
                        break;
                }
            }else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
                Log.i(tag, "Change to connection!!");
            }
        }
    };
}


