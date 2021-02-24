package com.example.keytronome.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.keytronome.models.KeytronomeModel;

@Database(entities = Keytronome.class, version = 2)
public abstract class KeytronomeRoomDatabase extends RoomDatabase {

    public abstract KeytronomeDao keytronomeDao();

    private static volatile KeytronomeRoomDatabase keytronomeRoomInstance;

    public static KeytronomeRoomDatabase getDatabase(final Context context) {
        if (keytronomeRoomInstance == null) {
            synchronized (KeytronomeRoomDatabase.class) {
                if (keytronomeRoomInstance == null) {
                    keytronomeRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            KeytronomeRoomDatabase.class, "preset_database")
                            .build();
                }
            }
        }
        return keytronomeRoomInstance;
    }
}
