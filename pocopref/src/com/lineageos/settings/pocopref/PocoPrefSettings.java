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
import com.lineageos.settings.pocopref.kcal.KCalSettingsActivity;
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
    public static final String PREF_DEVICE_KCAL = "device_kcal";
    public static final String CATEGORY_DISPLAY = "display";
    public static final String PREF_BFRAMEWORK = "device_bframework";	    
    public static final String DEFAULT_PERF_PROFILE = "default_perf_profile";
    public static final String PERFORMANCE_SYSTEM_PROPERTY = "persist.perf.default";
    public static final String DEFAULT_THERMAL_PROFILE = "default_therm_profile";
    public static final String THERMAL_SYSTEM_PROPERTY = "persist.therm.default";
    private static final String SYSTEM_PROPERTY_NVT_FW = "persist.nvt_fw";
    private static final String SYSTEM_PROPERTY_NVT_ESD = "persist.nvt_esd";
    private static final String SYSTEM_PROPERTY_DOLBY = "persist.dolby.enable";

    private Context mContext;
    private Preference mThermPref;
    private Preference mPerfPref;
    private Preference mKcal;
    private SwitchPreference mDolby;
    private SecureSettingListPreference mDefaultPerfProfile;
    private SecureSettingListPreference mDefaultThermProfile;
    private Preference mBframework;	
    private SwitchPreference mNvtFw;
    private SwitchPreference mNvtESD;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.poco_settings, rootKey);	
        mContext = this.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        PreferenceCategory displayCategory = (PreferenceCategory) findPreference(CATEGORY_DISPLAY);

        mKcal = findPreference(PREF_DEVICE_KCAL);

        mKcal.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), KCalSettingsActivity.class);
            startActivity(intent);
            return true;
        });		
		
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

             mBframework = findPreference(PREF_BFRAMEWORK);

             mBframework.setOnPreferenceClickListener(preference -> {
             Intent intent = new Intent(getActivity().getApplicationContext(), BframeworkActivity.class);
             startActivity(intent);
             return true;
             });

            mDefaultPerfProfile = (SecureSettingListPreference) findPreference(DEFAULT_PERF_PROFILE);
            mDefaultPerfProfile.setValue(FileUtils.getStringProp(PERFORMANCE_SYSTEM_PROPERTY, "0"));
            mDefaultPerfProfile.setOnPreferenceChangeListener(this);            

            mDefaultThermProfile = (SecureSettingListPreference) findPreference(DEFAULT_THERMAL_PROFILE);
            mDefaultThermProfile.setValue(FileUtils.getStringProp(THERMAL_SYSTEM_PROPERTY, "0"));
            mDefaultThermProfile.setOnPreferenceChangeListener(this);

            mNvtFw = (SwitchPreference) findPreference(SYSTEM_PROPERTY_NVT_FW);
            mNvtFw.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_NVT_FW, false));
            mNvtFw.setOnPreferenceChangeListener(this);

            mNvtESD = (SwitchPreference) findPreference(SYSTEM_PROPERTY_NVT_ESD);
            mNvtESD.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_NVT_ESD, false));
            mNvtESD.setOnPreferenceChangeListener(this);

            mDolby = (SwitchPreference) findPreference(SYSTEM_PROPERTY_DOLBY);
            mDolby.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_DOLBY, false));
            mDolby.setOnPreferenceChangeListener(this);
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

            case SYSTEM_PROPERTY_NVT_FW:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(SYSTEM_PROPERTY_NVT_FW, (Boolean) value);
                break;

            case SYSTEM_PROPERTY_NVT_ESD:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(SYSTEM_PROPERTY_NVT_ESD, (Boolean) value);
                break;

            case SYSTEM_PROPERTY_DOLBY:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(SYSTEM_PROPERTY_DOLBY, (Boolean) value);
                break;
                                
            default:				
                break;
        }
        return true;				
    }  
}
