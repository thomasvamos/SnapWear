package de.impierium.snapwear;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;


public class SnapWearActivity extends Activity {

    private Button takePictureButton;

    private BroadcastReceiver mReceiver;

    private static final String ACTION_RESPONSE = "com.example.android.wearable.elizachat.REPLY";
    public static final String EXTRA_REPLY = "reply";

    private View.OnClickListener takePictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(SnapWearActivity.this, "Take picture", Toast.LENGTH_LONG).show();
            createNotification();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_wear);

        takePictureButton = (Button) findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(takePictureClickListener);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "received notification.", Toast.LENGTH_LONG);
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(ACTION_RESPONSE));
        createNotification();
    }

    @Override
    protected void onPause() {
        NotificationManagerCompat.from(this).cancel(0);
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    private void createNotification() {

        int notificationId = 001;

        // Build intent for notification content
        Intent viewIntent = new Intent(this, SnapWearActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        // Build intent for additional action: take picture with service
        Intent takePictureIntent = new Intent(this, SnapWearService.class);
        PendingIntent takePicturePendingIntent =
                PendingIntent.getService(this, 0, takePictureIntent, 0);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("SnapWear")
                        .setContentText("Cheeese!")
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.drawable.ic_launcher, "Take Picture", takePicturePendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());

    }
}
