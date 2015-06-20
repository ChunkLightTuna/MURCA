package ch.oeleri.murca;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences last;
    private final static String isFree = "free";

    private boolean hasChecked = false;
    private final static String checked = "checked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            hasChecked = savedInstanceState.getBoolean(checked);

        setContentView(R.layout.activity_main);

        ImageView background = (ImageView) findViewById(R.id.background);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();


        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            background.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            background.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            background.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            background.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }


        last = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (last.contains(isFree)) {
            boolean free = last.getBoolean(isFree, false);
            if (!hasChecked) {
                TextView freedomStatus = (TextView) findViewById(R.id.freedom_status);
                if (free) {
                    freedomStatus.setText(R.string.previously_free);
                } else {
                    background.setImageResource(R.color.red);
                    background.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    background.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    freedomStatus.setText(R.string.previously_not_free);
                }
            } else {
                setFreedom(free);
            }
        }
    }


    /**
     * Save launched app name before quitting.
     *
     * @param outState Bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(checked, hasChecked);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void checkForFreedom(View view) {
        new getLocation(this).execute();

    }

    public void setFreedom(boolean free) {

        SharedPreferences.Editor editor = last.edit();
        editor.putBoolean(isFree, free);
        editor.apply();

        TextView freedomStatus = (TextView) findViewById(R.id.freedom_status);
        ImageView background = (ImageView) findViewById(R.id.background);
        if (free) {
            background.setImageResource(R.drawable.american_flag);
            freedomStatus.setText(R.string.free);
        } else {
            background.setImageResource(R.color.red);
            background.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            background.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            freedomStatus.setText(R.string.not_free);
        }
        hasChecked = true;
    }


}
