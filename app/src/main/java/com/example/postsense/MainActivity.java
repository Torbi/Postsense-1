package com.example.postsense;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    String channelId = "channelId";
    String channelTitle = "channelTitle";
    String channelDescription = "Default_channel_description";
    int notificationId = 1;
    private Socket socket = null;


    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("TAG", "onConnectError: " + args.toString());
        }
    };

    /*private Emitter.Listener onDisconnect = new Emitter.Listener(){
        @Override
        public void call(final Object... args) {
            if (activity != null)
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("TAG", "onDisconnect: " + args);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        }
    };
    */

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("TAG", "onConnect: " + args);
        }
    };

    private Emitter.Listener newMessageListner = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("TAG", "newMessageListner: " + args.toString());
            //Kalla på notifikationer
        }
    };


    protected void createSocket () {
        Log.d("socket", "createSocket start");

        try {
            socket = IO.socket("https://192.168.1.207:8000");

            socket.on(Socket.EVENT_CONNECT, onConnect);
            //socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.on("newMessage", newMessageListner);
            socket.connect();    //Connect socket to server
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
    }

    public void sendMessage(View v) {
        if (socket.connected()) {
            socket.emit("newMessage","Message from Android!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("test", "innan createSocket");
        createSocket();
        createNotificationChannel();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Alert")
                .setContentText("You´ve got mail!")
                .setChannelId(channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        ImageButton clockButton = (ImageButton) findViewById(R.id.clockButton);
        clockButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), history.class);
                startActivity(startIntent);
            }
        });

        ImageButton optionButton = (ImageButton) findViewById(R.id.optionButton);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(startIntent);
            }
        });

        Button notificationButton = (Button) findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(notificationId, builder.build());
            }
        });


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(channelDescription);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}











