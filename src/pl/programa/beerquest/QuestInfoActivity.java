package pl.programa.beerquest;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.api.ApiCallback;
import pl.programa.beerquest.app.App;
import pl.programa.beerquest.model.Quest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestInfoActivity extends Activity {
	private AnimationDrawable anim;
	private TextView title;
	private TextView startDate;
	private TextView confirmDate;
	private TextView participants;
	private Button confirmButton;
	private View layout;
	private ImageView bgImage;
	private Button recognizeBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quest_info);
		bgImage = (ImageView) findViewById(R.id.bg);
		bgImage.setBackgroundResource(R.drawable.spinner);
		anim = (AnimationDrawable) bgImage.getBackground();
		anim.start();
		layout = findViewById(R.id.quest_info_layout);
		title = (TextView) findViewById(R.id.quest_info_title);
		startDate = (TextView) findViewById(R.id.quest_info_startDate);
		confirmDate = (TextView) findViewById(R.id.quest_info_acceptTime);
		participants = (TextView) findViewById(R.id.quest_info_participants);
		confirmButton = (Button) findViewById(R.id.quest_info_confirm_btn);
		recognizeBtn = (Button) findViewById(R.id.quest_info_recognize_btn);

		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QuestInfoActivity.this.confirmParticipation();
			}
		});

		recognizeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(App.getContext(),
						RecognitionActivity.class);
				QuestInfoActivity.this.startActivity(intent);
			}
		});

		ApiCallback callback = new ApiCallback() {

			@Override
			public void onResponse(Object response, Integer status,
					String message, Integer httpStatus) {
				Toast.makeText(App.getContext(), "Wysyłam posłańców!",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		};
		Api.questInfo(App.getQ().getId(), App.getContext(), callback);
	}

	protected void confirmParticipation() {
		ApiCallback callback = new ApiCallback() {

			@Override
			public void onResponse(Object response, Integer status,
					String message, Integer httpStatus) {
				Intent intent = new Intent(App.getContext(),
						QuestInfoActivity.class);
				QuestInfoActivity.this.startActivity(intent);
				QuestInfoActivity.this.finish();
			}
		};
		Api.questParticipate(App.getQ().getId(), App.getContext(), callback);
	}

	protected void showQuest(Quest quest) {
		layout.setVisibility(View.VISIBLE);
		anim.stop();
		bgImage.setVisibility(View.GONE);
		bgImage.setBackgroundDrawable(null);
		title.setText(quest.getName());
		if (quest.getStatus().equalsIgnoreCase(Quest.STATUS_ACTIVE)) {
			startDate.setText("Przygoda trwa!");
			recognizeBtn.setVisibility(View.VISIBLE);
		} else if (quest.getStatus().equalsIgnoreCase(Quest.STATUS_DONE)) {
			startDate.setText("Przygoda zakończona!");
		} else {
			startDate.setText("Drużyna wyruszy " + quest.getStartTs());
			if (quest.getStatus().equalsIgnoreCase(Quest.STATUS_NEW)) {
				confirmDate.setText("jeśli do " + quest.getConfirmTs()
						+ " zbierze się " + quest.getMinGuests() + " osób");
				confirmDate.setVisibility(View.VISIBLE);
				participants.setText("Aktualny stan potwierdzeń: "
						+ quest.getMembers().length + "/"
						+ quest.getMinGuests());
				participants.setVisibility(View.VISIBLE);
			}
			if (!quest.getParticipate()) {
				confirmButton.setVisibility(View.VISIBLE);
			} else {
				confirmButton.setVisibility(View.GONE);
			}
		}
	}

}
