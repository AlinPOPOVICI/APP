package com.example.alin.app1.DB;



import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;


public class DataViewModel extends AndroidViewModel{

    private DataRepository mRepository;
    private List<Data> mAllWords;


    public DataViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllWords = mRepository.getAllData();

    }

    public List<Data> getAllData() {
        return mAllWords;
    }

    public void insert(Data data) {
        mRepository.insert(data);

    }

}