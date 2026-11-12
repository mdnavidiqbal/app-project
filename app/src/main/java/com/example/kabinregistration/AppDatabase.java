package com.example.kabinregistration;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(User user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getById(int id);
}

@Dao
interface KabinDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(KabinApplication application);

    @Query("SELECT * FROM kabin_applications WHERE userId = :userId LIMIT 1")
    KabinApplication getByUserId(int userId);

    @Update
    void update(KabinApplication application);
}

@Database(entities = {User.class, KabinApplication.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract KabinDao kabinDao();

    // Database work must run off the main thread.
    public static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase get(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "kabin_registration.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
