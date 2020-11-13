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
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
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
import android.preference.ListPreference;

import com.lineageos.settings.pocopref.R;

public class PocoPrefSettings extends PreferenceActivity implements OnPreferenceChangeListener {
	private static final boolean DEBUG = false;
	private static final String TAG = "PocoPref";
    private Context mContext;
    private SharedPreferences mPreferences;
    private Preference mThermPref;
    private Preference mPerfPref;
    private ListPreference mDefaultPerfProfile;
    private ListPreference mDefaultThermProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.poco_settings);
             mThermPref = findPreference("therm_display");
                mThermPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getApplicationContext(), ThermalActivity.class);
                         startActivity(intent);
                         return true;
                     }
                });
             mPerfPref = findPreference("perf_display");
                mPerfPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getApplicationContext(), PerformanceActivity.class);
                         startActivity(intent);
                         return true;
                     }
                });

        mContext = getApplicationContext();

            mDefaultPerfProfile = (ListPreference) findPreference("default_perf_profile");
            if( mDefaultPerfProfile != null ) {
                    String profile = getSystemPropertyString("persist.perf.default","0");
                    Log.e(TAG, "mDefaultPerfProfile: getProfile=" + profile);
                    mDefaultPerfProfile.setValue(profile);
                    mDefaultPerfProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                      public boolean onPreferenceChange(Preference preference, Object newValue) {
                        try {
                            Log.e(TAG, "mDefaultPerfProfile: setProfile=" + newValue.toString());
			                setSystemPropertyString("persist.perf.default",newValue.toString());
                        } catch(Exception re) {
                            Log.e(TAG, "onCreate: mDefaultPerfProfile Fatal! exception", re );
                        }
                        return true;
                      }
                    });
                }

            mDefaultThermProfile = (ListPreference) findPreference("default_therm_profile");
            if( mDefaultThermProfile != null ) {
                    String profile = getSystemPropertyString("persist.therm.default","0");
                    Log.e(TAG, "mDefaultThermProfile: getProfile=" + profile);
                    mDefaultThermProfile.setValue(profile);
                    mDefaultThermProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                      public boolean onPreferenceChange(Preference preference, Object newValue) {
                        try {
                            Log.e(TAG, "mDefaultThermProfile: setProfile=" + newValue.toString());
			                setSystemPropertyString("persist.therm.default",newValue.toString());
                        } catch(Exception re) {
                            Log.e(TAG, "onCreate: mDefaultPerfProfile Fatal! exception", re );
                        }
                        return true;
                      }
                    });
                }


}

    private void setEnable(String key, boolean value) {
	if(value) {
 	      SystemProperties.set(key, "1");
    	} else {
    		SystemProperties.set(key, "0");
    	}
    	if (DEBUG) Log.d(TAG, key + " setting changed");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        boolean value;
        String strvalue;
      	value = (Boolean) newValue;
    	((SwitchPreference)preference).setChecked(value);
    	setEnable(key,value);       
	     return true;
    }
    
     private void setSystemPropertyString(String key, String value) {
        Log.e(TAG, "setSystemPropertyBoolean: key=" + key + ", value=" + value);
        SystemProperties.set(key, value);
    }

    private String getSystemPropertyString(String key, String def) {
        return SystemProperties.get(key,def);
    }

    private boolean getSystemPropertyBoolean(String key) {
        if( SystemProperties.get(key,"0").equals("1") || SystemProperties.get(key,"0").equals("true") ) return true;
	    return false;
    }

    private void setSystemPropertyBoolean(String key, boolean value) {
        String text = value?"1":"0";
        Log.e(TAG, "setSystemPropertyBoolean: key=" + key + ", value=" + value);
        SystemProperties.set(key, text);
    }      
}
