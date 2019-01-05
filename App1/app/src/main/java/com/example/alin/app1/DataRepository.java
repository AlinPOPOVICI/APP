package com.example.alin.app1;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;




class DataRepository {

    private DataDao myDao;
    private LiveData<List<Data>> mAllData;

    DataRepository(Application application) {

        AppDatabase db = AppDatabase.getAppDatabase(application);
        myDao = db.dataDao();
    }


    LiveData<List<Data>> getAllData() {
        return mAllData;
    }

    void insert(Data data) {
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