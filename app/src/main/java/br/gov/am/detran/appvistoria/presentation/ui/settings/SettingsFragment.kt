package br.gov.am.detran.appvistoria.presentation.ui.settings

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.FragmentSettingsBinding
import br.gov.am.detran.appvistoria.until.ThemeHelper


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding= FragmentSettingsBinding.bind(view)
        //Get the Theme specific color
        val typedValue = TypedValue()
        val theme = requireContext().theme

        /*R.attr.colorBackgroundScreenBody is my own attr from attrs.xml file,
        you can directly use R.color.red - Or any color from your colors.xml
        file if you do not have multi-theme app.*/
        theme.resolveAttribute(R.attr.colorControlNormal, typedValue, true)
        val color = typedValue.data

        view.setBackgroundColor(color)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val themePreference = findPreference<ListPreference>("themePref")
        if (themePreference != null) {
            themePreference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference, newValue ->
                    val themeOption = newValue as String
                    ThemeHelper.applyTheme(themeOption)
                    true
                }
        }
    }
}