package barqsoft.footballscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
    public static String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_PAGER_CURRENT = "Pager_Current";
    private static final String KEY_SELECTED_MATCH = "Selected_match";
    private static final String KEY_PAGER_FRAGMENT = "pagerFragment";

    public static int selectedMatchId;
    public static int currentFragment = 2;
    private PagerFragment pagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        if (savedInstanceState == null) {
            pagerFragment = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, pagerFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent startAbout = new Intent(this, AboutActivity.class);
            startActivity(startAbout);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_PAGER_CURRENT, pagerFragment.mPagerHandler.getCurrentItem());
        outState.putInt(KEY_SELECTED_MATCH, selectedMatchId);
        getSupportFragmentManager().putFragment(outState, KEY_PAGER_FRAGMENT, pagerFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentFragment = savedInstanceState.getInt(KEY_PAGER_CURRENT);
        selectedMatchId = savedInstanceState.getInt(KEY_SELECTED_MATCH);
        pagerFragment = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_PAGER_FRAGMENT);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
