package barqsoft.footballscores;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by martin.andersson on 12/1/15.
 */
public class FootballRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new FootballRemoteViewsFactory(this.getApplicationContext(), intent));
    }

}