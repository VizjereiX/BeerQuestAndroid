package pl.programa.beerquest.utils;

import pl.programa.beerquest.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * dialogs helper
 * @author Programa.pl
 */
public class DialogsHelper {


	public static void showYesNoDialog(final Context activity, String title, String message,
			DialogInterface.OnClickListener onPositiveClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setCancelable(true).setTitle(title);
		if (message != null && message.length() > 0) {
			builder.setMessage(message);
		}
		if (title != null && title.length() > 0) {
			builder.setTitle(title);
		}
		builder.setPositiveButton(R.string.ok, onPositiveClick);
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showOkDialog(final Context activity, String title, String message) {
		QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(activity).
			    setTitle(title).
			    setTitleColor("#09bafe").
			    setMessage(message);
			

			qustomDialogBuilder.setNeutralButton("Ok", new android.content.DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        }).show();
	}
}
