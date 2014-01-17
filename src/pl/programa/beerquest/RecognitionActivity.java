package pl.programa.beerquest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import pl.itraff.TestApi.ItraffApi.ItraffApi;
import pl.programa.beerquest.app.App;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

public class RecognitionActivity extends Activity {

	public static final int ID = 42564;
	public static final String KEY = "a40efa0f3b";
	private File outputFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		outputFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/scan.jpg");
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_FULL_SCREEN, true);
		takePictureIntent.putExtra(MediaStore.EXTRA_SHOW_ACTION_ICONS, false);
		startActivityForResult(takePictureIntent, 1234);
	}

	// handler that receives response from api
	@SuppressLint("HandlerLeak")
	private Handler itraffApiHandler = new Handler() {
		// callback from api
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			if (data != null) {
				Integer status = data.getInt(ItraffApi.STATUS, -1);
				String response = data.getString(ItraffApi.RESPONSE);
				// status ok
				if (status == 0) {
					App.logd(response);
					// application error (for example timeout)
				} else if (status == -1) {

					// error from api
				} else {
				}
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		App.logv("RES");
		if (resultCode == Activity.RESULT_OK) {
			Bitmap image = null; // (Bitmap) bundle.get("data");
			App.logv("RES_OK");
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(outputFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			image = BitmapFactory.decodeStream(fis);
			if (image != null) {
				ItraffApi api = new ItraffApi(ID, KEY, App.TAG, true);
				api.setMode(ItraffApi.MODE_SINGLE);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] pictureData = stream.toByteArray();
				api.sendPhoto(pictureData, itraffApiHandler, false);
			}
		}
		App.logv("RES_OTHER");
	}
}
