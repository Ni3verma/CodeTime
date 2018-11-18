package com.Import.codetime.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.Import.codetime.database.ContestEntry;
import com.Import.codetime.model.Contest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DbUtils {
    public static final int TYPE_PAST_EVENTS = 1;
    public static final int TYPE_ONGOING_EVENTS = 2;
    public static final int TYPE_FUTURE_EVENTS = 3;

    public static List<ContestEntry> convertToContestEntryType(List<Contest> list, int type) {
        List<ContestEntry> contestEntryList = new ArrayList<>();
        for (Contest contest : list) {
            String startDate = getFormattedDate(contest.getStart());
            String endDate = getFormattedDate(contest.getEnd());

            ContestEntry contestEntry = new ContestEntry(
                    contest.getEvent(),
                    contest.getHref(),
                    contest.getId(),
                    startDate,
                    endDate,
                    contest.getResource().getName(),
                    type
            );

            contestEntryList.add(contestEntry);
        }
        return contestEntryList;
    }

    public static String getFormattedDate(String date) {
        String s[] = date.split("T"); //as response has time in this format yyyy:MM:ddThh:mm:ss, so we can split date and time at T. now s[0] contains date and s[1] contains time in 24 hour format

        String time[] = s[1].split(":");
        String marker = "AM";
        int hour = Integer.parseInt(time[0]);
        if (hour > 11) {
            marker = "PM";
            if (hour > 12) {
                if (hour - 12 < 10)
                    time[0] = "0" + (hour - 12); // eg: make it 09, instead of 9
                else
                    time[0] = hour - 12 + "";
            }
        }
        return s[0] + "\n" + time[0] + ":" + time[1] + " " + marker;
    }

    public static String getFavouriteResourcesRegex(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> keySet = sharedPreferences.getAll().keySet();
        StringBuilder resourceRegex = new StringBuilder();
        for (String key : keySet) {
            if (sharedPreferences.getBoolean(key, true)) {
                resourceRegex.append(key).append("|");
            }
        }
        if (resourceRegex.length() > 0)
            resourceRegex.deleteCharAt(resourceRegex.length() - 1);   //delete last extra pipe symbol

        return resourceRegex.toString();
    }
}
