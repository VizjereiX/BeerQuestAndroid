package pl.programa.beerquest;

import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.api.ApiCallback;
import pl.programa.beerquest.app.App;
import pl.programa.beerquest.model.Quest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NewQuestActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_quest);
		Button newQuestButton = (Button) findViewById(R.id.buttonNewQuest);
		newQuestButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ApiCallback callback = new ApiCallback() {
					
					@Override
					public void onResponse(Object response, Integer status, String message,
							Integer httpStatus) {
						if (status == 0  || status == 200) {
							Toast.makeText(App.getContext(), "Towarzysze powiadomieni!", Toast.LENGTH_SHORT).show();
							App.logv(response+"");
						} else {
							Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
						}
					}
				};
				Api.questNew(new Quest(), App.getContext(), callback);
			}
		});
	}
}
