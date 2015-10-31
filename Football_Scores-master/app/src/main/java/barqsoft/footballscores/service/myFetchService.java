package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by yehya khaled on 3/2/2015.
 */
public class MyFetchService extends IntentService {
    public static final String TAG = MyFetchService.class.getSimpleName();

    public MyFetchService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getData("n2");
        getData("p2");

        return;
    }

    private void getData(String timeFrame) {
        //Creating fetch URL
        final String BASE_URL = "http://api.football-data.org/alpha/fixtures"; //Base URL
        final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days
        //final String QUERY_MATCH_DAY = "matchday";

        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();
        //Log.v(TAG, "The url we are looking at is: "+fetch_build.toString()); //log spam
        HttpURLConnection httpUrlConnection = null;
        BufferedReader reader = null;
        String jsonData = null;
        //Opening Connection
        try {
            URL fetch = new URL(uri.toString());
            httpUrlConnection = (HttpURLConnection) fetch.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.addRequestProperty("X-Auth-Token", getString(R.string.api_key));
            httpUrlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = httpUrlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            jsonData = buffer.toString();
        } catch (Exception e) {
            Log.e(TAG, "Exception here: " + e.getMessage());
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error Closing Stream");
                }
            }
        }
        try {
            if (jsonData != null) {
                //This bit is to check if the data contains any matches. If not, we call processJson on the dummy data
                JSONArray matches = new JSONObject(jsonData).getJSONArray("fixtures");
                if (matches.length() == 0) {
                    //if there is no data, call the function on dummy data
                    //this is expected behavior during the off season.
                    processJSONdata(getString(R.string.dummy_data), getApplicationContext(), false);
                    return;
                }

                processJSONdata(jsonData, getApplicationContext(), true);
            } else {
                //Could not Connect
                Log.d(TAG, "Could not connect to server.");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void processJSONdata(String JSONdata, Context mContext, boolean isReal) {
        //JSON data
        // This set of league codes is for the 2015/2016 season. In fall of 2016, they will need to
        // be updated. Feel free to use the codes
        final String BUNDESLIGA1 = "394";
        final String BUNDESLIGA2 = "395";
        final String LIGUE1 = "396";
        final String LIGUE2 = "397";
        final String PREMIER_LEAGUE = "398";
        final String PRIMERA_DIVISION = "399";
        final String SEGUNDA_DIVISION = "400";
        final String SERIE_A = "401";
        final String PRIMERA_LIGA = "402";
        final String Bundesliga3 = "403";
        final String EREDIVISIE = "404";

        final String SEASON_LINK = "http://api.football-data.org/alpha/soccerseasons/";
        final String MATCH_LINK = "http://api.football-data.org/alpha/fixtures/";
        final String FIXTURES = "fixtures";
        final String LINKS = "_links";
        final String SOCCER_SEASON = "soccerseason";
        final String SELF = "self";
        final String MATCH_DATE = "date";
        final String HOME_TEAM = "homeTeamName";
        final String AWAY_TEAM = "awayTeamName";
        final String RESULT = "result";
        final String HOME_GOALS = "goalsHomeTeam";
        final String AWAY_GOALS = "goalsAwayTeam";
        final String MATCH_DAY = "matchday";

        //Match data
        String mLeague = null;
        String mDate = null;
        String mTime = null;
        String mHome = null;
        String mAway = null;
        String mHomeGoals = null;
        String mAwayGoals = null;
        String mMatchId = null;
        String mMatchDay = null;

        try {
            JSONArray matches = new JSONObject(JSONdata).getJSONArray(FIXTURES);

            //ContentValues to be inserted
            Vector<ContentValues> values = new Vector<ContentValues>(matches.length());
            for (int i = 0; i < matches.length(); i++) {

                JSONObject matchData = matches.getJSONObject(i);
                mLeague = matchData.getJSONObject(LINKS).getJSONObject(SOCCER_SEASON).getString("href");
                mLeague = mLeague.replace(SEASON_LINK, "");
                //This if statement controls which leagues we're interested in the data from.
                //add leagues here in order to have them be added to the DB.
                // If you are finding no data in the app, check that this contains all the leagues.
                // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
                if (mLeague.equals(PREMIER_LEAGUE) ||
                        mLeague.equals(SERIE_A) ||
                        mLeague.equals(BUNDESLIGA1) ||
                        mLeague.equals(BUNDESLIGA2) ||
                        mLeague.equals(PRIMERA_DIVISION)) {
                    mMatchId = matchData.getJSONObject(LINKS).getJSONObject(SELF).getString("href");
                    mMatchId = mMatchId.replace(MATCH_LINK, "");
                    if (!isReal) {
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        mMatchId = mMatchId + Integer.toString(i);
                    }

                    mDate = matchData.getString(MATCH_DATE);
                    mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
                    mDate = mDate.substring(0, mDate.indexOf("T"));
                    SimpleDateFormat matchDate = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    matchDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date parseddate = matchDate.parse(mDate + mTime);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                        simpleDateFormat.setTimeZone(TimeZone.getDefault());
                        mDate = simpleDateFormat.format(parseddate);
                        mTime = mDate.substring(mDate.indexOf(":") + 1);
                        mDate = mDate.substring(0, mDate.indexOf(":"));

                        if (!isReal) {
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentDate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            mDate = dateFormat.format(fragmentDate);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "error here!");
                        Log.e(TAG, e.getMessage());
                    }
                    mHome = matchData.getString(HOME_TEAM);
                    mAway = matchData.getString(AWAY_TEAM);
                    mHomeGoals = matchData.getJSONObject(RESULT).getString(HOME_GOALS);
                    mAwayGoals = matchData.getJSONObject(RESULT).getString(AWAY_GOALS);
                    mMatchDay = matchData.getString(MATCH_DAY);
                    ContentValues matchValues = new ContentValues();
                    matchValues.put(DatabaseContract.scores_table.MATCH_ID, mMatchId);
                    matchValues.put(DatabaseContract.scores_table.DATE_COL, mDate);
                    matchValues.put(DatabaseContract.scores_table.TIME_COL, mTime);
                    matchValues.put(DatabaseContract.scores_table.HOME_COL, mHome);
                    matchValues.put(DatabaseContract.scores_table.AWAY_COL, mAway);
                    matchValues.put(DatabaseContract.scores_table.HOME_GOALS_COL, mHomeGoals);
                    matchValues.put(DatabaseContract.scores_table.AWAY_GOALS_COL, mAwayGoals);
                    matchValues.put(DatabaseContract.scores_table.LEAGUE_COL, mLeague);
                    matchValues.put(DatabaseContract.scores_table.MATCH_DAY, mMatchDay);
                    //log spam

                    //Log.v(TAG,matchId);
                    //Log.v(TAG,mDate);
                    //Log.v(TAG,mTime);
                    //Log.v(TAG,Home);
                    //Log.v(TAG,Away);
                    //Log.v(TAG,Home_goals);
                    //Log.v(TAG,Away_goals);

                    values.add(matchValues);
                }
            }
            int insertedData = 0;
            ContentValues[] insert_data = new ContentValues[values.size()];
            values.toArray(insert_data);
            insertedData = mContext.getContentResolver().bulkInsert(
                    DatabaseContract.BASE_CONTENT_URI, insert_data);

            //Log.v(TAG,"Succesfully Inserted : " + String.valueOf(insertedData));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}

