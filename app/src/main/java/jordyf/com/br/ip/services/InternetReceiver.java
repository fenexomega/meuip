package jordyf.com.br.ip.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import jordyf.com.br.ip.R;
import jordyf.com.br.ip.activity.MainActivity;
import jordyf.com.br.ip.model.IPRegistry;
import jordyf.com.br.ip.util.HttpConnection;
import jordyf.com.br.ip.util.Prefs;

/**
 * Created by jordy on 2/15/17.
 */

public class InternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(Prefs.getBoolean(context,"enable_notifications") == false)
            return;

        ConnectivityManager connectivityManager =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetInfo != null && activeNetInfo.isConnected())
        {
            String url = context.getString(R.string.server_url);
            HttpConnection.Get(context, url, new HttpConnection.ICommand() {
                @Override
                public void Run(String response) {
                    IPRegistry registry = new IPRegistry(response);
                    registry.save();
                    showNotification(context, response);

                }

                @Override
                public void Error(String message) {

                }
            },10000);
        }



    }

    private void showNotification(Context context, String message)
    {
        Intent intent = new Intent(context, MainActivity.class);
        String new_ip = context.getString(R.string.new_ip);
        String click_here = context.getString(R.string.click_here);

        PendingIntent pIntent = PendingIntent.getActivity( context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /** Getting the System service NotificationManager */
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        /** Cancel all notifications */
        nManager.cancelAll();

        /** Configuring notification builder to create a notification */
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(new_ip + " " + message)
                .setContentText(click_here)
                .setSmallIcon(R.drawable.ic_search_white_18dp)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        /** Creating a notification from the notification builder */
        Notification notification = notificationBuilder.build();

        /** Sending the notification to system.
         * The first argument ensures that each notification is having a unique id
         * If two notifications share same notification id, then the last notification replaces the first notification
         * */
        nManager.notify((int)System.currentTimeMillis(), notification);
    }
}
