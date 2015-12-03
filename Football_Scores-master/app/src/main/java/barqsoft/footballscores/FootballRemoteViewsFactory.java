package barqsoft.footballscores;

/**
 * Created by martin.andersson on 12/1/15.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class FootballRemoteViewsFactory implements RemoteViewsFactory {
    public static final String TAG = FootballRemoteViewsFactory.class.getSimpleName();

    public static final int DAY_BEFORE_YESTERDAY = -2;
    public static final int YESTERDAY = -1;
    public static final int TODAY = 0;
    public static final int TOMORROW = 1;
    public static final int DAY_AFTER_TOMORROW = 2;

    private Context mContext = null;
    private int mAppWidgetId;
    private Cursor mCursor;

    public FootballRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        // No need to do anything here since we setup cursor in onDataSetChanged
    }


    @Override
    public int getCount() {
        int count = mCursor.getCount();
        Log.d(TAG, "getCount: " + count);
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt: " + position);

        final RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_row);

        if (mCursor.moveToPosition(position)) {

            remoteView.setTextViewText(R.id.heading, mCursor.getString(FootballScoresAdapter.COL_HOME));
            remoteView.setTextViewText(R.id.content, mCursor.getString(FootballScoresAdapter.COL_AWAY));

        } else {
            Log.w(TAG, "FAILED TO move cursor to position: " + position);
        }


        return remoteView;
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }


        // TODO, should we get all matches? Matches by date?
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//        String[] selectionArgs = new String[1];
//        selectionArgs[0] = simpleDateFormat.format(new Date(System.currentTimeMillis() + ((TODAY) * 86400000)));
//        selectionArgs[0] = simpleDateFormat.format(new Date(System.currentTimeMillis() + ((TOMORROW) * 86400000)));
//        selectionArgs[0] = simpleDateFormat.format(new Date(System.currentTimeMillis() + ((DAY_AFTER_TOMORROW) * 86400000)));
//        mCursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, selectionArgs, null);

        mCursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildScores(), null, null, null, null);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (mCursor != null) {
            mCursor.close();
        }
    }

}