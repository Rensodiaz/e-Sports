package news.esports.com.esports;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;

import news.esports.com.esports.Helpers.GameSelector;
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
    private boolean orientationState;

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
        if (savedInstanceState==null) {
            getGame(Preferences.getSelectedGame(this));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        if(newConfig.orientation != myOrientation){
            Log.w(tag, "my orientation change!");
            orientationState = true;
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "onResume");
        // This is a facebook analytics tool
        AppEventsLogger.activateApp(this);
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
     * @param link
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
     * @param sub
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
     * @param fragment
     * @param fragmentTag
     */
    public void setFragment(final Fragment fragment, String fragmentTag){
        if (fragmentTag.equals(LIST_TITLE_TAG) && !orientationState){
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
        orientationState = false;
        getGame(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * game selection from the drawer menu
     * @param game
     */
    private void getGame(int game){
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
    }

    /**
     * changing the image of the drawer layout for each game
     * @param backgroundImage
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changeDrawerHeaderImage(int backgroundImage){
        //drawer header layout
        final LinearLayout drawerHeaderLayout = (LinearLayout)findViewById(R.id.drawerHeaderLayout);
        drawerHeaderLayout.setBackground(ContextCompat.getDrawable(this, backgroundImage));
    }

    /**
     * This remove the loading object from the main screen
     * @param show
     */
    public void finishGettingData(boolean show){
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
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
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
}


