package com.example.andinurnaf.cobatugas5.activity.Widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

import com.example.andinurnaf.cobatugas5.activity.Adapter.FavoritViewFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoritViewFactory(this.getApplicationContext(),intent);
    }
}
