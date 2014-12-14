/*
    Copyright (C) 2013-2014 Christian Schneider
    christian.d.schneider@googlemail.com
    
    This file is part of Androsens classic.

    Androsens classic is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Androsens classic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Androsens classic.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.tritop.androsensclassic;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.tritop.androsensclassic.helper.SensorInfo;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {
	final static String SENSOR_KEY_NAME = "SENSOR_NAME";
	final static String SENSOR_KEY_VENDOR = "SENSOR_VENDOR";
	final static String SENSOR_KEY_VERSION = "SENSOR_VERSION";
	final static String SENSOR_KEY_POWER = "SENSOR_POWER";
	final static String SENSOR_KEY_MAXRANGE = "SENSOR_MAXRANGE";
	final static String SENSOR_KEY_RESOLUTION = "SENSOR_RESOLUTION";
	
	
	List<Map<String,String>> itemList = new ArrayList<Map<String,String>>();
	SimpleAdapter mListAdapter;
	
	SensorManager mSensorManager;
	
	int[] boundViews = new int[]{R.id.textView_SensorName,R.id.textView_SensorVendor,R.id.textView_SensorVersion,R.id.textView_SensorMaxRange,R.id.textView_SensorResolution,R.id.textView_SensorPower};
	String[] boundRows = new String[]{SENSOR_KEY_NAME,SENSOR_KEY_VENDOR,SENSOR_KEY_VERSION,SENSOR_KEY_MAXRANGE,SENSOR_KEY_RESOLUTION,SENSOR_KEY_POWER};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		
		((AndrosensApp) this.getApplication()).setSensorManager(mSensorManager);
		((AndrosensApp) getApplication()).setSensorList(((AndrosensApp) getApplication()).getSensorManager().getSensorList(Sensor.TYPE_ALL));
		getAvailableSensors();
		mListAdapter = new SimpleAdapter(this, itemList, R.layout.list_item_layout, boundRows, boundViews);
		setListAdapter(mListAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_settings:	Intent startIntent = new Intent(this, Settings.class);
										startActivity(startIntent);return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
	
	
	private void getAvailableSensors(){
		itemList.clear();
		itemList.addAll(parseAvailableSensors(((AndrosensApp) getApplication()).getSensorList()));
		
	}
	
	private List<Map<String,String>> parseAvailableSensors(List<Sensor> sList){
		List<Map<String,String>> mList = new Vector<Map<String,String>>();
		HashMap<String,String> mSensorInfo;
		for(Sensor sensor: sList){
			mSensorInfo = new HashMap<String,String>();
			mSensorInfo.put(SENSOR_KEY_NAME, sensor.getName());
			mSensorInfo.put(SENSOR_KEY_VENDOR, sensor.getVendor());
			mSensorInfo.put(SENSOR_KEY_VERSION, SensorInfo.getStringType(this, sensor.getType())+" v"+String.valueOf(sensor.getVersion()));
			mSensorInfo.put(SENSOR_KEY_POWER, SensorInfo.formatSensorfloat(sensor.getPower(),4)+" mA");
			mSensorInfo.put(SENSOR_KEY_MAXRANGE, SensorInfo.formatSensorfloat(sensor.getMaximumRange(),3)+" max");
			mSensorInfo.put(SENSOR_KEY_RESOLUTION,SensorInfo.formatSensorfloat(sensor.getResolution(),4)+" res"); 
			mList.add(mSensorInfo);
		}
		return mList;
		
		
	}
	
	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent startIntent = new Intent(this, SensorActivity.class);
		startIntent.putExtra("SENSORINDEX",position);
		startActivity(startIntent);
	}
	
}
