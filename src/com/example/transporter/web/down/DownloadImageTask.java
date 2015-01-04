package com.example.transporter.web.down;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImageTask extends AsyncTask<String, Void, Drawable>{

	 public DownloadImageTask() {
	}

	@Override
	 protected Drawable doInBackground(String... urls) {
		return loadImageFromWebOperations(urls[0]);
	 }
	 
	 /**
	 * <--- From StackOverFlow --->
	 * http://stackoverflow.com/questions/6407324/how-to-get-image-from-url-in-android
	 * 
	 * @param url
	 * @return
	 */
	public Drawable loadImageFromWebOperations(String url) {
		try {
			Log.i("ImageProcessor", url);
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "name");
			Log.i("ImageProcessor", d.getMinimumWidth() + " ");
		    return d;
		} catch (Exception e) {
			Log.e("ImageProcessor", e.toString());
		    return null;
		}
	}

}
