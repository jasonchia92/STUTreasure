package com.projectstu.connect;

import java.util.HashMap;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionTask {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "TreasurePref";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	
	// User name (make variable public to access from outside)
	public final static String KEY_COUNT = "0";
	
	public SessionTask(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(int count){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing name in pref
		editor.putInt(KEY_COUNT, count);
		
		editor.commit();
	}	
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, Integer> getUserDetails(){
		HashMap<String, Integer> user = new HashMap<String, Integer>();
		// user name
		user.put(KEY_COUNT, pref.getInt(KEY_COUNT, 0));
		// return user
		return user;
	}
	
	public void checkLogin(){
		// Check login status
		if(!this.isLoggedIn()){
//			// user is not logged in redirect him to Login Activity
//			Intent i = new Intent();
//			// Closing all the Activities
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			
//			// Add new Flag to start new Activity
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			
//			// Staring Login Activity
//			_context.startActivity(i);
			editor.putInt(KEY_COUNT, 0);

		}
	}
	
	/**
	 * Clear session details
	 * */
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
