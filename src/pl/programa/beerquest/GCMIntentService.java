package pl.programa.beerquest;

import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.app.App;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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

		int id = intent.getExtras().getInt("questid");
		String body = intent.getExtras().getString("body");

		Intent notificationIntent = new Intent(this,
				EventNotificationActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.beer_icon)
				.setContentTitle("BeerQuest powiadamia!")
				.setContentIntent(contentIntent).setContentText(body);
		Notification notification = mBuilder.build();

		notificationIntent.putExtra("questid", id);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		notificationIntent.addCategory(Intent.CATEGORY_HOME);

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
