package com.example.alin.app1.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.alin.app1.DB.Data;
import com.example.alin.app1.DB.DataRepository;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ToCSVService extends Service {
    private DataRepository mDataRepository;

    public ToCSVService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mDataRepository = new DataRepository(this.getApplication());
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "AnalysisData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer;



        if (f.exists() && !f.isDirectory()) {
            FileWriter mFileWriter = null;
            try {
                mFileWriter = new FileWriter(filePath, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer = new CSVWriter(mFileWriter);
            print(writer);

        } else {
            try {
                writer = new CSVWriter(new FileWriter(filePath));
                print(writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return super.onStartCommand(intent, flags, startId);
    }


    private void print(CSVWriter wrt) {
        List<Data> list = mDataRepository.getAllData();
        if (list != null && wrt != null) {
            for (int i = 0; i < list.size(); i++) {
                String[] data = {list.get(i).getTime().toString(), list.get(i).getApp_name() , ""+list.get(i).getActivity(), "" + list.get(i).getHeadphoneState(), "" + list.get(i).getLocationLatitude(), "" + list.get(i).getLocationLongitude(), "" + list.get(i).getWeatherCelsius(), "" + list.get(i).getWeatherCondition()};
                // File exist
                Log.i("CSV", data[0]+"  "+data[1]+"  "+data[2]+"  "+data[3]+"  "+data[4]+"  "+data[5]+"  "+data[6]+"  "+data[7]);
                wrt.writeNext(data);

            }
        }
        try {
            wrt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}