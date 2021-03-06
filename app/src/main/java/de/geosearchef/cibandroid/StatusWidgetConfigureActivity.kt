package de.geosearchef.cibandroid

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import de.geosearchef.cibandroid.StatusWidget.Companion.updateAppWidget
import de.geosearchef.cibandroid.StatusWidgetConfigureActivity

/**
 * The configuration screen for the [StatusWidget] AppWidget.
 */
class StatusWidgetConfigureActivity : Activity() {
    var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    lateinit var cityTextField: EditText


    var mOnClickListener = View.OnClickListener {
        val context: Context = this@StatusWidgetConfigureActivity
        val city = cityTextField.text.toString()
        saveCityPref(context, mAppWidgetId, city)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, mAppWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)
        setContentView(R.layout.status_widget_configure)
        cityTextField = findViewById(R.id.cityTextField)
        findViewById<View>(R.id.add_button).setOnClickListener(mOnClickListener)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        cityTextField.setText(getConfiguredCity(this@StatusWidgetConfigureActivity, mAppWidgetId))
    }

    companion object {
        private const val PREFS_NAME = "de.geosearchef.cibandroid.StatusWidget"
        private const val PREF_PREFIX_KEY = "appwidget_"
        fun saveCityPref(context: Context, appWidgetId: Int, text: String?) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
            prefs.apply()
        }

        fun getConfiguredCity(context: Context, appWidgetId: Int): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val city = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
            return city ?: context.getString(R.string.appwidget_text)
        }

        fun deleteCityPref(context: Context, appWidgetId: Int) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.remove(PREF_PREFIX_KEY + appWidgetId)
            prefs.apply()
        }
    }
}