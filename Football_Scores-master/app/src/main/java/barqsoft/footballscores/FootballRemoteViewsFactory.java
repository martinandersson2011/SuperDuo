package barqsoft.footballscores;

/**
 * Created by martin.andersson on 12/1/15.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.ArrayList;

public class FootballRemoteViewsFactory implements RemoteViewsFactory {
    private ArrayList<ListItem> mListItems = new ArrayList<ListItem>();
    private Context mContext = null;
    private int mAppWidgetId;

    public FootballRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source.
        // Heavy lifting, for example downloading or creating content etc, should be deferred to onDataSetChanged() or getViewAt().
        // Taking more than 20 seconds in this call will result in an ANR.

        for (int i = 0; i < 100; i++) {
            ListItem listItem = new ListItem();
            listItem.heading = "Heading " + i;
            listItem.content = "Content " + i;
            mListItems.add(listItem);
        }
    }


    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_row);
        ListItem listItem = mListItems.get(position);
        remoteView.setTextViewText(R.id.heading, listItem.heading);
        remoteView.setTextViewText(R.id.content, listItem.content);

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
    }

    @Override
    public void onDestroy() {
    }

}