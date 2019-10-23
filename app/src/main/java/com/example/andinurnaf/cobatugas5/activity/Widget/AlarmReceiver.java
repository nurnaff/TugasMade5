package com.example.andinurnaf.cobatugas5.activity.Widget;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.andinurnaf.cobatugas5.activity.BuildConfig;
import com.example.andinurnaf.cobatugas5.activity.R;
import com.example.andinurnaf.cobatugas5.activity.Activity.DetailMovieActivity;
import com.example.andinurnaf.cobatugas5.activity.Activity.MainActivity;
import com.example.andinurnaf.cobatugas5.activity.Entity.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private String API_KEY = BuildConfig.TMDB_API_KEY;
    private String url = "https://api.themoviedb.org/3/movie/now_playing?api_key="+ API_KEY + "&language=en-US";

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_NOTIF_ID = "notif_id";

    private final int DAILY_NOTIF_ID = 100;
    private final int NOWPLAYING_NOTIF_ID = 101;

    /* setting waktu untuk notifikasi*/
    private final String DAILY_NOTIF_TIME = "07:00";
    private final String NOWPLAYING_NOTIF_TIME = "08:00";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        String title = intent.getStringExtra(EXTRA_TITLE);
        final String message = intent.getStringExtra(EXTRA_MESSAGE);
        int notifId = intent.getIntExtra(EXTRA_NOTIF_ID, DAILY_NOTIF_ID );

        Log.d(">>_NOTIF ID", String.valueOf(notifId));

        if( notifId == NOWPLAYING_NOTIF_ID ){

            final PendingResult result = goAsync();
            Thread thread = new Thread() {
                public void run() {
                    Movie newm = nowPlaying();
                    String message = "Now Playing (start from: "+ newm.getReleaseDate() +")";
                    showNotification(context.getApplicationContext(), newm.getTitle(), message, NOWPLAYING_NOTIF_ID, newm);
                    result.finish();
                }
            };

            thread.start();

        } else {
            showNotification(context, title, message, notifId, null);
        }

        Log.d(">>_ALARM!", "RECEIVED");

    }

    private void showNotification(Context context, String title, String message, int notifId, Movie movie){
        Intent intent;

        if(notifId == NOWPLAYING_NOTIF_ID){
            intent = new Intent(context.getApplicationContext(), DetailMovieActivity.class);
            intent.putExtra("movie", movie);
        } else {
            intent = new Intent(context.getApplicationContext(), MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        notificationManager.notify(notifId, notification.build());
    }

    public void setNotificationAlarm(Context context, String type){
        String time;
        int notifId;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        if( type.equals("daily") ){
            intent.putExtra(EXTRA_TITLE, "Nonton Apa");
            intent.putExtra(EXTRA_MESSAGE, "Cek film favoritmu");
            intent.putExtra(EXTRA_NOTIF_ID, DAILY_NOTIF_ID);

            time = DAILY_NOTIF_TIME;
            notifId = DAILY_NOTIF_ID;

        } else {
            intent.putExtra(EXTRA_TITLE, "");
            intent.putExtra(EXTRA_MESSAGE, "Now Playing");
            intent.putExtra(EXTRA_NOTIF_ID, NOWPLAYING_NOTIF_ID);

            time = NOWPLAYING_NOTIF_TIME;
            notifId = NOWPLAYING_NOTIF_ID;
        }

        String timeArray[] = time.split(":");
        Calendar calNotif= Calendar.getInstance();
        calNotif.set(Calendar.HOUR_OF_DAY,  Integer.parseInt(timeArray[0]));
        calNotif.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calNotif.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, notifId, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calNotif.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, "Notification switched ON", Toast.LENGTH_SHORT).show();

        Log.d(">>_CAL_NOTIF",calNotif.getTime().toString());

    }

    public void cancelNotification(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent;

        pendingIntent = PendingIntent.getBroadcast(context, DAILY_NOTIF_ID, intent, 0);
        alarmManager.cancel(pendingIntent);

        pendingIntent = PendingIntent.getBroadcast(context, NOWPLAYING_NOTIF_ID, intent, 0);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Notification switched OFF", Toast.LENGTH_SHORT).show();
    }


    private Movie nowPlaying() {
        Movie newmovie = new Movie();
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {
            URL httpurl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) httpurl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.getDoInput();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            JSONObject resultObject = new JSONObject(stringBuilder.toString());
            JSONArray list = resultObject.getJSONArray("results");

            Calendar today = Calendar.getInstance();
            long diffdate = today.getTimeInMillis();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            for (int i = 0 ; i < list.length() ; i++){
                JSONObject movieitem = list.getJSONObject(i);
                Movie movie = new Movie(movieitem);

                try {
                    d = formatter.parse(movie.getReleaseDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar releaseDate = Calendar.getInstance();
                releaseDate.setTime(d);

                long diff = today.getTimeInMillis() - releaseDate.getTimeInMillis();
                if( diff < diffdate){
                    diffdate = diff;
                    newmovie = movie;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newmovie;
    }

}