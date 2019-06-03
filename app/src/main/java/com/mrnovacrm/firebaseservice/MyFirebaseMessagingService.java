package com.mrnovacrm.firebaseservice;

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
import com.mrnovacrm.R;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.utils.NotificationUtils;

import org.json.JSONObject;

/**
 * Created by Prasad on 18/04/18.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    GlobalShare globalShare;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        globalShare=(GlobalShare)getApplicationContext();
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        String click_action = remoteMessage.getNotification().getClickAction();
        String role="";
        //Calling method to generate notification
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                role=json.getString("role");
            } catch (Exception e) {
                //  Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(),
                "", "", "", "", click_action,role);

//        Toast.makeText(getApplicationContext(),"onMessageReceived",Toast.LENGTH_SHORT).show();
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
//        if (remoteMessage == null)
//            return;
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification().getBody());
//        }
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }
    }

    private void sendNotification(String messageBody, String messageTitle, String user_id, String date,
                                  String hal_id, String M_view, String click_action,String role) {

//        Intent intent=null;
//        if(SharedDB.isLoggedIn(getApplicationContext()))
//        {
//            if (role.equals("Supervisor") || role.equals("Manager")) {
//                globalShare.setSendnotificationfrom("notification");
//                intent = new Intent(getApplicationContext(), HydcndEmployeeMenuScreenActivity.class);
//            }else if(role.equals("User"))
//            {
//                   globalShare.setMenuSelectedPosition("1");
//            }else{
//                intent = new Intent(getApplicationContext(), HomeActivity.class);
//                if(HydcndMenuScreenActivity.mainfinish!=null)
//                {
//                    HydcndMenuScreenActivity.mainfinish.finish();
//                }
//            }
//        }else{
//           intent = new Intent(getApplicationContext(), MainActivity.class);
//        }
//        intent.putExtra("user_id", "");
//        intent.putExtra("date", "");
//        intent.putExtra("hal_id", "");
//        intent.putExtra("M_view", "");
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//
//        notificationBuilder.setSmallIcon(R.drawable.hydcnd_logo)
//                .setContentTitle(messageTitle)
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());

        Intent intent = new Intent(click_action);
        intent.putExtra("user_id", "test");
        intent.putExtra("date", "test1");
        intent.putExtra("hal_id", "test halid");
        intent.putExtra("M_view","test mview");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}



//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.nova.R;
//import com.nova.constants.GlobalShare;
//import com.nova.utils.NotificationUtils;
//
//
//import org.json.JSONObject;
//
///**
// * Created by Prasad on 18/04/18.
// */
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
//
//    private NotificationUtils notificationUtils;
//    GlobalShare globalShare;
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        globalShare=(GlobalShare)getApplicationContext();
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//        }
//        String click_action = remoteMessage.getNotification().getClickAction();
//        String role="";
//        //Calling method to generate notification
//        if (remoteMessage.getData().size() > 0) {
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                role=json.getString("role");
//            } catch (Exception e) {
//              //  Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }
//        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(),
//                "", "", "", "", click_action,role);
//    }
//
//    private void sendNotification(String messageBody, String messageTitle, String user_id, String date,
//                                  String hal_id, String M_view, String click_action,String role) {
//
//        Intent intent = new Intent(click_action);
//        intent.putExtra("user_id", "test");
//        intent.putExtra("date", "test1");
//        intent.putExtra("hal_id", "test halid");
//        intent.putExtra("M_view","test mview");
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(messageTitle)
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());
//    }
//}