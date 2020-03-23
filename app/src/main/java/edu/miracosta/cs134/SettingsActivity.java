/**
 * SettingsActivity.java: Activity which houses a Fragment for the user to change the quiz type setting in
 *
 * @author Cameron Colleran
 * @version 1.0
 */
package edu.miracosta.cs134;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // We can comment setContentView, since the content will be provided
        // by the preferences.xml file.
        //setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsActivityFragment())
                .commit();
    }

    /**
     * Inner Fragment class of SettingsActivity where user is prompted with which quiz type
     * they would like to take
     */
    public static class SettingsActivityFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
