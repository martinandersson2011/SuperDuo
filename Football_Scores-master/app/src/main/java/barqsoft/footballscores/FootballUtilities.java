package barqsoft.footballscores;

import android.content.Context;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public final class FootballUtilities {
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;

    public static final String ARSENAL_LONDON_FC = "Arsenal London FC";
    public static final String MANCHESTER_UNITED_FC = "Manchester United FC";
    public static final String SWANSEA_CITY = "Swansea City";
    public static final String LEICESTER_CITY = "Leicester City";
    public static final String EVERTON_FC = "Everton FC";
    public static final String WEST_HAM_UNITED_FC = "West Ham United FC";
    public static final String TOTTENHAM_HOTSPUR_FC = "Tottenham Hotspur FC";
    public static final String WEST_BROMWICH_ALBION = "West Bromwich Albion";
    public static final String SUNDERLAND_AFC = "Sunderland AFC";
    public static final String STOKE_CITY_FC = "Stoke City FC";

    private FootballUtilities() {
        // Hidden constructor
    }

    public static String getLeague(Context context, int leagueNum) {
        switch (leagueNum) {
            case SERIE_A:
                return context.getString(R.string.seriaa);
            case PREMIER_LEGAUE:
                return context.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE:
                return context.getString(R.string.champions_league);
            case PRIMERA_DIVISION:
                return context.getString(R.string.primeradivison);
            case BUNDESLIGA:
                return context.getString(R.string.bundesliga);
            default:
                return context.getString(R.string.unknown_league);
        }
    }

    public static String getMatchDay(Context context, int matchDay, int leagueNum) {
        if (leagueNum == CHAMPIONS_LEAGUE) {
            if (matchDay <= 6) {
                return context.getString(R.string.group_stages_matchday_6);
            } else if (matchDay == 7 || matchDay == 8) {
                return context.getString(R.string.first_knockout_round);
            } else if (matchDay == 9 || matchDay == 10) {
                return context.getString(R.string.quarter_final);
            } else if (matchDay == 11 || matchDay == 12) {
                return context.getString(R.string.semi_final);
            } else {
                return context.getString(R.string.final_text);
            }
        } else {
            return String.format(context.getString(R.string.matchday_text), String.valueOf(matchDay));
        }
    }

    public static String getScores(int homeGoals, int awayGoals) {
        if (homeGoals < 0 || awayGoals < 0) {
            return " - ";
        } else {
            return String.valueOf(homeGoals) + " - " + String.valueOf(awayGoals);
        }
    }

    public static int getTeamCrestByTeamName(String teamName) {
        if (teamName == null) {
            return R.drawable.no_icon;
        }
        switch (teamName) {
            // This is the set of icons that are currently in the app. Feel free to find and add more.
            case ARSENAL_LONDON_FC:
                return R.drawable.arsenal;
            case MANCHESTER_UNITED_FC:
                return R.drawable.manchester_united;
            case SWANSEA_CITY:
                return R.drawable.swansea_city_afc;
            case LEICESTER_CITY:
                return R.drawable.leicester_city_fc_hd_logo;
            case EVERTON_FC:
                return R.drawable.everton_fc_logo1;
            case WEST_HAM_UNITED_FC:
                return R.drawable.west_ham;
            case TOTTENHAM_HOTSPUR_FC:
                return R.drawable.tottenham_hotspur;
            case WEST_BROMWICH_ALBION:
                return R.drawable.west_bromwich_albion_hd_logo;
            case SUNDERLAND_AFC:
                return R.drawable.sunderland;
            case STOKE_CITY_FC:
                return R.drawable.stoke_city;
            default:
                return R.drawable.no_icon;
        }
    }
}
