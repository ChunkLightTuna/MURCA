package ch.oeleri.murca;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Chris Oelerich on 6/15/15.
 */

public class getLocation extends AsyncTask<Void, Void, Boolean> {

    private MainActivity mActivity;

    public getLocation(MainActivity activity) {
        mActivity = activity;
    }


    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            InputStream inputStream = downloadUrl("http://ip-api.com/json");
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                String value = reader.nextString();
                Log.wtf(getClass().getName(), name + " " + value);

                if (name.equals("countryCode") && value.equals("US")) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            Log.wtf(getClass().getName(), "fetch location data failed: " + e);
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean free) {
        mActivity.setFreedom(free);
    }
}
