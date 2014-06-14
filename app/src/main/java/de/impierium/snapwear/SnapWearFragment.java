package de.impierium.snapwear;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.preview.support.wearable.notifications.*;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;

/**
 * Created by tomek on 14.06.14.
 */
public class SnapWearFragment extends Fragment {

    private Button takePictureButton;

    private View.OnClickListener takePictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(SnapWearFragment.this.getActivity(), "Take picture", Toast.LENGTH_LONG).show();
            createNotification();
        }
    };

    private void createNotification() {

        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(this.getActivity(), SnapWearFragment.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this.getActivity(), 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this.getActivity())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("SnapWear")
                        .setContentText("Cheeese!")
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this.getActivity());

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_snap_wear, container, false);
        takePictureButton = (Button) rootView.findViewById(R.id.takePictureButton);

        takePictureButton.setOnClickListener(takePictureClickListener);

        return rootView;
    }


}
