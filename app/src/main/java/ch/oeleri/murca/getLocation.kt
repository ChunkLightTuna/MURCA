package ch.oeleri.murca

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.JsonReader
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Chris Oelerich on 6/15/15.
 */
class GetLocation internal constructor(@field:SuppressLint("StaticFieldLeak") private val mActivity: MainActivity) : AsyncTask<Void?, Void?, Boolean>() {

    /**
     * Given a string representation of a URL, sets up a connection and gets an input stream.
     * @param urlString String
     * @return InputSteam
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): InputStream {
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = 10000
        conn.connectTimeout = 15000
        conn.requestMethod = "GET"
        conn.doInput = true
        // Starts the query
        conn.connect()
        return conn.inputStream
    }

    override fun doInBackground(vararg params: Void?): Boolean {
        return try {
            val inputStream = downloadUrl("http://ip-api.com/json")
            val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
            reader.beginObject()
            while (reader.hasNext()) {
                val name = reader.nextName()
                val value = reader.nextString()
                Log.wtf(javaClass.name, "$name $value")
                if (name == "countryCode" && value == "US") {
                    return true
                }
            }
            false
        } catch (e: Exception) {
            Log.wtf(javaClass.name, "fetch location data failed: $e")
            true
        }
    }

    override fun onPostExecute(free: Boolean) {
        mActivity.setFreedom(free)
    }
}