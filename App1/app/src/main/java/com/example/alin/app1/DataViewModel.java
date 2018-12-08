package com.example.alin.app1;



import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import java.util.List;


public class DataViewModel extends AndroidViewModel{

    private DataRepository mRepository;
    private LiveData<List<Data>> mAllWords;


    public DataViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mAllWords = mRepository.getAllData();

    }

    LiveData<List<Data>> getAllData() {
        return mAllWords;
    }

    void insert(Data data) {
        mRepository.insert(data);

    }

}