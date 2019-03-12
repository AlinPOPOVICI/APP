/*package com.example.alin.app1.View_Models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.alin.app1.DB.Aplicatie;
import com.example.alin.app1.DB.AplicatieRepository;

import java.util.List;

public class AplicatieViewModel extends AndroidViewModel {

  private AplicatieRepository mRepository;
    private LiveData<List<Aplicatie>> mAllData;

    public AplicatieViewModel(Application application) {
        super(application);
        mRepository = new AplicatieRepository(application);
        mAllData = mRepository.getAllData();

    }

    LiveData<List<Aplicatie>> getAllData() {
        return mAllData;

    }

    void insert(Aplicatie app) {

        mRepository.insert(app);

    }

}

}*/
