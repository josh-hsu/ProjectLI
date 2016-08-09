/*
 * Copyright (C) 2016 The Josh Tool Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mumu.projectli.utility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mumu.projectli.R;

public class AppPreferenceActivity extends PreferenceActivity {
    public static String TAG = "ProjectLI";
    private static boolean mInitialized = false;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener spChanged;
    private Preference enableServicePreference;
    private Preference configSkillNormalPreference;
    private Preference configSkillGoldenPreference;
    private ListPreference battleStagePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML file
        addPreferencesFromResource(R.xml.preferences);

        // Prepare for button-like preference
        prepareButtonListener();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        /* This is for action bar */
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Add preference changed event listener, this must be called before XML loaded.
        prefs = this.getSharedPreferences("com.mumu.projectli_preferences", MODE_PRIVATE);
        spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                Log.d(TAG, key + " changed");
                handlePreferenceEvent(key);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(spChanged);
        prepareDefaultPropertySetup();
    }

    @Override
    public void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(spChanged);
    }

    /*
     * This must be called after preference activity is loaded
     */
    private void prepareButtonListener() {
        enableServicePreference = findPreference("enableServicePref");
        configSkillNormalPreference = findPreference("normal_skill");
        configSkillGoldenPreference = findPreference("golden_skill");
        battleStagePreference = (ListPreference) findPreference("battle_stage");

        configSkillNormalPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent=new Intent();
                //intent.setClass(AppPreferenceActivity.this, SkillSettingActivity.class);
                startActivity(intent);
                return true;
            }
        });

        configSkillGoldenPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent=new Intent();
                //intent.setClass(AppPreferenceActivity.this, SkillSettingActivity.class);
                startActivity(intent);
                return true;
            }
        });
        configSkillGoldenPreference.setEnabled(false);

        enableServicePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (Build.VERSION.SDK_INT >= 23) {
                    //Toast.makeText(AppPreferenceActivity.this, R.string.startup_permit_system_alarm, Toast.LENGTH_SHORT).show();
                    if (!Settings.canDrawOverlays(AppPreferenceActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 10);
                    } else {
                        //Toast.makeText(AppPreferenceActivity.this, R.string.headservice_how_to_stop, Toast.LENGTH_SHORT).show();
                        //startService(new Intent(AppPreferenceActivity.this, HeadService.class));
                    }
                } else {
                    //Toast.makeText(AppPreferenceActivity.this, R.string.headservice_how_to_stop, Toast.LENGTH_SHORT).show();
                    //startService(new Intent(AppPreferenceActivity.this, HeadService.class));
                }
                return true;
            }
        });

        mInitialized = true;
    }

    private boolean isSkillValid(String skill) {
        int number;
        try {
            number = Integer.parseInt(skill);
            Log.d(TAG, "Skill parse to " + number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     * Handle all default property setup
     */
    private void prepareDefaultPropertySetup() {
        // Set initialize state to false for safety
        mInitialized = false;

        // Set it to preference
        SharedPreferences appPrefs =
                getSharedPreferences("com.mumu.projectli_preferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appPrefs.edit();

        // Battle Skill Setting
        String normalSkill = "145";
        Log.d(TAG, "Normal Skill: " + normalSkill);
        if (normalSkill.length() >= 3)
            configSkillNormalPreference.setSummary(normalSkill.charAt(0) + " > " +
                normalSkill.charAt(1) + " > " + (normalSkill.charAt(2)));
        else
            configSkillNormalPreference.setSummary("技能尚未設定");
        //configSkillGoldenPreference.setSummary("技能尚未設定");

        // Service status
        enableServicePreference.setEnabled(true);

        prefsEditor.apply();

        mInitialized = true;
    }

    /*
     * Handle all the preference change events
     */
    private void handlePreferenceEvent(String key) {
        Log.d(TAG, "Changed: " + key);

        // If we are preparing the view by getting property for the fist time. Don't do anything here.
        if (!mInitialized) {
            Log.d(TAG, "System is in initializing phase, skip " + key + " changed.");
            return;
        }

        prepareDefaultPropertySetup();
    }
}

