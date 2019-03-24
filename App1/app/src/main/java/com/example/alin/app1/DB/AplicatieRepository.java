package com.example.alin.app1.DB;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class AplicatieRepository {
     private static final String TAG = "AP_REPOSITORY";

     private AplicatieDao myADao;
     //private LiveData<List<Aplicatie>> mAllData ;
    //private List<Aplicatie>mAllData ;

     public AplicatieRepository(Application application) {
            AppDatabase db = AppDatabase.getAppDatabase(application);
            myADao =  db.aplicatieDao();

     }


        //public LiveData<List<Aplicatie>> getAllData() {
    public List<Aplicatie> getAllData() {
        Log.i(TAG,"getALL ");
        return myADao.getAll();
    }
    public List<Aplicatie> getSortedData() {
        Log.i(TAG,"getALL ");
        return myADao.sortedFind();
    }

        public void insert(Aplicatie app) {
            Log.i(TAG,"insert");
            new insertAsyncTask(myADao).execute(app);
        }
    public void deleteAll() {
        Log.i(TAG,"deleteAll");
        myADao.deleteAll();
    }
    public void delete(Aplicatie data) {
            Log.i(TAG,"delete");
        myADao.delete(data);
    }



        private static class insertAsyncTask extends AsyncTask<Aplicatie, Void, Void> {

            private AplicatieDao mAsyncTaskDao;

            insertAsyncTask(AplicatieDao dao) {
                mAsyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final Aplicatie... params) {
                mAsyncTaskDao.insert(params[0]);
                return null;
            }
        }
    }

