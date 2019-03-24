package com.example.alin.app1.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.alin.app1.DB.Aplicatie;
import com.example.alin.app1.DB.AplicatieData;
import com.example.alin.app1.DB.AplicatieDataRepository;
import com.example.alin.app1.DB.AplicatieRepository;
import com.example.alin.app1.DB.Data;

import java.util.ArrayList;
import java.util.List;

public class SuggestionListService extends Service {
    private AplicatieRepository mAplicatieRepository ;
    private AplicatieDataRepository mAplicatieDataRepository ;
    private List<Aplicatie> appList;
    private List<AplicatieData> appDataList;
   private List<String> sList = new ArrayList<String>();
    public SuggestionListService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAplicatieDataRepository = new AplicatieDataRepository(this.getApplication());
        mAplicatieRepository = new AplicatieRepository(this.getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Data data;
        //get latest db entry

        updateAppList(null);
       // updateAppList(data);
        return android.app.Service.START_STICKY;
    }

    public void updateAppList(Data data){
        mAplicatieRepository.deleteAll();
        appDataList =  mAplicatieDataRepository.getAllData();

        Log.i("SUGGESTION ","updateLIST ");
        if(appDataList != null ){

            if(appDataList.size() > 0) {
                for (int i = 0; i < appDataList.size(); i++) {
                    AplicatieData appdata = appDataList.get(i);
                    Aplicatie app = new Aplicatie();
                    app.setName(appdata.getName());

                    app.setPrioritate(evaluate(data,appdata));
                    //appList.add(app);
                    mAplicatieRepository.insert(app);
                }
            }else{
                //sList.add("NO_DATA");
            }
        }else{
            //  Aplicatie app2 = new Aplicatie();
            // app2.setName("NO_DATA");
           // sList.add("NULL");
        }
    }



    public int evaluate(Data data1, AplicatieData appdata2){
        int value= 0;

       /* if((data1.getActivity() != null ) && (appdata2.getActivity() != null)) {
            if (data1.getActivity() == appdata2.getActivity()) {
                value += 10;
            }
        }

        if(data1.getHeadphoneState() == appdata2.getHeadphoneState()){
            value += 10;
        }

        if(sqrt(pow(data1.getLocationLatitude() - appdata2.getLocationLatitude(),2)+pow(data1.getLocationLongitude() - appdata2.getLocationLongitude(),2))<100){
            value += 10;
        }
*/
        return value;
    }
}
