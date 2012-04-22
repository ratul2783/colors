package com.magicbox.sample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DummyDatabaseActivity extends Activity {
    /**GPS**/
	private LocationManager mMyLocationManager;
	private MyLocationListener mGPSLocationListener;
	private MyLocationListener mNetworkLocationListener;
	private MyLocationListener mpassiveLocationListener;
	private static final long MINTIME = 600000;
	private static final long MINDISTANCE = 5;
	public static Double mMyLatitude;
	public static Double mMyLongitude;
	/**GPS**/
	
	
	private DatabaseHelper mDBHelper;
	private String mQrValue;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mDBHelper = new DatabaseHelper(this);
        mQrValue = "SGX4044M\nMB C Class 180";
        
        EditText txt = (EditText) findViewById(R.id.editText1);
        
        ArrayList<String> valSet = mDBHelper.getOptionSet();
        if(valSet.size() > 0)
        	txt.setText(valSet.get(0));
        
        
        /**GPS**/
        mMyLatitude = 0.0;
	    mMyLongitude = 0.0;
	    
	    // ---use the LocationManager class to obtain GPS locations---
	    mMyLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    mGPSLocationListener = new MyLocationListener();
	    mNetworkLocationListener = new MyLocationListener();
	    mpassiveLocationListener = new MyLocationListener();
	    
	    try
	    {
	    	mMyLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINTIME, MINDISTANCE, mGPSLocationListener);
		    mMyLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINTIME, MINDISTANCE, mNetworkLocationListener);
		    mMyLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINTIME, MINDISTANCE, mpassiveLocationListener);
	    }
	    catch(RuntimeException e)
	    {
	    	Log.e("Unable to get location manager", e.getMessage());
	    }
	    catch(Exception e)
	    {
	    	
	    }
	    
	    if(mMyLocationManager != null)
	    {
	    	Location myLoc = mMyLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    if(myLoc != null)
		    {
		    	mMyLatitude = myLoc.getLatitude();
			    mMyLongitude = myLoc.getLongitude();
		    }
		    else
		    {
		    	myLoc = mMyLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			    if(myLoc != null)
			    {
			    	mMyLatitude = myLoc.getLatitude();
				    mMyLongitude = myLoc.getLongitude();
			    }
			    else
			    {
			    	myLoc = mMyLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
				    if(myLoc != null)
				    {
				    	mMyLatitude = myLoc.getLatitude();
					    mMyLongitude = myLoc.getLongitude();
				    }
			    }
		    }
	    }
	    
	    Toast.makeText(getBaseContext(), "In the beginning: latitude" + mMyLatitude + " longitude: " + mMyLongitude, Toast.LENGTH_LONG).show();
	    
	    /*GPS*/
    }
    
    
    @Override
	protected void onDestroy()
	{
		mMyLocationManager.removeUpdates(mNetworkLocationListener);
		mMyLocationManager.removeUpdates(mGPSLocationListener);
		mMyLocationManager.removeUpdates(mpassiveLocationListener);
		super.onDestroy();
	}
    
    public void onClick_Add(View v)
    {
    	
    }
    
    public void onClick_Save(View v)
    {
    	EditText txt = (EditText) findViewById(R.id.editText1);
    	mDBHelper.setOption(txt.getText().toString());
    }
    
    
    private class MyLocationListener implements LocationListener
	{
	    @Override
	    public void onLocationChanged(Location loc)
	    {
	        if (loc != null)
	        {
	            Toast.makeText(getBaseContext(), "Location changed : Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
	            mMyLatitude = loc.getLatitude();
			    mMyLongitude = loc.getLongitude();
	        }
	    }
	
	    @Override
	    public void onProviderDisabled(String provider) {
	        // TODO Auto-generated method stub
	    	Toast.makeText(getBaseContext(), provider + " disabled", Toast.LENGTH_SHORT);
	    	
	    }
	
	    @Override
	    public void onProviderEnabled(String provider) {
	        // TODO Auto-generated method stub
	    	Toast.makeText(getBaseContext(), provider + " enabled", Toast.LENGTH_SHORT);
	    }
	
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	        // TODO Auto-generated method stub
	    	Toast.makeText(getBaseContext(), provider + " status changed:" + status, Toast.LENGTH_SHORT);
	    }
	}
}