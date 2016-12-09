package cf.VoxStudio.bubblekeep;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

//TODO: Make it running in background so the notifications don't have problems when users receive them
public class NotificationService extends com.google.firebase.messaging.FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String url = remoteMessage.getData().get("url");
        Uri webpage = Uri.parse(url);


        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setColor(getResources().getColor(R.color.notification_color));
        notificationBuilder.setLights(16759552, 1000, 1000);
        notificationBuilder.setSmallIcon(R.drawable.ic_lightbulb);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}



