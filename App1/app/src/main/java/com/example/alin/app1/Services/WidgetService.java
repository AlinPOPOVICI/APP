package com.example.alin.app1.Services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.alin.app1.Widget.WidgetRemoteViewsFactory;

public class WidgetService extends RemoteViewsService
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent, this.getApplication()));
    }

}