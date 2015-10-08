package co.uk.dmott.mymusicfinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import co.uk.dmott.mymusicfinder.EventListActivity;
import co.uk.dmott.mymusicfinder.MainActivity;
import co.uk.dmott.mymusicfinder.R;
import co.uk.dmott.mymusicfinder.ScheduleListActivity;

import android.app.Activity;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	public final static String MUSIC_SEARCHES_SYMBOL = "co.uk.dmott.myradiofinder1.SEARCHES";
	
	private SharedPreferences musicSearchesEntered; // cache of previous searches
	
	private TableLayout musicTableScrollView; //Scrollview for the matches returned from pullparser
	
	private EditText musicEditText; // optional text field for specifying string to search for in the synopsis
	private Spinner genreSpinner; // spinner for choosing the music genre to search for
	
	Button enterMusicSearchButton; //Button to add the search pattern to cache and search in xmlpullparser
	Button getSavedEventsButton; //Button to recover the stored calendar events
	
	Context mainContext;
	HashMap<String, String> genresMap = new HashMap<String, String>();
	
    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();
    public static ArrayList<String> channelpointer = new ArrayList<String>();	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		musicEditText = (EditText)findViewById(R.id.searchScheduleEditText);
		genreSpinner = (Spinner)findViewById(R.id.musicGenreSpinner);
		
		musicSearchesEntered = getSharedPreferences(MUSIC_SEARCHES_SYMBOL, MODE_PRIVATE);
		
		musicTableScrollView = (TableLayout)findViewById(R.id.musicSearchTableScrollView);
		
		mainContext = this;
		
		genresMap.clear(); // map the names put in the spinner to the string used in the search
		genresMap.put("classicpopandrock","Classic Pop & Rock");
		genresMap.put("Classic Pop & Rock","classicpopandrock");
		
		genresMap.put("hiphoprnbanddancehall", "HipHop, R&B & Dance");
		genresMap.put("HipHop, R&B & Dance", "hiphoprnbanddancehall");		
		
		genresMap.put("classical", "Classical");
		genresMap.put("Classical", "classical");	
		
		genresMap.put("jazzandblues", "Jazz & Blues");
		genresMap.put("Jazz & Blues", "jazzandblues");	

		genresMap.put("country", "Country");
		genresMap.put("Country", "country");		
		
		genresMap.put("popandchart", "Pop & Chart");
		genresMap.put("Pop & Chart", "popandchart");		
		
		genresMap.put("danceandelectronica", "Dance & Electronica");
		genresMap.put("Dance & Electronica", "danceandelectronica");		
		
		genresMap.put("rockandindie", "Rock & Indie");
		genresMap.put("Rock & Indie", "rockandindie");
		
		genresMap.put("desi", "Desi");
		genresMap.put("Desi", "desi");
		
		genresMap.put("soulandreggae", "Soul & Raggae");
		genresMap.put("Soul & Raggae", "soulandreggae");
		
		
		genresMap.put("easylisteningsoundtracksandmusicals", "Easy Listening,Sound tracks,Musicals");
		genresMap.put("Easy Listening,Sound tracks,Musicals", "easylisteningsoundtracksandmusicals");
		
		
		genresMap.put("world", "World");
		genresMap.put("World", "world");		
		
		genresMap.put("folk", "Folk");	
		genresMap.put("Folk", "folk");
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);	
		Button goFindMusicButton = (Button)findViewById(R.id.enterSearchScheduleButton);
		
		goFindMusicButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String newSearchString1 = genreSpinner.getSelectedItem().toString();
				String newSearchString2 = genresMap.get(newSearchString1);
								
				newSearchString2 += "/" + musicEditText.getText().toString();
				
				musicEditText.setText("");
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(musicEditText.getWindowToken(), 0);
				
				
				
				saveSearchkString(newSearchString2);
				
			}
			
		});
		getSavedEventsButton = (Button)findViewById(R.id.getSavedEvents);
		
		getSavedEventsButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//String newSearchString1 = genreSpinner.getSelectedItem().toString();
				//String newSearchString2 = genresMap.get(newSearchString1);
				
				Toast.makeText(mainContext, "Getting saved events", Toast.LENGTH_SHORT).show();
				//readCalendarEvent(mainContext);
				//readCalendar(mainContext);
				

				Intent intent = new Intent(MainActivity.this, EventListActivity.class);
				
				
				startActivity(intent);
				
				
				
			}
		});
		if (getResources().getConfiguration().orientation != 1)
		{
			
			musicTableScrollView.removeAllViews();		
			deleteAllSaveSearchkString();
			
			updateSavedSearchList(null);
		}
		else
		{
			musicTableScrollView.removeAllViews();
			updateSavedSearchList(null);
			
		}
		

	}

	
	private void deleteAllSaveSearchkString()
	{
		SharedPreferences.Editor preferencesEditor = musicSearchesEntered.edit();
		preferencesEditor.clear();
		preferencesEditor.apply();
	}
	
	

	private void saveSearchkString(String newSearchString)
	{
		String isTheSearchStringNew = musicSearchesEntered.getString(newSearchString, null);

		
		if (isTheSearchStringNew == null)
		{
			SharedPreferences.Editor preferencesEditor = musicSearchesEntered.edit();
			preferencesEditor.putString(newSearchString, newSearchString);
			preferencesEditor.apply();
			updateSavedSearchList(newSearchString);
		}
		else
		{
			Toast.makeText(this, R.string.already_stored, Toast.LENGTH_SHORT).show();

			if (isNetworkAvailable(mainContext)) {
				Intent intent = new Intent(MainActivity.this, ScheduleListActivity.class);
				intent.putExtra(MUSIC_SEARCHES_SYMBOL, newSearchString);
				startActivity(intent);
			}
			else
			{
				Toast.makeText(mainContext, "You have no network available", Toast.LENGTH_SHORT).show();

			}
		}
		
	}	
	

	// added so that now the user adds the search genre and goes to find in on operation
	private void insertSearchStringInScrollViewAndClick(String searchString, int arrayIndex)
	{
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		View newSearchScheduleRow = inflater.inflate(R.layout.music_search_row, null);
		
		TextView newSearchScheduleTextView = (TextView)newSearchScheduleRow.findViewById(R.id.musicSearchTextView);
	
		newSearchScheduleTextView.setText(searchString);
		
		Button scheduleFindButton = (Button)newSearchScheduleRow.findViewById(R.id.musicSearchButton);
		scheduleFindButton.setOnClickListener(getScheduleActivityListener);
		
		Button deleteMeButton = (Button)newSearchScheduleRow.findViewById(R.id.deleteButton);
		deleteMeButton.setOnClickListener(deleteScheduleActivityListener);
		
		musicTableScrollView.addView(newSearchScheduleRow, arrayIndex );
		newSearchScheduleRow.findViewById(R.id.musicSearchButton).performClick();
	}
	
	
	
	
	
	private void insertSearchStringInScrollView(String searchString, int arrayIndex)
	{
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		View newSearchScheduleRow = inflater.inflate(R.layout.music_search_row, null);
		
		TextView newSearchScheduleTextView = (TextView)newSearchScheduleRow.findViewById(R.id.musicSearchTextView);
	
		newSearchScheduleTextView.setText(searchString);
		
		Button scheduleFindButton = (Button)newSearchScheduleRow.findViewById(R.id.musicSearchButton);
		scheduleFindButton.setOnClickListener(getScheduleActivityListener);
		
		Button deleteMeButton = (Button)newSearchScheduleRow.findViewById(R.id.deleteButton);
		deleteMeButton.setOnClickListener(deleteScheduleActivityListener);
		
		musicTableScrollView.addView(newSearchScheduleRow, arrayIndex );
		//newSearchScheduleRow.findViewById(R.id.musicSearchButton).performClick();
	}
	
	
	
	private void updateSavedSearchList(String newSearchString)
	{
		String[] searchStrings = musicSearchesEntered.getAll().keySet().toArray(new String[0]);
		Arrays.sort(searchStrings, String.CASE_INSENSITIVE_ORDER);
		if (newSearchString != null)
		{
			insertSearchStringInScrollViewAndClick(newSearchString, Arrays.binarySearch(searchStrings, newSearchString));
			
		} else
		{
			for (int i=0; i < searchStrings.length; i++)
			{
				insertSearchStringInScrollView(searchStrings[i], i);
			}
			
		}
	}
	public OnClickListener getScheduleActivityListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			//musicSearchTextView		
			
			
			View SearchScheduleRow = (TableRow)v.getParent();	
			SearchScheduleRow.setBackgroundColor(getResources().getColor(R.color.aero_blue));
			TextView searchScheduleTextView = (TextView)SearchScheduleRow.findViewById(R.id.musicSearchTextView);
			String searchScheduleString = searchScheduleTextView.getText().toString();

			if (isNetworkAvailable(mainContext))
			{
				Intent intent = new Intent(MainActivity.this, ScheduleListActivity.class);
				intent.putExtra(MUSIC_SEARCHES_SYMBOL, searchScheduleString);
				startActivity(intent);
			}
			else
			{
				Toast.makeText(mainContext, "You have no network available", Toast.LENGTH_SHORT).show();
			}
			
			
		}
		
		
		
	};


	public boolean isNetworkAvailable(final Context context) {
		final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
		return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
	}







	public OnClickListener deleteScheduleActivityListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
			View SearchScheduleRow = (TableRow)v.getParent();
			int indx = musicTableScrollView.indexOfChild(SearchScheduleRow);
			System.out.println("remove index + " + indx);
			TextView keyStringTV = (TextView)SearchScheduleRow.findViewById(R.id.musicSearchTextView);
			String keyString = keyStringTV.getText().toString();
			System.out.println("remove string+ " + keyString);
			deleteScheduleAtIndex(indx,keyString);
					
			
		}
		
		
		
	};
	
	
	private void deleteScheduleAtIndex(int index, String keyToRemove)
	{
		musicTableScrollView.removeViewAt(index);
		
		SharedPreferences.Editor editor = musicSearchesEntered.edit();
		editor.remove( keyToRemove);
		editor.apply();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            //newGame();
	            return true;
	        case R.id.action_help1:
	            showHelp();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	
	
	
	private void showHelp()
	{
		// show the help screen
		
		Intent intent = new Intent(MainActivity.this, HelpActivity.class);
		//intent.putExtra(MUSIC_SEARCHES_SYMBOL, searchScheduleString);
		startActivity(intent);
		
	}
	
	
	
    public void listTheLastSevenDaysEvents(View view)
    {
    	readCalendarEvent(this);
    	
    }
   
    
    
    // TODO - put this in a new Activity whicch puts the events on the screen
    
    public static ArrayList<String> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        int cCount = cursor.getCount();
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        for (int i = 0; i < CNames.length; i++) {

           
        	long evtDateStrt = Long.parseLong(cursor.getString(3));
            
            long now = new Date().getTime();
            String dtNow = getDate(now);
            long weekago = now - DateUtils.WEEK_IN_MILLIS;
            String nameEvent = cursor.getString(1);
            
            if ((Long.parseLong(cursor.getString(3)) > weekago) && (Long.parseLong(cursor.getString(3)) < now) && nameEvent.matches("Radio reminder"))
            {
                startDates.add(getDate(Long.parseLong(cursor.getString(3))));
                endDates.add(getDate(Long.parseLong(cursor.getString(4))));
                
            	nameOfEvent.add(cursor.getString(1));

            	
            	//(Events.TITLE, "Radio reminder"
            	
            	
                descriptions.add(cursor.getString(2));
                //CNames[i] = cursor.getString(1);

                channelpointer.add(cursor.getString(5));
                
            	
            }	
            cursor.moveToNext();

        }
        return nameOfEvent;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }        
