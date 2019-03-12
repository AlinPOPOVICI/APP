package com.example.alin.app1.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;




public class DataRepository {

    private DataDao myDao;
    private LiveData<List<Data>> mAllData;

    public DataRepository(Application application) {

        AppDatabase db = AppDatabase.getAppDatabase(application);
        myDao = db.dataDao();
    }


    public LiveData<List<Data>> getAllData() {
        return mAllData;
    }

    public void insert(Data data) {
        new insertAsyncTask(myDao).execute(data);
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