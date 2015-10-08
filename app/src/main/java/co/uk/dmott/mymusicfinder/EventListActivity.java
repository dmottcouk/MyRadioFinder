package co.uk.dmott.mymusicfinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class EventListActivity extends ListActivity{

	private Context context;
	private ListView mListView;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.schedule_list_view);
        setContentView(R.layout.event_list_view);
        context = this;
        final ArrayList<HashMap<String, String>> data = GetEventData();
        
        
		 SimpleAdapter adapter = new SimpleAdapter(context, data,
				  R.layout.event_list_item, new String[] { "mediaTypeImageView", "dateTextView", "titleTextView", "synopsisTextView" },
				  new int[] { R.id.mediaTypeImageView, R.id.dateTextView, R.id.detailsTitleTextView, R.id.synopsisTextView }){

		        // here is the method you need to override, to achieve colorful list

		        @Override
		        public View getView(int position, View convertView, ViewGroup parent) {

		            View view = super.getView(position, convertView, parent);

		            HashMap < String, String > items = (HashMap < String, String > ) getListView()
		                .getItemAtPosition(position);
		            
		            String progTime = items.get("dateTextView");
		            System.out.println("Prog time is" + progTime);
		            
		    /**        
		            
		            Calendar rightNow = Calendar.getInstance();
		            Integer hourNow = rightNow.get(Calendar.HOUR_OF_DAY);
		            Integer minNow = rightNow.get(Calendar.MINUTE);
		            
		            Integer progHourStart = Integer.parseInt(progTime.substring(2, 4));
		            if(progTime.substring(0, 1).equalsIgnoreCase("B"))
		            	progHourStart += 24;
		            Integer progMinStart = Integer.parseInt(progTime.substring(5, 7));

		            
		            Integer progHourFinish = Integer.parseInt(progTime.substring(8, 10));
		            if(progTime.substring(0, 1).equalsIgnoreCase("B"))
		            	progHourFinish += 24;
		            Integer progMinFinish = Integer.parseInt(progTime.substring(11));				            
		            
		            
		            Integer roughTimeNow = hourNow*60 + minNow;
		            Integer roughProgTimeStart = progHourStart*60 + progMinStart;
		            Integer roughProgTimeEnd = progHourFinish*60 + progMinFinish;
		            
		            
		            
		            
		            if((roughProgTimeStart < roughTimeNow) && (roughProgTimeEnd < roughTimeNow))
		            	view.setBackgroundColor(Color.rgb(0xBC, 0xD4, 0xE6));
		            else if ((roughProgTimeStart < roughTimeNow) && (roughProgTimeEnd > roughTimeNow))
		            	view.setBackgroundColor(Color.GREEN);
		            else if ((roughProgTimeStart > roughTimeNow) && (roughProgTimeEnd > roughTimeNow))
		            	view.setBackgroundColor(Color.YELLOW);				            
		            
		     **/       
		            	
		            /**				            
		            if (Long.parseLong(items.get("id")) % 2 == 0) {
		                view.setBackgroundColor(Color.GREEN);
		            } else {
		                view.setBackgroundColor(Color.YELLOW);
		            }
		      **/
		            
		            
		            
		            return view;
		        }

		    };	

		 

		 
				         
		 mListView = (ListView)findViewById(android.R.id.list);
		 mListView.setAdapter(adapter);
        
		mListView.setOnItemClickListener(new OnItemClickListener() {
				
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					

										
					System.out.println("Position is " + position);
					
					HashMap<String, String> map1 = data.get(position);
					String desc1 = map1.get("titleTextView");
					String synop1 = map1.get("synopsisTextView");
					

						
					//iplayer is bbc.iplayer.android
					
					
					ClipboardManager clipboard = (ClipboardManager)
					        getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("simple text",desc1);
					clipboard.setPrimaryClip(clip);
					
			    	Intent i;
			    	PackageManager manager = getPackageManager();
			    	
					if (synop1.startsWith("T"))
					{
				    	try {
				    	    i = manager.getLaunchIntentForPackage("bbc.iplayer.android");
				    	    if (i == null)
				    	        throw new PackageManager.NameNotFoundException();
				    	    i.addCategory(Intent.CATEGORY_LAUNCHER);
				    	    startActivity(i);
				    	} catch (PackageManager.NameNotFoundException e) {
				    		System.out.println("Cannot find tv package name");
				    	}	
						
						
					}
			    	
					else
					{
			    	
						try {
							i = manager.getLaunchIntentForPackage("uk.co.bbc.android.iplayerradio");
							if (i == null)
								throw new PackageManager.NameNotFoundException();
							i.addCategory(Intent.CATEGORY_LAUNCHER);
							startActivity(i);
						} catch (PackageManager.NameNotFoundException e) {
							System.out.println("Cannot find radio package name");
						}	
					}

				}
				
				
				
				
				
		});
        //getIntentInfo();
		
	}
	   
	
	private ArrayList<HashMap<String, String>> GetEventData()
	{
		 ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		 
	     Cursor cursor = context.getContentResolver()
	                .query(
	                        Uri.parse("content://com.android.calendar/events"),
	                        new String[] { "calendar_id", "title", "description",
	                                "dtstart", "dtend", "eventLocation" }, null,
	                        null, null);
	     int cCount = cursor.getCount();
	     cursor.moveToFirst();
	     for (int i = 0; i < cCount; i++) {

	        	long evtDateStrt = Long.parseLong(cursor.getString(3));

	            
	            long now = new Date().getTime();
	            String dtNow = getDate(now);
	            long weekago = now - DateUtils.WEEK_IN_MILLIS;
	            String nameEvent = cursor.getString(1);
	            String channelAbbr = "";
	            
	            if ((Long.parseLong(cursor.getString(3)) > weekago) && (Long.parseLong(cursor.getString(3)) < now) && nameEvent.matches("Radio reminder"))
	            {
	            	 String channel = cursor.getString(5); // get the channel 
	 	        	 HashMap map = new HashMap();
	 	        	 
					 if (channel.toLowerCase().replaceAll("[ \t]", "").contains("radio3"))
					 {
				 
						 map.put("mediaTypeImageView", R.drawable.radio3);
						 channelAbbr = "R3";
						 
					 }
					 else if (channel.toLowerCase().replaceAll("[ \t]", "").contains("radio6"))
					 {
					 
						 map.put("mediaTypeImageView", R.drawable.radio6);
						 channelAbbr = "R6";
					 }					 
					 else if (channel.toLowerCase().replaceAll("[ \t]", "").contains("cymru"))
					 {
					 
						 map.put("mediaTypeImageView", R.drawable.cymru);
						 channelAbbr = "RCym";
					 }
					 else if (channel.toLowerCase().replaceAll("[ \t]", "").contains("radio2"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.radio2);
						 channelAbbr = "R2";
					 }					 
					 else if (channel.toLowerCase().replaceAll("[ \t]", "").contains("radio1"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.radio1);
						 channelAbbr = "R1";
					 }			 
					 else if (channel.toLowerCase().replaceAll("[ \t]", "").contains("radio4"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.radio4);
						 channelAbbr = "R4";
					 }					 
					 else if (channel.toLowerCase().replaceAll("[ \t]", "").contains("bbcfour"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.bbc4);
						 channelAbbr = "T4";
					}	
					 else if (channel.toLowerCase().replaceAll("[ \t]", "").contains("bbcasian"))
					{
					 
						 map.put("mediaTypeImageView",R.drawable.asian);
						 channelAbbr = "RCym";
					}		 
					 else if (channel.toLowerCase().replaceAll("[ \t]", "").contains("scotland"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.scot);
						 channelAbbr = "RSco";
					 }		 
				 
					 else 
					  map.put("mediaTypeImageView", R.drawable.ic_launcher);
	            	
	            	
					 String[] datesplit = getDate(Long.parseLong(cursor.getString(3))).split("[ \t]");
					 
					// map.put("dateTextView",getDate(Long.parseLong(cursor.getString(3))));
					 
					 map.put("dateTextView",datesplit[0]);
	            	
					 String[] descsplit = cursor.getString(2).split(":");
					 
					 //map.put("titleTextView", cursor.getString(1));
					 
					 if (descsplit.length > 1) // for historical reason - should be always a : soon
					 {
						 map.put("titleTextView", descsplit[0]);
						 map.put("synopsisTextView", channelAbbr + ":" + descsplit[1]);
					 }
					 else
					 {
						 map.put("titleTextView", "");
					 	 map.put("synopsisTextView", channelAbbr + ":" + descsplit[0]);
					 }
					 	
	            	 //map.put("synopsisTextView", cursor.getString(2));

					 
	            	 list.add(map);
	            } 	 
	            	 
	            cursor.moveToNext();
	     }
		 return list;
	}
	
	
	
	
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

	        ArrayList<String> nameOfEvent = new ArrayList<String>();
	        ArrayList<String> startDates = new ArrayList<String>();
	        ArrayList<String> endDates = new ArrayList<String>();
	        ArrayList<String> descriptions = new ArrayList<String>();	        
	        ArrayList<String> channelpointer = new ArrayList<String>();	        
	        
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
	    
	    public void getIntentInfo()
	    {
	    	//bbc.iplayer.android.apk
	    	Intent i;
	    	PackageManager manager = getPackageManager();
	    	try {
	    	    i = manager.getLaunchIntentForPackage("uk.co.bbc.android.iplayerradio");
	    	    if (i == null)
	    	        throw new PackageManager.NameNotFoundException();
	    	    i.addCategory(Intent.CATEGORY_LAUNCHER);
	    	    startActivity(i);
	    	} catch (PackageManager.NameNotFoundException e) {
	    		System.out.println("Cannot find Radio package name");
	    	}	
	    }
	    
	    public void getTVIntentInfo()
	    {
	    	//bbc.iplayer.android.apk
	    	Intent i;
	    	PackageManager manager = getPackageManager();
	    	try {
	    	    i = manager.getLaunchIntentForPackage("bbc.iplayer.android");
	    	    if (i == null)
	    	        throw new PackageManager.NameNotFoundException();
	    	    i.addCategory(Intent.CATEGORY_LAUNCHER);
	    	    //startActivity(i);
	    	} catch (PackageManager.NameNotFoundException e) {
	    		System.out.println("Cannot find TV package name");
	    	}	
	    }
	    
	
}
