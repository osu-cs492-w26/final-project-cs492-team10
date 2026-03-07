package com.example.final_project_team10.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.final_project_team10.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}