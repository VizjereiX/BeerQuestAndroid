package pl.programa.beerquest;

import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.app.App;
import pl.programa.beerquest.gcm.EventNotificationActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class GCMIntentService extends
		com.google.android.gcm.GCMBaseIntentService {

	public static final String SENDER_ID = "188491213763";

	private static final int HELLO_ID = 1;
	private String[] mSenderIds;

	public GCMIntentService() {
	}

	public GCMIntentService(String... senderIds) {
		super(senderIds);
		this.mSenderIds = senderIds;
	}

	@Override
	protected void onError(Context ctx, String text) {
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onMessage(Context ctx, Intent intent) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.beer_icon;
		int type = intent.getExtras().getInt("type");

		CharSequence tickerText = "";
		CharSequence contentTitle = "";
		CharSequence contentText = "";
		long when = System.currentTimeMillis();
		Context context = getApplicationContext();

		if (type == 1) {
			tickerText = intent.getExtras().get("title").toString(); // ticker-text

			contentTitle = "Zostałeś zaproszony na wydarzenie";
			contentText = "";
		}
		Intent notificationIntent = new Intent(this,
				EventNotificationActivity.class);
		notificationIntent.putExtra("text", contentTitle);
		notificationIntent.putExtra("title", contentTitle);
		notificationIntent.putExtra("body", intent.getExtras()
				.getString("body"));
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		notificationIntent.addCategory(Intent.CATEGORY_HOME);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL
				| Notification.FLAG_SHOW_LIGHTS;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.number = 1;
		// and this
		mNotificationManager.notify(HELLO_ID, notification);
	}

	@Override
	protected void onRegistered(Context ctx, String regId) {
		Api.register(regId, App.getContext());
	}

	@Override
	protected void onUnregistered(Context ctx, String text) {

	}

	@Override
	protected String[] getSenderIds(Context context) {
		if (mSenderIds != null)
			return super.getSenderIds(context);
		String senderIds[] = { SENDER_ID };
		return senderIds;
	}
}
