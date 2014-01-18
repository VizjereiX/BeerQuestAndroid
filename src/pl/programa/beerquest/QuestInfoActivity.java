package pl.programa.beerquest;

import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.api.ApiCallback;
import pl.programa.beerquest.app.App;
import pl.programa.beerquest.model.Quest;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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
		final TextView title = (TextView) findViewById(R.id.quest_info_title);
		final TextView startDate = (TextView) findViewById(R.id.quest_info_startDate);
		TextView confirmDate = (TextView) findViewById(R.id.quest_info_acceptTime);
		

		ApiCallback callback = new ApiCallback() {
			
			@Override
			public void onResponse(Object response, Integer status, String message,
					Integer httpStatus) {
				anim.stop();
				if (status == 0  || status == 200) {
					Quest quest = Quest.fromJson(response.toString());
					title.setText(quest.getName());
					if (quest.getStatus().equalsIgnoreCase(Quest.STATUS_ACTIVE)) {
						startDate.setText("Przygoda trwa!");
					} else {
						startDate.setText("Drużyna wyruszy " + "");
					}
				} else {
					Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		};
		Api.questInfo(App.getQ().getId(), App.getContext(), callback);
	}
	
	
}
