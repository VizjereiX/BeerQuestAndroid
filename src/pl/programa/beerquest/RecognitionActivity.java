package pl.programa.beerquest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import pl.itraff.TestApi.ItraffApi.ItraffApi;
import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.api.ApiCallback;
import pl.programa.beerquest.app.App;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

public class RecognitionActivity extends Activity {

	public static final int ID = 42564;
	public static final String KEY = "a40efa0f3b";
	private File outputFile;
	private AnimationDrawable anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recognize);
		 ImageView bgImage= (ImageView) findViewById(R.id.recognize_bg);
		  bgImage.setBackgroundResource(R.drawable.spinner);
		  anim = (AnimationDrawable) bgImage.getBackground();
		  anim.start();

		outputFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/scan.jpg");
		getImage();
	}

	protected void getImage() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(outputFile));
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
				if (status == 0) {
					String beerJson = response.replace("\"status\":0,", "");
					final String beerName = beerJson.replace("{\"id\":", "").replace("}", "").replace("\"", "");
					ApiCallback callback = new ApiCallback() {
						
						@Override
						public void onResponse(Object response, Integer status, String message,
								Integer httpStatus) {
							if (status == 0  || status == 200) {
								Toast.makeText(App.getContext(), beerName + " padł pod Twymi ciosami!", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
							}
							anim.stop();
						}
					};
					Api.recognize(beerJson, App.getContext(), callback);
					// application error (for example timeout)
				} else if (status == -1) {
					Toast.makeText(App.getContext(), "Zlamał Ci sie miecz, spróbuj jeszcze raz!", Toast.LENGTH_SHORT).show();
					getImage();
				} else {
					Toast.makeText(App.getContext(), "Nie znam tej bestii!", Toast.LENGTH_SHORT).show();
					getImage();
				}
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Bitmap image = null;
			App.logv("RES_OK");
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(outputFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 8;
			image = BitmapFactory.decodeStream(fis, null, options);
			if (image != null) {
				ItraffApi api = new ItraffApi(ID, KEY, App.TAG, true);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] pictureData = stream.toByteArray();
				api.sendPhoto(pictureData, itraffApiHandler, false);
				pictureData = null;
				System.gc();
			}
		}
	}
}
