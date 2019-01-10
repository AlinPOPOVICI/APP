package com.example.alin.app1;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private static final String[] items={"lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer",
            "adipiscing", "elit", "morbi",
            "vel", "ligula", "vitae",
            "arcu", "aliquet", "mollis",
            "etiam", "vel", "erat",
            "placerat", "ante",
            "porttitor", "sodales",
            "pellentesque", "augue",
            "purus"};

    private Context ctxt=null;
    private int appWidgetId;
    private String[] strList;
    private List<String> sList = new ArrayList<String>();

    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        this.ctxt=applicationContext;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        List<PackageInfo> packList = applicationContext.getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            // if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            //  {
            String appName = packInfo.applicationInfo.loadLabel(applicationContext.getPackageManager()).toString();
            Log.e("App № " + Integer.toString(i), appName);
            Aplicatie app = new Aplicatie(appName);
            sList.add(app.getTitle());
            //  }
        }
        strList = new String[sList.size()];
        strList = sList.toArray(strList);
    }



    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }


    @Override
    public int getCount() {
        return(strList.length);
    }


    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews row=new RemoteViews(ctxt.getPackageName(), R.layout.row);

        row.setTextViewText(android.R.id.text1, strList[position]);

        Intent i=new Intent();
        Bundle extras=new Bundle();


        extras.putString(WidgetProvider.EXTRA_WORD, strList[position]);
        i.putExtras(extras);
        row.setOnClickFillInIntent(android.R.id.text1, i);

        return(row);
    }


    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }

}