package pl.programa.beerquest;

import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.api.ApiCallback;
import pl.programa.beerquest.app.App;
import pl.programa.beerquest.model.Quest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewQuestActivity extends Activity {

	private TextView questNameView;
	private TextView questDateView;
	private TextView questTimeView;
	private TextView questConfirmView;
	private TextView questTeamSizeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_quest);
		questNameView = (TextView) findViewById(R.id.newQuest_name);
		questDateView = (TextView) findViewById(R.id.newQuest_startDate);
		questTimeView = (TextView) findViewById(R.id.newQuest_startTime);
		questConfirmView = (TextView) findViewById(R.id.newQuest_confirmTime);
		questTeamSizeView = (TextView) findViewById(R.id.newQuest_minTeamSize);

		Button newQuestButton = (Button) findViewById(R.id.buttonNewQuest);
		newQuestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ApiCallback callback = new ApiCallback() {

					@Override
					public void onResponse(Object response, Integer status,
							String message, Integer httpStatus) {
						if (status == 0 || status == 200) {
							App.setQ(Quest.fromJson(message));
							Toast.makeText(App.getContext(),
									"Towarzysze powiadomieni!",
									Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(NewQuestActivity.this, QuestInfoActivity.class);
							NewQuestActivity.this.startActivity(intent);
							NewQuestActivity.this.finish();
						} else {
							Toast.makeText(App.getContext(), message,
									Toast.LENGTH_SHORT).show();
						}
					}
				};
				Quest quest = new Quest();
				quest.setName(NewQuestActivity.this.questNameView.getText()
						.toString());

				String str_date = NewQuestActivity.this.questDateView.getText()
						.toString();
				str_date += " "
						+ NewQuestActivity.this.questTimeView.getText()
								.toString();
				quest.setStartTs(str_date);

				str_date = NewQuestActivity.this.questDateView.getText()
						.toString();
				str_date += " "
						+ NewQuestActivity.this.questConfirmView.getText()
								.toString();

				quest.setConfirmTs(str_date);

				quest.setMinGuests(Integer
						.parseInt(NewQuestActivity.this.questTeamSizeView
								.getText().toString()));

				Api.questNew(quest, App.getContext(), callback);
			}
		});
	}
}
