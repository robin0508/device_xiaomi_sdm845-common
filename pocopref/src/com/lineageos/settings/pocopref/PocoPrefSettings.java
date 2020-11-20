/*
 *  Poco Extras Settings Module
 *  Made by @shivatejapeddi 2019
 */

package com.lineageos.settings.pocopref;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import com.lineageos.settings.pocopref.SecureSettingListPreference;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

import android.util.Log;
import android.os.SystemProperties;
import java.io.*;
import android.widget.Toast;

import com.lineageos.settings.pocopref.R;

public class PocoPrefSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {
	private static final boolean DEBUG = false;
	private static final String TAG = "PocoPref";
    public static final String DEFAULT_PERF_PROFILE = "default_perf_profile";
    public static final String PERFORMANCE_SYSTEM_PROPERTY = "persist.perf.default";
    public static final String DEFAULT_THERMAL_PROFILE = "default_therm_profile";
    public static final String THERMAL_SYSTEM_PROPERTY = "persist.therm.default";    
    private Context mContext;
    private Preference mThermPref;
    private Preference mPerfPref;
    private SecureSettingListPreference mDefaultPerfProfile;
    private SecureSettingListPreference mDefaultThermProfile;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.poco_settings, rootKey);	
        mContext = this.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
             mThermPref = findPreference("therm_display");
                mThermPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getContext(), ThermalActivity.class);
                         startActivity(intent);
                         return true;
                     }
                });
             mPerfPref = findPreference("perf_display");
                mPerfPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getContext(), PerformanceActivity.class);
                         startActivity(intent);
                         return true;
                     }
                });

            mDefaultPerfProfile = (SecureSettingListPreference) findPreference(DEFAULT_PERF_PROFILE);
            mDefaultPerfProfile.setValue(FileUtils.getStringProp(PERFORMANCE_SYSTEM_PROPERTY, "0"));
            mDefaultPerfProfile.setOnPreferenceChangeListener(this);            

            mDefaultThermProfile = (SecureSettingListPreference) findPreference(DEFAULT_THERMAL_PROFILE);
            mDefaultThermProfile.setValue(FileUtils.getStringProp(THERMAL_SYSTEM_PROPERTY, "0"));
            mDefaultThermProfile.setOnPreferenceChangeListener(this);


}


    private void setSystemPropertyBoolean(String key, boolean value) {
        String text = value?"1":"0";
        SystemProperties.set(key, text);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();      
        switch (key) {
            case DEFAULT_PERF_PROFILE:
                mDefaultPerfProfile.setValue((String) value);
                FileUtils.setStringProp(PERFORMANCE_SYSTEM_PROPERTY, (String) value);
                break;
                
            case DEFAULT_THERMAL_PROFILE:
                mDefaultThermProfile.setValue((String) value);
                FileUtils.setStringProp(THERMAL_SYSTEM_PROPERTY, (String) value);
                break;               
                                
            default:				
                break;
        }
        return true;				
    }  
}
