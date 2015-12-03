package barqsoft.footballscores;

/**
 * Created by martin.andersson on 12/1/15.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FootballRemoteViewsFactory implements RemoteViewsFactory {
    public static final String TAG = FootballRemoteViewsFactory.class.getSimpleName();

    public static final boolean ONLY_SHOW_TODAYS_MATCHES = false;
    public static final int TODAY = 0;

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
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        final RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_row);

        if (mCursor.moveToPosition(position)) {

            remoteView.setTextViewText(R.id.match_time, mCursor.getString(FootballScoresAdapter.COL_MATCHTIME));
            remoteView.setTextViewText(R.id.home_team_name, mCursor.getString(FootballScoresAdapter.COL_HOME));
            remoteView.setTextViewText(R.id.away_team_name, mCursor.getString(FootballScoresAdapter.COL_AWAY));
            remoteView.setTextViewText(R.id.score, FootballUtilities.getScores(mCursor.getInt(FootballScoresAdapter.COL_HOME_GOALS), mCursor.getInt(FootballScoresAdapter.COL_AWAY_GOALS)));
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

        if (ONLY_SHOW_TODAYS_MATCHES) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String[] selectionArgs = new String[1];
            selectionArgs[0] = simpleDateFormat.format(new Date(System.currentTimeMillis() + ((TODAY) * 86400000)));
            mCursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, selectionArgs, null);
        } else {
            mCursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildScores(), null, null, null, null);
        }

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

}