package pl.programa.beerquest;

import pl.programa.beerquest.model.Quest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class EventNotificationActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    int questId = extras.getInt("questId");
		    Quest q = new Quest();
		    q.setId(questId);
		    Intent intent = new Intent(this, QuestInfoActivity.class);
		    startActivity(intent);
		    finish();
		}
	}
}
