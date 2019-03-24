package com.example.alin.app1.DB;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class AplicatieDataRepository {

    private static final String TAG = "AP_DATA_REPOSITORY";

    private AplicatieDataDao myADao;
    //private LiveData<List<Aplicatie>> mAllData ;
    private List<AplicatieData> mAllData ;

    public AplicatieDataRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        myADao =  db.aplicatieDataDao();
        mAllData = myADao.getAll();

    }


    //public LiveData<List<Aplicatie>> getAllData() {
    public List<AplicatieData> getAllData() {
        Log.i(TAG,"getALL ");
        return mAllData;
    }

    public void insert(AplicatieData app) {
        Log.i(TAG,"insert");
        new insertAsyncTask(myADao).execute(app);
    }
    public void deleteAll() {
        Log.i(TAG,"deleteAll");
        myADao.deleteAll();
    }
    public void delete(AplicatieData data) {
        Log.i(TAG,"delete");
        myADao.delete(data);
    }


    private static class insertAsyncTask extends AsyncTask<AplicatieData, Void, Void> {

        private AplicatieDataDao mAsyncTaskDao;

        insertAsyncTask(AplicatieDataDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AplicatieData... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

