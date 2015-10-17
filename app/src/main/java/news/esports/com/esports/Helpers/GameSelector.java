package news.esports.com.esports.Helpers;

import android.content.Context;

import news.esports.com.esports.R;

/**
 * Created by Renso on 9/19/2015.
 */
public class GameSelector {

    public static String getGame(Context context, int sub){
        String game = null;
        switch (sub){
            case R.string.headlinesKey:
                game = context.getResources().getString(R.string.headlinesKey);
                break;
            case R.string.lolSubKey:
                game = context.getResources().getString(R.string.lolSubKey);
                break;
            case R.string.dota2Key:
                game =  context.getResources().getString(R.string.dota2Key);
                break;
            case R.string.starcraftKey:
                game = context.getResources().getString(R.string.starcraftKey);
                break;
            case R.string.counter_strikeKey:
                game = context.getResources().getString(R.string.counter_strikeKey);
                break;
            case R.string.herosofthestormKey:
                game = context.getResources().getString(R.string.herosofthestormKey);
                break;
            case R.string.overWatchKey:
                game = context.getResources().getString(R.string.overWatchKey);
                break;
            case R.string.hearthStoneKey:
                game = context.getResources().getString(R.string.hearthStoneKey);
                break;
        }
        return game;
    }
}
