package com.example.alin.app1.Services;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.alin.app1.DB.Aplicatie;
import com.example.alin.app1.DB.AplicatieData;
import com.example.alin.app1.DB.AplicatieDataRepository;
import com.example.alin.app1.DB.AplicatieRepository;
import com.example.alin.app1.DB.Data;
import com.example.alin.app1.DB.DataRepository;
import com.example.alin.app1.Widget.WidgetProvider;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SnapshotService extends JobService {
    public static final String CUSTOM_BROADCAST_ACTION = "com.example.alin.app1.Servises.SnapshotService.CUSTOM_BROADCAST_ACTION";
    private Data data;
    private DataRepository mDataRepository;
    private DatabaseReference mDatabase;

    private static final String TAG = "SnapshotService";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10001;
    private GoogleApiClient mGoogleApiClient;
    private boolean isRunning  = false;
    private Looper looper;
    private MyServiceHandler myServiceHandler;
    private FirebaseAuth auth ;
    private FirebaseUser user;
    private int flag_done = 0;
    private FirebaseModelInputOutputOptions inputOutputOptions;
    private FirebaseModelInterpreter firebaseInterpreter;
    private AplicatieRepository mAplicatieRepository ;
    private AplicatieDataRepository mAplicatieDataRepository ;
    private List<AplicatieData> appDataList;
    private String[] title;
    /*@Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/

    @Override
    public void onCreate(){
        super.onCreate();
        HandlerThread handlerthread = new HandlerThread("MyThread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerthread.start();
        looper = handlerthread.getLooper();
        myServiceHandler = new MyServiceHandler(looper);
        isRunning = true;
        mGoogleApiClient = new GoogleApiClient.Builder(SnapshotService.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();

        //getSnapshots();
        mDataRepository = new DataRepository(this.getApplication());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        title = new String[]{
                "Block Puzzle ",
                "Facebook     ",
                "Audible      ",
                "Messenger    ",
                "Food Panda   ",
                "Instagram    ",
                "Tinder       ",
                "MMS          ",
                "CALL         ",
                "Whatsapp     ",
                "Skype        ",
                "Achero       ",
                "Chrome       ",
                "Search Box   ",
                "Wheater      ",
                "Notes        ",
                "Settings     ",
                "Youtube      ",
                "Deskclock    ",
                "Hipmenu      ",
                "Contacts     ",
                "Gallery      ",
                "ING Home Bank",
                "Snapchat     ",
                "Camera       ",
                "NewPipe      ",
                "Uber         ",
                "Docs         ",
                "File Manager ",
                "Maps         ",
                "VLC          ",
                "Home Workout ",
                "Excel        "
        };


    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //Toast.makeText(this, "Getting data", Toast.LENGTH_LONG).show();
        data = new Data();
        getSnapshots();
        //while(flag_done == 0){
        //    Log.i("MAP_SETUP_DATA_007","astept");
       // }
       // Log.i("MAP_SETUP_DATA_007",    data.getTime().toString()+"    "+data.getLocationLatitude()+"   "+data.getLocationLatitude()+"  "+ data.getActivity()+"   "+data.getWeatherCelsius());
        send_broadcast();
        return false;
    }

    @Override
    public void onDestroy() {
      //  WidgetProvider.sendRefreshBroadcast(getApplicationContext());
//        Log.i("MAP_SETUP_DATA_008",    data.getTime().toString()+"    "+data.getLocationLatitude()+"   "+data.getLocationLatitude()+"  "+ data.getActivity()+"   "+data.getWeatherCelsius());
        super.onDestroy();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
       // WidgetProvider.sendRefreshBroadcast(getApplicationContext());
    //    Log.i("MAP_SETUP_DATA_008",    data.getTime().toString()+"    "+data.getLocationLatitude()+"   "+data.getLocationLatitude()+"  "+ data.getActivity()+"   "+data.getWeatherCelsius());

        return false;
    }

    public void getSnapshots() {
        // User Activity
        String appName = "";
        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (!detectedActivityResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not detect user activity");
                            data.setActivity(-13);
                            return;
                        }
                        ActivityRecognitionResult arResult = detectedActivityResult.getActivityRecognitionResult();
                        DetectedActivity probableActivity = arResult.getMostProbableActivity();
                        Log.i(TAG, probableActivity.toString());
                        data.setActivity(probableActivity.getType());
                        getLocation();
                    }
                });

    }

    private void firebase(Data data){
        String uid = "";
        if (user != null) {
            uid = user.getUid();
        }
        DatabaseReference ref = mDatabase.child("users").child(uid).child("Awareness").child(data.getTime().toString());

        //HashMap<String, Data> hashMap = new HashMap<>();
        //hashMap.put("Data", data);
        ref.setValue(data );
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "Fine Location Permission not yet granted");
            /*ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);*/
        } else {
            Log.i(TAG, "Fine Location permission already granted");
            // Location
            Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {
                            if (!locationResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not detect user location");
                                data.setLocationLatitude(-13);
                                data.setLocationLongitude(-13);
                                return;
                            }
                            Location location = locationResult.getLocation();
                            data.setLocationLatitude(location.getLatitude());
                            data.setLocationLongitude(location.getLongitude());
                            Log.i("MAP_SETUP_DATA", location.getLongitude() + "   " + location.getLatitude());
                            flag_done = 1;
                            getWeather();
                        }
                    });
        }
    }

    private void getPlaces() {
        // Check for permission first
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "Fine Location Permission not yet granted");
            /*ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);*/
        } else {
            Log.i(TAG, "Fine Location permission already granted");
            Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<PlacesResult>() {
                        @Override
                        public void onResult(@NonNull PlacesResult placesResult) {
                            if (!placesResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not get places list");
                                return;
                            }

                            List<PlaceLikelihood> placeLikelihoods = placesResult.getPlaceLikelihoods();
                            if (placeLikelihoods != null) {
                                StringBuilder places = new StringBuilder();
                                for (PlaceLikelihood place :
                                        placeLikelihoods) {
                                    Log.i(TAG, place.getPlace().getName().toString() +
                                            "[likelihood = " + place.getLikelihood() + "]");
                                    places.append(place.getPlace().getName().toString() +
                                            "[likelihood = " + place.getLikelihood() + "]\n");
                                }
                            } else {
                                Log.e(TAG, "There is no known place");
                            }
                        }
                    });
        }
    }



    private void getWeather() {
        // Check for permission first
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "Fine Location Permission not yet granted");
            /*ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);*/
        } else {
            Log.i(TAG, "Fine Location permission already granted");


            // Weather
            Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<WeatherResult>() {
                        @Override
                        public void onResult(@NonNull WeatherResult weatherResult) {
                            if (!weatherResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not detect weather info");
                                data.setWeatherCelsius(-13);
                                data.setWeatherCondition(-13);
                                return;
                            }
                            Weather weather = weatherResult.getWeather();
                            if (weather != null) {
                                data.setWeatherCelsius(weatherResult.getWeather().getTemperature(2));
                                data.setWeatherCondition(weatherResult.getWeather().getConditions()[0]);
                                getHeadphones();
                            } else {
                                data.setWeatherCelsius(-13);
                                data.setWeatherCondition(-13);
                            }
                        }
                    });

        }

        Log.i("MAP_SETUP_DATA_2",    data.getLocationLatitude()+"   "+data.getLocationLatitude()+"  "+ data.getActivity()+"   "+data.getWeatherCelsius());

    }
    public void getHeadphones(){ // Headphones
        Awareness.SnapshotApi.getHeadphoneState(mGoogleApiClient)
                .setResultCallback(new ResultCallback<HeadphoneStateResult>() {
                    @Override
                    public void onResult(@NonNull HeadphoneStateResult headphoneStateResult) {
                        if (!headphoneStateResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not detect headphone state");
                            data.setHeadphoneState(-13);
                            return;
                        }
                        HeadphoneState headphoneState = headphoneStateResult.getHeadphoneState();
                        data.setHeadphoneState(headphoneState.getState());
                    }
                });

        // Time (Simply get device time)
        Calendar calendar = Calendar.getInstance();
        data.setTime(calendar.getTime());
        data.setApp_name(getTopAppName(getApplicationContext()));
        Log.i("MAP_SETUP_DATA_1",    data.getTime().toString()+"    "+data.getLocationLatitude()+"   "+data.getLocationLatitude()+"  "+ data.getActivity()+"   "+data.getWeatherCelsius());
        mDataRepository.insert(data);
        firebase(data);
        //updateAppList(data);
        //mAplicatieRepository.deleteAll();
        create_model(data);
        WidgetProvider.sendRefreshBroadcast(getApplicationContext());

    }

    private void updateAppList(Data data) {
        /*Intent schedule_intent = new Intent(getApplicationContext(), SuggestionListService.class);
        schedule_intent.putExtra("data", data);
        getApplicationContext().startService(schedule_intent);*/
        //MyCustomObject myObj = new MyCustomObject("data", data);
        Gson g = new Gson();
        String json = g.toJson(data);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("data", json);
        ComponentName serviceComponent = new ComponentName(this.getApplicationContext(), SuggestionListService.class);
        JobInfo.Builder builder = (JobInfo.Builder) new JobInfo.Builder(0, serviceComponent)
                .setExtras(bundle);
        builder.setMinimumLatency(10 * 1000); // wait at least
        builder.setOverrideDeadline(30 * 1000); // maximum dela
        JobScheduler jobScheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            jobScheduler = this.getApplicationContext().getSystemService(JobScheduler.class);
        }

        jobScheduler.schedule(builder.build());
    }

    public void send_broadcast() {
        Log.i(TAG, "Snapshot Broadcast");
        Intent intent = new Intent(CUSTOM_BROADCAST_ACTION);
        sendBroadcast(intent, "com.example.alin.app1.FenceBroadcastReceiver");
    }
    private void schedule_snapshot(Context context , int t ){
        Calendar cal = Calendar.getInstance();
        Intent schedule_intent = new Intent(context, SnapshotService.class);
        PendingIntent pintent = PendingIntent.getService(context, 1, schedule_intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), t, pintent);
    }

    private final class MyServiceHandler extends Handler {
        private static final String TAG = "MyServiceHandler";
        public MyServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
                for (int i = 0; i < 10; i++) {
                    try {
                        Log.i(TAG, "MyService running...");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    if(!isRunning){
                        break;
                    }
                }
            }
            //stops the service for the start id.
            stopSelfResult(msg.arg1);
        }
    }

    public static String getTopAppName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String strName = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String currentApp = null;
                UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                long time_end = System.currentTimeMillis();
                long time_begin = time_end - 1000*1000;
                List<UsageStats> applist = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time_begin, time_end);
                if (applist == null) {
                    Log.i(TAG, "Current App list is NULL ");
                }
                if (applist.size() == 0) {
                    Log.i(TAG, "Current App list is 0 ");
                }

                if (applist != null && applist.size() > 0) {
                    Log.i(TAG, "Current App list is not NULL ");
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                   // Iterator<Map.Entry<Long, UsageStats>> it = mySortedMap.entrySet().iterator();

                    for (UsageStats usageStats : applist) {
                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (mySortedMap != null && !mySortedMap.isEmpty()) {

                        /*for (UsageStats us: ((TreeMap<Long, UsageStats>) mySortedMap).descendingMap().values()) {
                            currentApp = us.getPackageName();
                            if(!(currentApp.equals("com.miui.home")||currentApp.equals("com.example.alin.app1"))){
                            break;
                            }
                        }*/
                        currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();

                        while ((currentApp.equals("com.miui.home")||currentApp.equals("com.example.alin.app1"))){
                            mySortedMap.remove(mySortedMap.lastKey());
                            currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                        }
                    }
                }
                Log.i(TAG, "Current App in foreground is: " + currentApp);

                return currentApp;

            //Log.i(TAG, "Got top app name through LOLIPOP: " + strName);
            } else {
                strName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
                Log.i(TAG, "Got top app name : " + strName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strName;
    }
    void create_model(Data ddata){

        mAplicatieDataRepository = new AplicatieDataRepository(this.getApplication());
        mAplicatieRepository = new AplicatieRepository(this.getApplication());
        mAplicatieRepository.deleteAll();
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
        predict(ddata);
    }

    void predict(Data ddata){
        //get latest db entry
        float[][] input = new float[1][7];

        if(ddata != null) {
            input = prepare_data(ddata);
            update(ddata);
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
                                    int i;
                                    for ( i = 0; i<=31; i = i+1) {
                                        Log.i(TAG, "OUTPUT: " + title[i] + output[0][i]*100);
                                        Aplicatie app = new Aplicatie();
                                        app.setName(title[i]);
                                        app.setPrioritate(Math.round(output[0][i]*100));
                                        //appList.add(app);
                                        mAplicatieRepository.insert(app);
                                        //WidgetProvider.sendRefreshBroadcast(getApplicationContext());
                                    }
                                   // if(i == 31)
                                      //  WidgetProvider.sendRefreshBroadcast(getApplicationContext());
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
    void update(Data ddata){
        appDataList =  mAplicatieDataRepository.getAllData();

        Log.i(TAG,"updateLIST ");
        if(appDataList != null ){

            if(appDataList.size() > 0) {
                for (int i = 0; i < appDataList.size(); i++) {
                    AplicatieData appdata = appDataList.get(i);
                    Aplicatie app = new Aplicatie();
                    app.setName(appdata.getName());

                    app.setPrioritate(evaluate(ddata,appdata));
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
       // WidgetProvider.sendRefreshBroadcast(getApplicationContext());

    }



    public int evaluate(Data data1, AplicatieData appdata2){
        int value= 0;
        Log.i(TAG,"evaluate ");
        if (data1 != null && appdata2 !=null){
            if(data1.getTime() != null ) {
                if (Math.abs(data1.getTime().getHours() - appdata2.getTime().getHours()) <= 1) {
                    return 500000;
                }
            }
           if((data1.getLocationLatitude() != 0) && (appdata2.getLocationLatitude() != 0) && (data1.getLocationLongitude() != 0) && (appdata2.getLocationLongitude() != 0)) {

                if (sqrt(pow(data1.getLocationLatitude() - appdata2.getLocationLatitude(), 2) + pow(data1.getLocationLongitude() - appdata2.getLocationLongitude(), 2)) < 1) {
                    return 500001;
                }
            }
       }
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

