package co.uk.dmott.mymusicfinder;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ScheduleEntryDetailScreen extends Activity {

	private String strtTime;
	private String endTime;	
	private String strtDate;
	private String endDate;
	private String start;
	private String end;
	private boolean isTomorrow;
	
	private String displayTitlesTitle;		
	private String shortSynopsis;			
	private String serviceTypeTitle;	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.schedule_list_view);
        setContentView(R.layout.list_item_detail);
		Intent theIntent = getIntent();
		
		start = theIntent.getStringExtra("startTime");
		end = theIntent.getStringExtra("endTime");
		
		strtTime = start.substring(11, 16);
		endTime = end.substring(11, 16);

		strtDate = start.substring(8, 10);
		endDate = end.substring(8, 10);
		
		
		
	    Calendar rightNow = Calendar.getInstance();
	    int calDate = rightNow.get(Calendar.DATE);
		 
		 
		if (Integer.parseInt(strtDate) == calDate)
		{
			isTomorrow = false;
		}
		else
		{
			isTomorrow = true;
		}			
		
		
		
		displayTitlesTitle = theIntent.getStringExtra("displayTitlesTitle");		
		shortSynopsis = theIntent.getStringExtra("shortSynopsis");			
		serviceTypeTitle = theIntent.getStringExtra("serviceTypeTitle");
		String mediaType = theIntent.getStringExtra("mediaType");		
 
	
		ImageView iconVw = (ImageView) findViewById(R.id.detailsMediaTypeImageView); 
		
		
		 if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio3"))
		 {
		 
			 iconVw.setImageResource(R.drawable.radio3);
		 }
		 else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio6"))
		 {
			 
			 iconVw.setImageResource(R.drawable.radio6);
		 }					 
		 else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("cymru"))
		 {
			 
			 iconVw.setImageResource(R.drawable.cymru);
		 }
		 else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio2"))
		 {
			 
			 iconVw.setImageResource(R.drawable.radio2);
		 }					 
		 else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio1"))
		 {
			 
			 iconVw.setImageResource(R.drawable.radio1);
		 }
		 else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("radio4"))
		 {
			 
			 iconVw.setImageResource(R.drawable.radio4);
		 }					 
		 else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("bbc4"))
		 {
			 
			 iconVw.setImageResource(R.drawable.bbc4);
		 }	
		 else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("bbcasian"))
		 {
			 
			 iconVw.setImageResource(R.drawable.asian);
		 }		 
		 else if (serviceTypeTitle.toLowerCase().replaceAll("[ \t]", "").contains("scotland"))
		 {
			 
			 iconVw.setImageResource(R.drawable.scot);
		 }				 
	
		
		TextView startEndTV = (TextView) findViewById(R.id.detailsTimeTextView);
		String startEndStr = strtTime + "-" + endTime;
		startEndTV.setText(startEndStr);
		
		TextView titleTV = (TextView) findViewById(R.id.detailsTitleTextView);
		titleTV.setText(displayTitlesTitle);
		
		TextView synopsisTV = (TextView) findViewById(R.id.detailsOverviewTextView);
		synopsisTV.setText(shortSynopsis);
		
		
		TextView serviceTV = (TextView) findViewById(R.id.detailsServiceTitleTextView);
		serviceTV.setText("Service : "+ serviceTypeTitle );
		
		
		TextView mediaTV = (TextView) findViewById(R.id.detailsMediaTypeTextView);
		mediaTV.setText("Media : "+ mediaType);
		

		
	
	}
	
	
    public void saveProgrammeToCalendar(View view)
    {
    	
	     Calendar rightNow = Calendar.getInstance();
	     int calDate = rightNow.get(Calendar.DATE);
	     
	     String strtyear = start.substring(0,4 );
	     String strtmonth = start.substring(5,7 );
	     //String strtdate = start.substring((strtDate.length()-12), (strtDate.length()-10));
	     String endyear = end.substring(0,4 );
	     String endmonth = end.substring(5,7 );
	     //String enddate = endDate.substring((strtDate.length()-12), (strtDate.length()-10));
	     
	     
	     Calendar beginTime = Calendar.getInstance();
	     beginTime.set(Integer.parseInt(strtyear), Integer.parseInt(strtmonth)-1, Integer.parseInt(strtDate), Integer.parseInt(strtTime.substring(0,2)), Integer.parseInt(strtTime.substring(3,5)));
	     Calendar finishTime = Calendar.getInstance();
	     finishTime.set(Integer.parseInt(endyear), Integer.parseInt(endmonth)-1, Integer.parseInt(endDate), Integer.parseInt(endTime.substring(0,2)), Integer.parseInt(endTime.substring(3,5)));

	     
	     
	     
	     
	     Intent intent = new Intent(Intent.ACTION_INSERT)
	     // need to change min api to 14 for use of CONTENT_URI??
	             .setData(Events.CONTENT_URI)
	             .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
	             .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, finishTime.getTimeInMillis())
	             .putExtra(Events.TITLE, "Radio reminder")
 	             .putExtra(Events.DESCRIPTION, displayTitlesTitle + ":" + shortSynopsis)
	             .putExtra(Events.EVENT_LOCATION, serviceTypeTitle);
	     startActivity(intent);
	     
	     
	     
	     System.out.println("You hit the save to calendar button");
    	
    	
    }
	
		
	
	
	
	
}
