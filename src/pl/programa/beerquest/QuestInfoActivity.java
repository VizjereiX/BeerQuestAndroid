package pl.programa.beerquest;

import java.io.File;

import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.api.ApiCallback;
import pl.programa.beerquest.app.App;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestInfoActivity extends Activity{
	private AnimationDrawable anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quest_info);
		ImageView bgImage = (ImageView) findViewById(R.id.bg);
		bgImage.setBackgroundResource(R.drawable.spinner);
		anim = (AnimationDrawable) bgImage.getBackground();
		anim.start();
		View layout = findViewById(R.id.quest_info_layout);
		TextView title = (TextView) findViewById(R.id.quest_info_title);
		TextView startDate = (TextView) findViewById(R.id.quest_info_startDate);
		TextView confirmDate = (TextView) findViewById(R.id.quest_info_acceptTime);
		

		ApiCallback callback = new ApiCallback() {
			
			@Override
			public void onResponse(Object response, Integer status, String message,
					Integer httpStatus) {
				anim.stop();
				if (status == 0  || status == 200) {
					
				} else {
					Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		};
		Api.questInfo(App.getQ().getId(), App.getContext(), callback);
	}
}
