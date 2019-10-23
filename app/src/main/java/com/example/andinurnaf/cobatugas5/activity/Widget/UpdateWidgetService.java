package com.example.andinurnaf.cobatugas5.activity.Widget;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.andinurnaf.cobatugas5.activity.R;

public class UpdateWidgetService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.favorit_widget);
        ComponentName favWidget = new ComponentName(this, FavoritWidget.class);

        //reload listMovie here
//        updateAllWidgets();
        Toast.makeText(this, "next Job Done", Toast.LENGTH_SHORT).show();

        manager.updateAppWidget(favWidget, views);

        jobFinished(jobParameters,false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
