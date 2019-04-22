package com.example.alin.app1.DB;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alin.app1.DateConverter;

import java.util.Date;
import java.util.List;




public class DataRepository {

    private static final String TAG ="DataRepository";
    private DataDao myDao;
    private DateConverter conv;
   // private List<Data> mAllData;

    public DataRepository(Application application) {

        AppDatabase db = AppDatabase.getAppDatabase(application);
        myDao = db.dataDao();
    }

    public Data findByDate(Date date){
        Log.i(TAG,"getDate");
        return myDao.findByDate(conv.toTimestamp(date)) ;

    }
    public List<Data> getAllData() {
        Log.i(TAG,"getAll");
        return myDao.getAll();
    }

    public void insert(Data data) {
        Log.i(TAG,"insert");
        new insertAsyncTask(myDao).execute(data);
    }

    public void deleteAll() {
        Log.i(TAG,"deleteAll");
        myDao.deleteAll();
    }
    public void delete(Data data) {
        Log.i(TAG,"delete");
        myDao.delete(data);
    }


    private static class insertAsyncTask extends AsyncTask<Data, Void, Void> {

        private DataDao mAsyncTaskDao;

        insertAsyncTask(DataDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Data... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}