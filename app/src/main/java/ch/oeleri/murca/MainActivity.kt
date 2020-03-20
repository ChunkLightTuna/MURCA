package ch.oeleri.murca

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    companion object {
        private const val isFree = "free"
        private const val checked = "checked"
    }

    private var last: SharedPreferences? = null
    private var hasChecked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) hasChecked = savedInstanceState.getBoolean(checked)
        setContentView(R.layout.activity_main)
        val background = findViewById<ImageView>(R.id.background)
        val rotation = windowManager.defaultDisplay.rotation

        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            background.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            background.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            background.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            background.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        last = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        if (last!!.contains(isFree)) {
            val free = last!!.getBoolean(isFree, false)

            if (!hasChecked) {
                val freedomStatus = findViewById<TextView>(R.id.freedom_status)

                if (free) {
                    freedomStatus.setText(R.string.previously_free)
                } else {
                    background.setImageResource(R.color.red)
                    background.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    background.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    freedomStatus.setText(R.string.previously_not_free)
                }
            } else {
                setFreedom(free)
            }
        }
    }

    /**
     * Save launched app name before quitting.
     *
     * @param outState Bundle
     */
    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(checked, hasChecked)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        finish()
    }

    fun checkForFreedom(view: View) {
        getLocation(this@MainActivity).execute()
    }

    fun setFreedom(free: Boolean) {
        findViewById<Button>(R.id.check_button).visibility = View.GONE

        val editor = last!!.edit()
        editor.putBoolean(isFree, free)
        editor.apply()
        val freedomStatus = findViewById<TextView>(R.id.freedom_status)
        val background = findViewById<ImageView>(R.id.background)
        if (free) {
            background.setImageResource(R.drawable.american_flag)
            freedomStatus.setText(R.string.free)
        } else {
            background.setImageResource(R.color.red)
            background.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            background.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            freedomStatus.setText(R.string.not_free)
        }
        hasChecked = true
    }
}