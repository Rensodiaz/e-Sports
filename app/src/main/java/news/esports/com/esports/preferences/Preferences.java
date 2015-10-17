package news.esports.com.esports.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import news.esports.com.esports.R;

/**
 * Created by Renso on 9/23/2015.
 */
public class Preferences {

    //Name of our preference
    private static String preferences = "preferences";

    private static final String firstTimeLaunch = "firstTimeLaunch";
    private static final String selectedGame = "selectedGame";

    public static SharedPreferences getSharedPreferences(Context context){

        return context.getSharedPreferences(preferences, 0);
    }

    /*
     * Getters and setters for the option if the application launch for the first time.
     */
    public static void setLaunchFirstTime(Context context, boolean firstTime){
        getSharedPreferences(context).edit().putBoolean(firstTimeLaunch, firstTime).commit();
    }

    public static boolean getLaunchFirstTime(Context context){
        return getSharedPreferences(context).getBoolean(firstTimeLaunch, false);
    }

    /*
     * Get the game that is selected by the user
     */
    public static void setSelectedGame(Context context, int type){
        getSharedPreferences(context).edit().putInt(selectedGame, type).commit();
    }
    public static int getSelectedGame(Context context){
        return getSharedPreferences(context).getInt(selectedGame, R.id.headLines);
    }
}
