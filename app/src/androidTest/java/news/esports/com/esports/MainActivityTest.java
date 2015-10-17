package news.esports.com.esports;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import news.esports.com.esports.models.Collection1;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Renso on 10/14/2015.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Test if the loading screen is display
     */
    @Test
    public void progressWrapperDisplayed(){
        onView(withId(R.id.progressWraper)).check(matches(isDisplayed()));
    }

    /**
     * Test if the RecyclerView is display
     */
    @Test
    public void titlesDisplayed(){
       waitingTime(2000);
        onData(instanceOf(Collection1.class)).atPosition(0).inAdapterView(allOf(withId(R.id.img_thumbnail), isDisplayed()));
    }

    /**
     * Check that the item is created. onData() takes care of scrolling.
     */
    @Test
    public void list_Scrolls() {
        waitingTime(3000);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(10));
    }

    /**
     * Clicks on a cardView and checks that the recyclerView detected the click
     * and check if the content fragment is call.
     */
    @Test
    public void cardView_Click() {
        //load articles
        waitingTime(3000);
        //wait time
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //Move to the next fragment and check if WebView is displayed
        onView(withId(R.id.articleWebView)).check(matches(isDisplayed()));
    }

    /**
     * Test the application drawer
     */
    @Test
    public void mainDrawer(){
        waitingTime(2000);
        //open the drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        //perform multiple clicks on the drawer
        onView(withId(R.id.drawer_layout)).perform(click());
        waitingTime(1000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).perform(click());
        //onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    /**
     * Wait time before calling our actions
     */
    private static void waitingTime(long waitTime){
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
