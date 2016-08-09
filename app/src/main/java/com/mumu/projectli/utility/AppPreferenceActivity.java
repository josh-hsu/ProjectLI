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

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mumu.projectli.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class AppPreferenceActivity extends PreferenceActivity {
    public static final String TAG = "ProjectLI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add a button to the header list.
        if (hasHeaders()) {
            Log.d(TAG, "Launched App preference header");
            Button button = new Button(this);
            button.setText(getString(R.string.settings_restore_data_from_sdcard));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    restoreDataFromSdcard();
                }
            });
            setListFooter(button);
        }
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

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_header, target);
    }

    /*
     * Since API 19, PreferenceActivity must implement isValidFragment if it is initiated from
     * PACKAGE_NAME.PREF_ACTIVITY$FRAG_SUBCLASS
     */
    @Override
    protected boolean isValidFragment (String fragmentName) {
        return true;
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            PreferenceManager.setDefaultValues(getActivity(),
                    R.xml.preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    void restoreDataFromSdcard() {
        MaterialDialog builder = new MaterialDialog.Builder(this)
                .title(getString(R.string.settings_restore_title))
                .content(getString(R.string.settings_restore_subtitle))
                .positiveText(getString(R.string.action_confirm))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        doRestoreDataFromSdcard();
                    }
                })
                .negativeText(getString(R.string.action_cancel)).show();
    }

    void doRestoreDataFromSdcard() {
        try {
            saveSdcardFileToData(getString(R.string.electric_data_file_name));
        } catch (FileNotFoundException e) {
            Log.w(TAG, "Try to restore data from sdcard but no backup file found");
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.settings_restore_failed))
                    .content(getString(R.string.settings_restore_not_found))
                    .negativeText(getString(R.string.action_cancel)).show();
            return;
        }

        new MaterialDialog.Builder(this)
                .title(getString(R.string.settings_restore_title))
                .content(getString(R.string.settings_restore_finished))
                .negativeText(getString(R.string.action_confirm)).show();
    }

    public void saveSdcardFileToData(String filename) throws FileNotFoundException {
        String userSdcardPath = Environment.getExternalStorageDirectory() + "/" + filename;
        File srcFile = new File(userSdcardPath);
        String destFilePath = getFilesDir().getAbsolutePath() + "/" + filename;

        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(new File(destFilePath));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            Log.e(TAG, "Save " + filename + " to data failed: " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

