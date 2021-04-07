package com.arcias.melocate.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            Log.d("ConnectivityReceiver", "ConnectivityReceiver invoked...");
        context.startService(new Intent(context, LocationService.class));
            // only when background update is enabled in prefs
//                boolean noConnectivity = intent.getBooleanExtra(
//                        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

//                if (!noConnectivity) {
//                    ConnectivityManager cm = (ConnectivityManager) context
//                            .getSystemService(Context.CONNECTIVITY_SERVICE);
//                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
//
//                    // only when connected or while connecting...
//                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//
//                        // if we have mobile or wifi connectivity...
//                        if (((netInfo.getType() == ConnectivityManager.TYPE_MOBILE))
//                                || (netInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
//                            Log.d("ConnectivityReceiver", "We have internet, start update check and disable receiver!");
//
//
//                        }
//                    }
//                }


    }
//    public static void enableReceiver(Context context) {
//        ComponentName component = new ComponentName(context,
//                ConnectivityReceiver.class);
//
//        context.getPackageManager().setComponentEnabledSetting(component,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//    }

//    /**
//     * Disables ConnectivityReceiver
//     *
//     * @param context
//     */
//    public static void disableReceiver(Context context) {
//        ComponentName component = new ComponentName(context,
//                ConnectivityReceiver.class);
//
//        context.getPackageManager().setComponentEnabledSetting(component,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
//    }
//    private void startLocationService(Context context){
//        MainFrame mainFrame=new MainFrame();
//
//        boolean isPermission= mainFrame.isLocationServiceRunning();
//        if(!(isPermission)){
//            Intent serviceIntent = new Intent(context, LocationService.class);
////        this.startService(serviceIntent);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//
//                context.startForegroundService(serviceIntent);
//            }else{
//                context.startService(serviceIntent);
//            }
//        }
//    }
}
