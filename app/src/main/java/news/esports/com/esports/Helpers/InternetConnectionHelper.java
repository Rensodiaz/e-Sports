package news.esports.com.esports.Helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by Renso on 12/19/2015.
 */
public class InternetConnectionHelper {

    private static final String tag = "ConnectivityHelper";

    //Get the netWork manager
    private static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isAnyNetworkConnected(Context context) {
        //android 22 and below
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP_MR1) {
            return isWiFiNetworkConnected(context) || isMobileNetworkConnected(context);
        }else {//This is for android 23 and above
            return netWorksAvailable(context);
        }
    }

    /**
     * This method is for android 23 and above
     */
    public static boolean netWorksAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * This methods are for every any android version below 23
     * because some of them are deprecated.
     * @param context
     * @return
     */
    public static boolean isWiFiNetworkConnected(Context context) {
        NetworkInfo mWifi = getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }
    public static boolean isMobileNetworkConnected(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean hasTelephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        //check if the device support network data like provider.
        if(hasTelephony){
            NetworkInfo mProvider = getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return mProvider.isConnected();
        }else{
            return false;
        }
    }


}
