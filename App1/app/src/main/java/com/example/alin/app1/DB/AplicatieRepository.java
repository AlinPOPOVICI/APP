package com.example.alin.app1.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AplicatieRepository {

        private AplicatieDao myADao;
        private LiveData<List<Aplicatie>> mAllData;

        public AplicatieRepository(Application application) {

            AppDatabase db = AppDatabase.getAppDatabase(application);
            myADao =  db.aplicatieDao();
        }


        public LiveData<List<Aplicatie>> getAllData() {
            return mAllData;
        }

        public void insert(Aplicatie app) {
            new insertAsyncTask(myADao).execute(app);
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

