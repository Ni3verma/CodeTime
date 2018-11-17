package com.Import.codetime.work;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.Import.codetime.R;
import com.Import.codetime.database.AppDatabase;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

//todo: change this class. it is just to check periodic work
public class FetchDataWork extends Worker {
    Context mContext;

    public FetchDataWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Database will be cleared after every 24 hours so next time user opens app, new fresh data will be fetched
        AppDatabase appDatabase = AppDatabase.getInstance(mContext);
        appDatabase.ContestDao().deleteAllContests();
        showNotification();
        return Result.SUCCESS;
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channelId", "name", NotificationManager.IMPORTANCE_HIGH);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "channelId")
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_home)
                .setContentTitle("CodeTime")
                .setContentText("Database cleared")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setTicker("app");

        assert notificationManager != null;
        notificationManager.notify(154, builder.build());
    }
}
