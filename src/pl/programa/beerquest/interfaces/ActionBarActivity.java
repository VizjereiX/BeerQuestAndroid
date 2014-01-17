package pl.programa.beerquest.interfaces;

import pl.programa.beerquest.app.App;
import pl.programa.beerquest.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Custom Activity with custom action bar and useful methods
 * 
 * @author Programa.pl
 * 
 */
public abstract class ActionBarActivity extends Fragment{

	///parameters
	Button actionBarLogout;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	App.logv("inflating action bar");
    	View view = inflater.inflate(R.layout.action_bar, container, false);
    	    	
        return view;
    }	

}