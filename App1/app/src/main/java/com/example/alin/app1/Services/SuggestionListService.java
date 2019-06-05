package com.example.alin.app1.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.alin.app1.DB.Aplicatie;
import com.example.alin.app1.DB.AplicatieData;
import com.example.alin.app1.DB.AplicatieDataRepository;
import com.example.alin.app1.DB.AplicatieRepository;
import com.example.alin.app1.DB.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.common.modeldownload.FirebaseRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SuggestionListService extends JobService {
    private AplicatieRepository mAplicatieRepository ;
    private AplicatieDataRepository mAplicatieDataRepository ;
    private List<Aplicatie> appList;
    private List<AplicatieData> appDataList;
   private List<String> sList = new ArrayList<String>();
   private FirebaseModelInputOutputOptions inputOutputOptions;
   private FirebaseModelInterpreter firebaseInterpreter;

    private static final String TAG = "SUGGESTION";
    public SuggestionListService() {
    }

   /* @Overrid
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        mAplicatieDataRepository = new AplicatieDataRepository(this.getApplication());
        mAplicatieRepository = new AplicatieRepository(this.getApplication());

        //ML---------------------------------------------------------------------------------------------
        FirebaseModelDownloadConditions.Builder conditionsBuilder =
                new FirebaseModelDownloadConditions.Builder().requireWifi();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Enable advanced conditions on Android Nougat and newer.
            conditionsBuilder = conditionsBuilder
                    .requireCharging()
                    .requireDeviceIdle();
        }
        FirebaseModelDownloadConditions conditions = conditionsBuilder.build();

        FirebaseRemoteModel cloudSource = new FirebaseRemoteModel.Builder("my_model_v1")
                .enableModelUpdates(true)
                .setInitialDownloadConditions(conditions)
                .setUpdatesDownloadConditions(conditions)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            FirebaseModelManager.getInstance().registerRemoteModel(cloudSource);
        }
        //-------------------------------------------------------------------------------------------------
        FirebaseLocalModel localSource =
                new FirebaseLocalModel.Builder("my_model_local_v1")  // Assign a name to this model
                        .setAssetFilePath("my_model_local_v1.tflite")
                        .build();
        FirebaseModelManager.getInstance().registerLocalModel(localSource);
        //-------------------------------------------------------------------------------------------------
        FirebaseModelOptions options = new FirebaseModelOptions.Builder()
                //.setRemoteModelName("my_model_v1")
                .setLocalModelName("my_model_local_v1")
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                 firebaseInterpreter = FirebaseModelInterpreter.getInstance(options);
            } catch (FirebaseMLException e) {
                e.printStackTrace();
            }
        }
        try {
             inputOutputOptions =
                    new FirebaseModelInputOutputOptions.Builder()
                            .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 7})
                            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1,32})
                            .build();
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onStartJob(JobParameters params){
        Data data = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            String json = params.getExtras().getString("data");
            Gson g = new Gson();
            data = g.fromJson(json, Data.class);
            //data = (Data) params.getExtras("data");
            Log.i(TAG, "Got data");
        }
        //get latest db entry
        float[][] input = new float[1][7];

        if(data != null) {
            input = prepare_data(data);
            //updateAppList(data);
        }
        else{
            Log.i(TAG, "Null DATA");
        }

        FirebaseModelInputs inputs = null;
        try {
            inputs = new FirebaseModelInputs.Builder()
                    .add(input)  // add() as many input arrays as your model requires
                    .build();
            Log.i(TAG, "Modified inputs");
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
        try {
            if(firebaseInterpreter == null) {
                Log.i(TAG, "NULL Interpreter");
            }
            firebaseInterpreter.run(inputs, inputOutputOptions)
                    .addOnSuccessListener(
                            new OnSuccessListener<FirebaseModelOutputs>() {
                                @Override
                                public void onSuccess(FirebaseModelOutputs result) {
                                    // ...
                                    float[][] output = result.getOutput(0);
                                    for (int i = 0; i<=31; i = i+1) {
                                        Log.i(TAG, "OUTPUT: " + output[0][i]);

                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "FAil Run"+"   "+"    "+e.getCause());

                                    // Task failed with an exception
                                    // ...
                                }
                            });

        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }
         //updateAppList(data);
       // return android.app.Service.START_STICKY;
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private float[][] prepare_data(Data d){
        float[][] input = new float[1][7];
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        // input[0][0]= d.getTime();
        input[0][0]= (float) currentHourIn24Format;
        Log.i(TAG, "HOUR: " + input[0][0]);
        input[0][1]= d.getActivity();
        Log.i(TAG, "Activity: " +input[0][1]);
        input[0][2]= d.getHeadphoneState();
        Log.i(TAG, "HP: " + input[0][2]);
        input[0][4]= (float) d.getLocationLongitude();
        Log.i(TAG, "Lon: "+ input[0][4]);
        input[0][3]= (float) d.getLocationLatitude();
        Log.i(TAG, "Lat: " +input[0][3]);
        input[0][5]= d.getWeatherCelsius();
        Log.i(TAG, "W_t: " + input[0][5]);
        input[0][6]= d.getWeatherCondition();
        Log.i(TAG, "W_c: " + input[0][6]);
        return input;
    }

    public void updateAppList(Data data){
        mAplicatieRepository.deleteAll();
        appDataList =  mAplicatieDataRepository.getAllData();

        Log.i(TAG,"updateLIST ");
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
        Log.i("SUGGESTION ","evaluate ");

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
