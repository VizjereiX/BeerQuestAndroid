package pl.programa.beerquest.api;
/**
 * Api callback
 * @author Programa.pl
 */
public interface ApiCallback {
	public void onResponse(Object response, Integer status, String message, Integer httpStatus);
}
