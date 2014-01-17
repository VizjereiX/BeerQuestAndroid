package pl.programa.beerquest.utils;

/**
 * synchgronization helper class
 * @author Programa.pl
 */
public class SyncHelper {
	public static final Integer RECEIVE = 0;
	public static final Integer OK = 200;

	private SyncHelper() {
	}

	public static Boolean isDataToSend() {
//		if (DM.getInstance().getPostsCount() > 0) {
//			return true;
//		}
		return false;
	}



}
