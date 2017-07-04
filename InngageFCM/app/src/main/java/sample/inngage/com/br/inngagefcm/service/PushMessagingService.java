package sample.inngage.com.br.inngagefcm.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import sample.inngage.com.br.inngagefcm.MainActivity;
import sample.inngage.com.br.inngagefcm.R;

/**
 * Created by viniciusdepaula on 24/06/17.
 */

public class PushMessagingService extends FirebaseMessagingService {

    private static final String TAG = "PushMessagingService";
    String body, title = null;
    Random random = new Random();
    int notifyID = random.nextInt(9999 - 1000) + 1000;
    JSONObject jsonObject;

    /**
     * Get the push notification event
     *
     * @param  remoteMessage  Remote message from push notification
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

           
            jsonObject = parseRemoteMessageToJson(remoteMessage);
            showNotification(jsonObject);
        }
    }

    private JSONObject parseRemoteMessageToJson(RemoteMessage remoteMessage) {

        Map<String, String> params = remoteMessage.getData();
        jsonObject = new JSONObject(params);
        Log.d("JSON:", jsonObject.toString());
        return jsonObject;
    }

    /**
     * Show the push notification
     *
     * @param  jsonObject The message title
     */
    private void showNotification(JSONObject jsonObject) {

        Intent intent = new Intent(this, MainActivity.class);

        try {

            if (!jsonObject.isNull("id")) {

                intent.putExtra("notifyID", jsonObject.getString("id"));
            }
            if (!jsonObject.isNull("title")) {

                title = jsonObject.getString("title");
                intent.putExtra("title", title);
            }
            if (!jsonObject.isNull("body")) {

                body = jsonObject.getString("body");
                intent.putExtra("body", body);
            }
            if (!jsonObject.isNull("url")) {

                intent.putExtra("url", jsonObject.getString("url"));
            }

        } catch (JSONException e) {

            Log.e(TAG, "Error getting JSON field \n" +e);
        }

        Log.d(TAG, "Showing push notification");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, notificationBuilder.build());
    }
}
