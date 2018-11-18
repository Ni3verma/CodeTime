package com.Import.codetime.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.Import.codetime.R;

public class WidgetUpdateService extends IntentService {
    public static final String ACTION_UPDATE_LIST_VIEW = "update widget list view";

    public WidgetUpdateService() {
        super("codetime widget service");
    }

    public static void startActionUpdateAppWidget(Context context) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_LIST_VIEW);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            if (action != null && action.equals(ACTION_UPDATE_LIST_VIEW)) {
                handleActionUpdateListView();
            }
        }
    }

    private void handleActionUpdateListView() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavContestsWidgetProvider.class));

        FavContestsWidgetProvider.updateAllAppWidget(this, appWidgetManager, appWidgetIds);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
    }
}
