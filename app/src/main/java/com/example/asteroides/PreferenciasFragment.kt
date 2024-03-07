package com.example.asteroides
import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
class PreferenciasFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val fragmentos = findPreference("numeroFragmentos") as EditTextPreference?
        fragmentos?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                val valor: Int
                valor = try {
                    (newValue as String).toInt()
                } catch (e: Exception) {
                    Toast.makeText(
                        activity, "Ha de ser un número",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnPreferenceChangeListener false
                }
                if (valor >= 0 && valor <= 9) {
                    fragmentos!!.summary = "En cuantos trozos se divide un asteroide ($valor)"
                    true
                } else {
                    Toast.makeText(
                        activity, "Máximo de fragmentos 9",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            }
    }
}