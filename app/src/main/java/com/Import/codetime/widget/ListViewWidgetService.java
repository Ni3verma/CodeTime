package com.Import.codetime.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.Import.codetime.ContestDetailActivity;
import com.Import.codetime.R;
import com.Import.codetime.database.AppDatabase;
import com.Import.codetime.database.FavouriteEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ListViewWidgetService extends RemoteViewsService {
    AppDatabase appDatabase;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        final List<FavouriteEntry>[] list = new List[1];
        final CountDownLatch latch = new CountDownLatch(1);

//        appDatabase=AppDatabase.getInstance(this.getApplicationContext());
//        MyDiskExecutor.getsInstance().getDiskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                list[0] =appDatabase.FavouriteDao().getAllFavContests();
//                latch.countDown();
//            }
//        });
//
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        list[0] = getFakeData();

        return new AppWidgetListView(list[0], this.getApplicationContext());
    }

    private List<FavouriteEntry> getFakeData() {
        List<FavouriteEntry> list = new ArrayList<>();
        list.add(new FavouriteEntry(2, "code week 35", "hackerrank1"));
        list.add(new FavouriteEntry(2, "code week 35", "hackerrank1"));
        list.add(new FavouriteEntry(2, "code week 35", "hackerrank1"));
        list.add(new FavouriteEntry(2, "code week 35", "hackerrank1"));
        list.add(new FavouriteEntry(2, "code week 35", "hackerrank1"));

        return list;
    }

    class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory {

        private List<FavouriteEntry> contestList;
        private Context context;

        public AppWidgetListView(List<FavouriteEntry> contestList, Context context) {
            this.contestList = contestList;
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return contestList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            FavouriteEntry contest = contestList.get(position);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);

            views.setTextViewText(R.id.res_name_tv, contest.getResName());
            views.setTextViewText(R.id.contest_name_tv, contest.getName());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(ContestDetailActivity.ID_EXTRA_KEY, contest.getContestId());

            views.setOnClickFillInIntent(R.id.parent_view, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
