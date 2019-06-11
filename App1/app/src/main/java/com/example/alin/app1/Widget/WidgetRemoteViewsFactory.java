package com.example.alin.app1.Widget;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.alin.app1.DB.Aplicatie;
import com.example.alin.app1.DB.AplicatieRepository;
import com.example.alin.app1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{

   // private AplicatieViewModel mAplicatieViewModel ;
    private AplicatieRepository mAplicatieRepository ;
    private Context ctxt = null;
    private int appWidgetId;
    private String[] strList;
    private List<String> sList = new ArrayList<String>();
    private List<Aplicatie> appList;

    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent, Application ap) {
        this.ctxt=applicationContext;
        //mAplicatieViewModel = ViewModelProviders.of(this).get(AplicatieViewModel)
        this.mAplicatieRepository = new AplicatieRepository(ap);
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        /*List<PackageInfo> packList = applicationContext.getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            // if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            //  {
            String appName = packInfo.applicationInfo.loadLabel(applicationContext.getPackageManager()).toString();
            Log.i("WIDGET_R_VIEW " + Integer.toString(i), appName);
           // Aplicatie app = new Aplicatie();
           // app.setName(appName);
            sList.add(appName);
            //  }
        }*/

        appList =  mAplicatieRepository.getAllData();
        //appList =  mAplicatieRepository.getSortedData();
        Log.i("WIDGET_R_VIEW ","in widget r factory ");
        if(appList != null ){

            if(appList.size() > 0) {
                for (int i = 0; i < appList.size(); i++) {
                    Aplicatie app = appList.get(i);
                    sList.add(app.getName());
                }
            }else{
                sList.add("NO_DATA");
            }
        }else{
              //  Aplicatie app2 = new Aplicatie();
               // app2.setName("NO_DATA");
                sList.add("NULL");
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
        sList = new ArrayList<>();
        appList = (List<Aplicatie>) mAplicatieRepository.getAllData();
        Log.i("WIDGET_R_VIEW ","in widget r2 factory appListsize:  " + appList.size());
        if(appList != null ){
            Collections.sort(appList, new Comparator<Aplicatie>(){
                @Override
                public int compare(Aplicatie obj1, Aplicatie obj2) {

                    // ## Descending order
                    //return obj2.getPrioritate().compareToIgnoreCase(obj1.getPrioritate()); // To compare string values
                    return Integer.valueOf(obj2.getPrioritate()).compareTo(Integer.valueOf(obj1.getPrioritate())); // To compare integer values

                }
            });
            if(appList.size() > 0) {
                for (int i = 0; i < appList.size(); i++) {
                    Aplicatie app = appList.get(i);
                    Log.i("WIDGET_R_VIEW "," appListElement:  " + appList.get(i).getName() + "  "+ appList.get(i).getPrioritate());
                    sList.add(app.getName());
                }
            }else{
                sList.add("NO_DATA");
            }
        }else{
            //  Aplicatie app2 = new Aplicatie();
            // app2.setName("NO_DATA");
            sList.add("NULL");
        }
        strList = new String[sList.size()];
        strList = sList.toArray(strList);
        for (int i = 0; i < sList.size(); i++) {
           // Log.i("WIDGET_R_VIEW ","apps  "+ sList.size() + "  " + strList[i] );
        }
    }

}