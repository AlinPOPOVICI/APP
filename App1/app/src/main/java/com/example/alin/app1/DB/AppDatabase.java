package com.example.alin.app1.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Data.class, Aplicatie.class}, version =2)
public abstract class AppDatabase extends RoomDatabase {

        private static AppDatabase INSTANCE;

        public abstract DataDao dataDao();
        public abstract AplicatieDao aplicatieDao();

        public static AppDatabase getAppDatabase(Context context) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "data-database")
                                // allow queries on the main thread.
                                // Don't do this on a real app! See PersistenceBasicSample for an example.
                                .allowMainThreadQueries()
                                .fallbackToDestructiveMigration()
                                //.addCallback(sRoomDatabaseCallback)
                                .build();
            }
            return INSTANCE;
        }

        public static void destroyInstance() {
            INSTANCE = null;
        }
}