/**	
    public ArrayList<String> readCalendarEvent2(Context context) {	
        Uri.Builder builder = Uri.parse(getCalendarUriBase() + "/instances/when").buildUpon();
        final ContentResolver resolver = getContentResolver();
        long now = new Date(now).getTime();
        ContentUris.appendId(builder, now - DateUtils.WEEK_IN_MILLIS);   	
        Cursor eventCursor = resolver.query(builder.build(),
                new String[] { "event_id"}, "Calendars._id=" + 1,
                null, "startDay ASC, startMinute ASC"); 
        // For a full list of available columns see http://tinyurl.com/yfbg76w
        while (eventCursor.moveToNext()) {
            String uid2 = eventCursor.getString(0);
            Log.v("eventID : ", uid2);

        }     
    	
    }
	
 **/   
  
    
    void readCalendar(Context context) {
    	 
        ContentResolver contentResolver = context.getContentResolver();
 
        // Fetch a list of all calendars synced with the device, their display names and whether the
        // user has them selected for display.
        
        //content://com.android.calendar/events
        
 
        final Cursor cursor = contentResolver.query(Uri.parse("content://calendar/calendars"),
                (new String[] { "_id", "displayName", "selected" }), null, null, null);
        // For a full list of available columns see http://tinyurl.com/yfbg76w
 
        HashSet<String> calendarIds = new HashSet<String>();
 
        while (cursor.moveToNext()) {
 
            final String _id = cursor.getString(0);
            final String displayName = cursor.getString(1);
            final Boolean selected = !cursor.getString(2).equals("0");
 
            Log.v("anim","Id: " + _id + " Display Name: " + displayName + " Selected: " + selected);
            calendarIds.add(_id);
        }
 
        // For each calendar, display all the events from the previous week to the end of next week.        
        for (String id : calendarIds) {
            Uri.Builder builder = Uri.parse("content://calendar/instances/when").buildUpon();
            long now = new Date().getTime();
            ContentUris.appendId(builder, now - DateUtils.WEEK_IN_MILLIS);
            ContentUris.appendId(builder, now + DateUtils.WEEK_IN_MILLIS);
 
            Cursor eventCursor = contentResolver.query(builder.build(),
                    new String[] { "title", "begin", "end", "allDay"}, "Calendars._id=" + id,
                    null, "startDay ASC, startMinute ASC");
            // For a full list of available columns see http://tinyurl.com/yfbg76w
 
            while (eventCursor.moveToNext()) {
                final String title = eventCursor.getString(0);
                final Date begin = new Date(eventCursor.getLong(1));
                final Date end = new Date(eventCursor.getLong(2));
                final Boolean allDay = !eventCursor.getString(3).equals("0");
 
                Log.v("anim","Title: " + title + " Begin: " + begin + " End: " + end +
                        " All Day: " + allDay);
            }
        }
    } 
    
    
  
    
    private String getCalendarUriBase() {
        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, null, null, null, null);
        } catch (Exception e) {
            // eat
        }

        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = managedQuery(calendars, null, null, null, null);
            } catch (Exception e) {
                // statement to print the stacktrace
            }

            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }

        }

        return calendarUriBase;
    }



}
