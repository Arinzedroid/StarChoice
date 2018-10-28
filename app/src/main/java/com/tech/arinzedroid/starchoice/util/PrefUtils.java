package com.tech.arinzedroid.starchoice.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    private Activity activity;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static final String AGENT_ID = "AGENT_ID";

    public PrefUtils(Activity activity){
        this.activity = activity;
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setAgentId(String agentId){
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = prefs.edit();
        editor.putString(AGENT_ID,agentId);
        editor.apply();
    }

    public String getAgentId(){
        return prefs.getString(AGENT_ID,"");
    }
}
