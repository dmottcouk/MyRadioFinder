package co.uk.dmott.mymusicfinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.text.TextUtils;
import android.view.ViewGroup;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScheduleListActivity extends ListActivity{

	  
	    private final String bbcURLFirst = "http://www.bbc.co.uk/programmes/genres/music/";
	    String bbcURLTotal;
	    private static final String ns = null;
	    private ListView mListView;
	    private boolean finishedNetwork;
	   // private ProgressDialog pd;
	    
	    
	    
	    
	    private Context context;
	    private String deepSearch = "";
	    private HashMap<Integer,Integer> deepSearchLinking;
		    
	    String[][] xmlPullParserArray = {{"start", "0"},{"end", "0"},
				{"serviceTypeTitle", "0"},{"shortSynopsis", "0"},{"mediaType", "0"},{"displayTitlesTitle", "0"}
				};
		

	    List<ScheduleEntry> broadcasts;
	
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.schedule_list_view);
	        setContentView(R.layout.schedule_list_view2);
		    broadcasts = new ArrayList<ScheduleEntry>();
		    finishedNetwork = false;
		    context = this;
		    
	        
	        // Get the message from the intent that has the music query string
	        Intent intent = getIntent();
	        String musicSearchString = intent.getStringExtra(MainActivity.MUSIC_SEARCHES_SYMBOL);
	        
	        String[] urlAndDeepSearch = musicSearchString.split("/");
	        if (urlAndDeepSearch.length > 1)
	        	deepSearch = urlAndDeepSearch[1];
	        
			deepSearchLinking   = new HashMap<Integer,Integer>();
	        Calendar rightNow = Calendar.getInstance();
	        
	        int calYear = rightNow.get(Calendar.YEAR);
	        int calDate = rightNow.get(Calendar.DATE);
	        int calMonth = rightNow.get(Calendar.MONTH) + 1;
	        
	        String strcalYear = String.format("%04d", calYear);
	        String strcalDate = String.format("%02d", calDate);
	        String strcalMonth = String.format("%02d", calMonth);
	        
	        
	        
	        
	        //bbcURLTotal = bbcURLFirst + musicSearchString + "schedules/" + strcalYear + "/" + strcalMonth + "/" + strcalDate + ".xml";
	        bbcURLTotal = bbcURLFirst + urlAndDeepSearch[0] + "/schedules/" + strcalYear + "/" + strcalMonth + "/" + strcalDate + ".xml";

	        
	        Toast.makeText(this, "Getting listings", Toast.LENGTH_LONG).show();
	        
	        getScheduleDataForListView();
	        
	        //while (finishedNetwork == false)
	        //	;
	        System.out.println("Finished network");
/**	        
			mListView = (ListView)findViewById(R.id.list);
	
			 
			mListView.setOnItemClickListener(new OnItemClickListener() {
					
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						

											
						System.out.println("Position is " + position);
						int realPosition = position;
						
						if (!deepSearch.equals(""))
							realPosition = deepSearchLinking.get(position);
						
						
						
						ScheduleEntry clickedEntry = broadcasts.get(realPosition);
						
						String startTime = clickedEntry.getStart();
						String endTime = clickedEntry.getEnd();
						String serviceTypeTitle = clickedEntry.getServiceTypeTitle();						
						String displayTitlesTitle = clickedEntry.getDisplayTitlesTitle();	
						String shortSynopsis = clickedEntry.getShortSynopsis();
						String mediaType = clickedEntry.getMediaType();

						Intent  theIndent = new Intent(getApplication(),ScheduleEntryDetailScreen.class);
						
						theIndent.putExtra("startTime", startTime); 
						theIndent.putExtra("endTime", endTime); 
						theIndent.putExtra("serviceTypeTitle", serviceTypeTitle); 
						theIndent.putExtra("displayTitlesTitle", displayTitlesTitle); 
						theIndent.putExtra("shortSynopsis", shortSynopsis); 
						theIndent.putExtra("mediaType", mediaType); 
						
						
						startActivity(theIndent); 
							
					}
				}); 			 
			 
		**/	 
		 
			 
			// ArrayList<HashMap<String, String>> data = GetBroadcastData(broadcasts);
	
	/**		 
			 
			 
			 SimpleAdapter adapter = new SimpleAdapter(this, data,
			  R.layout.schedule_list_item, new String[] { "mediaTypeImageView", "startTextView", "titleTextView", "synopsisTextView" },
			  new int[] { R.id.mediaTypeImageView, R.id.startTextView, R.id.detailsTitleTextView, R.id.synopsisTextView });
			  
		 
			 
			 mListView.setAdapter(adapter);
			 
			 if (data.isEmpty())
			 {
				 Toast.makeText(this, R.string.nobroadcastsfound, Toast.LENGTH_SHORT).show();
			 }
	**/        
	     
	    }
	
		
		
	

		@SuppressLint("DefaultLocale")
		private ArrayList<HashMap<String, String>> GetBroadcastData(List<ScheduleEntry> broadcasts)
		{
			 ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			 System.out.println("In GetBroadcastData");
			 int indxcntr = 0;
			 int deepSearchLinkIndex = 0;
			 
			 for (ScheduleEntry se: broadcasts )
			 {
				 String synpsismodified; 
				 String synpsis;
				 String titleModified;
				 String title;
				 //String[] listOfUndesirableStrings = {"from","the","lands","of","into","her","his","their","in","for","and","presents","featuring","this","is","by","an","An","performs","music","with" };
				 
				 HashMap map = new HashMap();

				 

				  
				 synpsis  = se.getShortSynopsis();
				 synpsismodified  = se.getShortSynopsis();
				 boolean includeInResults = true;
				 titleModified = se.getDisplayTitlesTitle();
				 title = se.getDisplayTitlesTitle();
				 int titleLength = titleModified.length();
				 
				 //Are we doing deep search
				 if (!deepSearch.equals(""))
				 {
					 
					 if ((synpsis.toLowerCase().contains(deepSearch)) ||(title.toLowerCase().contains(deepSearch) ))
					 {
						 includeInResults = true;
						 deepSearchLinking.put(deepSearchLinkIndex++, indxcntr);
						 
					 }
					 else
					 {
						 includeInResults = false;
						 
					 }						 
					 
				 }
				 
				 if (includeInResults == true)
				 {
				 
					 if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("radio3"))
					 {
				 
						 map.put("mediaTypeImageView", R.drawable.radio3);
					 }
					 else if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("radio6"))
					 {
					 
						 map.put("mediaTypeImageView", R.drawable.radio6);
					 }					 
					 else if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("cymru"))
					 {
					 
						 map.put("mediaTypeImageView", R.drawable.cymru);
					 }
					 else if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("radio2"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.radio2);
					 }					 
					 else if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("radio1"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.radio1);
					 }			 
					 else if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("radio4"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.radio4);
					 }					 
					 else if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("bbcfour"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.bbc4);
					}	
					 else if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("bbcasian"))
					{
					 
						 map.put("mediaTypeImageView",R.drawable.asian);
					}		 
					 else if (se.getServiceTypeTitle().toLowerCase().replaceAll("[ \t]", "").contains("scotland"))
					 {
					 
						 map.put("mediaTypeImageView",R.drawable.scot);
					 }		 
				 
					 else 
					  map.put("mediaTypeImageView", R.drawable.ic_launcher);
				  
					 @SuppressWarnings("unused")
					 //String strtTime = se.getStart().substring((se.getStart().length()-9), (se.getStart().length()-4));
					 String strtTime = se.getStart().substring(11, 16);
					 
					 String strtDate = se.getStart().substring(8, 10);
					 
					 
					 //String endTime = se.getEnd().substring((se.getStart().length()-9), (se.getStart().length()-4));
					 String endTime = se.getEnd().substring(11,16);
					 
					 
					 String endDate = se.getEnd().substring(8, 10);
					 
					 
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+from[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+the[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("The[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+lands[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+of[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+into[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+her[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+his[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+their[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+in[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+for[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+and[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+presents[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+present[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+featuring[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+this[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+is[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+by[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+An[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+an[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+performs[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+music[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+a[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+with[ \t]+", " ");
					 synpsismodified  = synpsismodified.replaceAll("[ \t]+Philharmonic[ \t]+", "Phil ");
				  //synpsismodified  = synpsismodified.("and", "/");
				  
					 synpsismodified  = synpsismodified.replaceAll("[ \t]", "/");
				  
				  
				  
				  //synpsismodified = TextUtilsRemove(synpsis,listOfUndesirableStrings );
				  
				  
				  
					 int synopLength = synpsismodified.length();
					 if (synopLength <= 58) // 49
						 synpsis  = synpsismodified.toLowerCase();
					  
					 else
					 {
						//synpsis  = synpsismodified.substring(0,49).toLowerCase();
					 	synpsis  = synpsismodified.substring(0,58).toLowerCase();
					 	synpsis += "..";
					 }
					 //dfv.
				  

				     Calendar rightNow = Calendar.getInstance();
				     int calDate = rightNow.get(Calendar.DATE);
					 
					 
					 if (Integer.parseInt(endDate) == calDate)
						 map.put("startTextView", "A:"+strtTime + "-" + endTime );
					 else if ((Integer.parseInt(strtDate) == calDate) && (Integer.parseInt(endDate) != calDate))
						 map.put("startTextView", "B:"+strtTime + "-" + endTime );
					 else
						 map.put("startTextView", "C:"+strtTime + "-" + endTime );
				  
				  

				  
					 if (titleLength <= 23)
						 titleModified  = title.toLowerCase();
					  
					 else
						 titleModified  = title.toLowerCase().substring(0,23) + ".";			  
				  
				 				  
					 map.put("titleTextView", titleModified);
				  
					 map.put("synopsisTextView", synpsis);
				  
				  
					 list.add(map);
				 }
				 
				 indxcntr++;
				 
			 }
					
			return list;
		}	
		
		
		
	    
	    private String TextUtilsRemove(String synpsismodified, String[] undesiredStrings) {
	    	
	    	String [] replacements = new String[undesiredStrings.length];
	    	int indx = 0;
	    	for (String str: undesiredStrings)
	    		replacements[indx++] = "/";
	    	
	    	
	    	String newString = TextUtils.replace(synpsismodified, undesiredStrings, replacements).toString();
	    	
	    	
	    	
	    	
			return newString;
		}





		public void getScheduleDataForListView()
	    {

	    	new MyAsyncTask().execute(bbcURLTotal);
	    	
	    	//return scheduleEntryList;
	    	//return broadcasts;
	    	
	    }
	    
	    
		private class MyAsyncTask extends AsyncTask<String, String, String>{

			
			@Override
			protected String doInBackground(String... args) {

		         try{
	                 
		                Log.d("test","In XmlPullParser");	
		                
		                
		                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		           	 
		                // Parser supports XML namespaces
		                factory.setNamespaceAware(true);
		 
		                // Provides the methods needed to parse XML documents
		                XmlPullParser parser = factory.newPullParser();
		 
		                // InputStreamReader converts bytes of data into a stream
		                // of characters
		                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);   
		                parser.setInput(new InputStreamReader(getUrlData(args[0]))); 
		 
		                  // Passes the parser and the first tag in the XML document
		                  // for processing
		                   
		                beginDocument(parser,"broadcasts");   // works
		                readFeed(parser);
		                
		                
		                
		                
		                
		                Log.d("test","Size of broadcasts " + broadcasts.size());	
		                Log.d("test","Finished reading feed");	
		                
		                Thread.sleep(5000);

		
		         } 
		         
		         catch (ClientProtocolException e) 
		         {

		                e.printStackTrace();
		         } catch (XmlPullParserException e) {

		                e.printStackTrace();
		         } catch (URISyntaxException e) {

		                e.printStackTrace();
		         } catch (IOException e) {

		                e.printStackTrace();
		         } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		             
		         finally {
						//finishedNetwork = true;
		         }
		         
		         
       
		         
		         return null;
			}
			
			@Override
			protected void onPostExecute(String result)
			{
								
				//finishedNetwork = true;
				
				 ArrayList<HashMap<String, String>> data = GetBroadcastData(broadcasts);
				 SimpleAdapter adapter = new SimpleAdapter(context, data,
						  R.layout.schedule_list_item, new String[] { "mediaTypeImageView", "startTextView", "titleTextView", "synopsisTextView" },
						  new int[] { R.id.mediaTypeImageView, R.id.startTextView, R.id.detailsTitleTextView, R.id.synopsisTextView }){

				        // here is the method you need to override, to achieve colorful list

				        @Override
				        public View getView(int position, View convertView, ViewGroup parent) {

				            View view = super.getView(position, convertView, parent);

				            HashMap < String, String > items = (HashMap < String, String > ) getListView()
				                .getItemAtPosition(position);
				            
				            String progTime = items.get("startTextView");
				            
				            
				            
				            
				            Calendar rightNow = Calendar.getInstance();
				            Integer hourNow = rightNow.get(Calendar.HOUR_OF_DAY);
				            Integer minNow = rightNow.get(Calendar.MINUTE);
				            
				            
				            
				            
				            
				            
				            
				            Integer progHourStart = Integer.parseInt(progTime.substring(2, 4));
				            if(progTime.substring(0, 1).equalsIgnoreCase("C"))
				            	progHourStart += 24;
				            Integer progMinStart = Integer.parseInt(progTime.substring(5, 7));

				            
				            Integer progHourFinish = Integer.parseInt(progTime.substring(8, 10));
				            if((progTime.substring(0, 1).equalsIgnoreCase("B")) || (progTime.substring(0, 1).equalsIgnoreCase("C")))
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
				 
				 
				 
				 
				 
				 if (data.isEmpty())
				 {
					 Toast.makeText(context, R.string.nobroadcastsfound, Toast.LENGTH_SHORT).show();
				 }
				 else
				 {
						mListView = (ListView)findViewById(android.R.id.list);
						
						 
						mListView.setOnItemClickListener(new OnItemClickListener() {
								
								public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
									

														
									System.out.println("Position is " + position);
									int realPosition = position;
									
									if (!deepSearch.equals(""))
										realPosition = deepSearchLinking.get(position);
									
									
									
									ScheduleEntry clickedEntry = broadcasts.get(realPosition);
									
									String startTime = clickedEntry.getStart();
									String endTime = clickedEntry.getEnd();
									String serviceTypeTitle = clickedEntry.getServiceTypeTitle();						
									String displayTitlesTitle = clickedEntry.getDisplayTitlesTitle();	
									String shortSynopsis = clickedEntry.getShortSynopsis();
									String mediaType = clickedEntry.getMediaType();

									Intent  theIndent = new Intent(getApplication(),ScheduleEntryDetailScreen.class);
									
									theIndent.putExtra("startTime", startTime); 
									theIndent.putExtra("endTime", endTime); 
									theIndent.putExtra("serviceTypeTitle", serviceTypeTitle); 
									theIndent.putExtra("displayTitlesTitle", displayTitlesTitle); 
									theIndent.putExtra("shortSynopsis", shortSynopsis); 
									theIndent.putExtra("mediaType", mediaType); 
									
									
									startActivity(theIndent); 
										
								}
							});				 
					 
					 
					 
					 
					 
					 
					 
					 
				 }
				
				
			}
			

		}
		
		
		private List<ScheduleEntry> readFeed(XmlPullParser parser)
		            throws XmlPullParserException, IOException {

		        Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "List<ScheduleEntry> readFeed");
		        //List<ScheduleEntry> schedulesList = new ArrayList<ScheduleEntry>();

		        //parser.require(XmlPullParser.START_TAG, ns, "lfm");
		        while (parser.next() != XmlPullParser.END_TAG) {
		        	if (parser.getEventType() != XmlPullParser.START_TAG) {
		        		continue;
		        	}
		        	String tag = parser.getName();
		        	if (tag.equals("brdgdfgfdg")) {
		        		Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == " + tag);
		        		tag = parser.getName();

		        	} else if (tag.equals("broadcast")) {
		        		Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == " + tag);
		        		broadcasts.add(readBroadcast(parser));
		        	} else {
		        		skip(parser);
		        	}	
		        }
		        return broadcasts;
		  }
		
		
		
		  private ScheduleEntry readBroadcast(XmlPullParser parser)
		                throws XmlPullParserException, IOException {

		            Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readBroadcast");
		            //parser.require(XmlPullParser.START_TAG, ns, "artist");

		            String start = "dummy";
		            String end = "dummy";
		            String serviceTypeTitle = "dummy";
		            
		            
		            String displayTitlesTitle = "dummy";
		            String shortSynopsis = "dummy";
		            String mediaType = "dummy";
		            
		        	//public ScheduleEntry(String start, String end, String serviceTypeTitle, String displayTitlesTitle,String shortSynopsis, String mediaType) {
		            
		            
		            while (parser.next() != XmlPullParser.END_TAG) {
		                if (parser.getEventType() != XmlPullParser.START_TAG) {
		                    continue;
		                }
		                String tag = parser.getName();
		                if (tag.equals("start")) {
		                	start = readStart(parser);
		                	xmlPullParserArray[0][1] = start;
		                    Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == 'start': " + start);
		                } else if (tag.equals("end")) {
		                    end = readEnd(parser);
		                	xmlPullParserArray[1][1] = end;
		                    Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == 'end': " + end);
		                } else if (tag.equals("service")) {
		                    serviceTypeTitle = readService(parser);
		                    xmlPullParserArray[2][1] = serviceTypeTitle;
		                    Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == 'serviceTypeTitle': " + serviceTypeTitle);
		                } 
		                else if (tag.equals("programme")) {
		                    readProgramme(parser, xmlPullParserArray);
		                }else {
		                    skip(parser);
		                }
		            }

		            shortSynopsis = xmlPullParserArray[3][1];
		            mediaType = xmlPullParserArray[4][1];		            
		            displayTitlesTitle = xmlPullParserArray[5][1];
		            
		            
		            return new ScheduleEntry(start, end, serviceTypeTitle, shortSynopsis,mediaType, displayTitlesTitle );
		            		            
		        }
		  
		  private void readProgramme(XmlPullParser parser, String[][] parsedValues)
	                throws XmlPullParserException, IOException 
	      {
			  Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readProgramme");
		      String shortSynopsis = "";
		      String mediaType = "";
		            
		      while (parser.next() != XmlPullParser.END_TAG) {
		           if (parser.getEventType() != XmlPullParser.START_TAG) {
		               continue;
		            }
		            String tag = parser.getName();
		            if (tag.equals("short_synopsis")) {
		            	shortSynopsis = readShortSynopsis(parser);
	                   xmlPullParserArray[3][1] = shortSynopsis; 	
		               Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == 'short_synopsis': " + shortSynopsis);
		             }else if (tag.equals("media_type")) {
		            	 mediaType = readMediaType(parser);
			             xmlPullParserArray[4][1] = mediaType; 	
				         Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == 'mediaType': " + mediaType);
				     }else if (tag.equals("display_titles")) {
				    	 readDisplayTitles(parser, xmlPullParserArray);
			             	

				     }else
		             
            
		             
		             
		             {
		                   skip(parser);
		             }
		            
			            
		            }

		      }		  

		  
		  private void readDisplayTitles(XmlPullParser parser, String[][] parsedValues)
	                throws XmlPullParserException, IOException 
	      {
			  Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readDisplayTitles");
			  String displayTitlesTitle;
		      while (parser.next() != XmlPullParser.END_TAG) {
		           if (parser.getEventType() != XmlPullParser.START_TAG) {
		               continue;
		            }
		            String tag = parser.getName();
		            if (tag.equals("title")) {
		            	displayTitlesTitle = readdisplayTitlesTitle(parser);
		            	xmlPullParserArray[5][1] = displayTitlesTitle; 	
		            	Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == 'displayTitlesTitle': " + displayTitlesTitle);
		            }
		            else {
		                   skip(parser);		            	
		            }
		  
		      }
	      }
		  
		  
		  private String readdisplayTitlesTitle(XmlPullParser parser) throws IOException,
          XmlPullParserException {
			   	Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readdisplayTitlesTitle");
			   	parser.require(XmlPullParser.START_TAG, ns, "title");
			   	String displayTitlesTitle = readText(parser);
			   	parser.require(XmlPullParser.END_TAG, ns, "title");
			   	return displayTitlesTitle;
		   }	
		  
		  
		  
		  
		   private String readMediaType(XmlPullParser parser) throws IOException,
           XmlPullParserException {
			   	Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readMediaType");
			   	parser.require(XmlPullParser.START_TAG, ns, "media_type");
			   	String mediaType = readText(parser);
			   	parser.require(XmlPullParser.END_TAG, ns, "media_type");
			   	return mediaType;
		   }			  
		  
		  
		  
			  
		   private String readShortSynopsis(XmlPullParser parser) throws IOException,
           XmlPullParserException {
			   	Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readShortSynopsis");
			   	parser.require(XmlPullParser.START_TAG, ns, "short_synopsis");
			   	String shortSynopsis = readText(parser);
			   	parser.require(XmlPullParser.END_TAG, ns, "short_synopsis");
			   	return shortSynopsis;
		   }	  
		  
		  
		  
		  
		  
		  
		  private String readService(XmlPullParser parser)
	                throws XmlPullParserException, IOException 
	      {

	            Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readService");
	            String title = "";
	            
	            while (parser.next() != XmlPullParser.END_TAG) {
	                if (parser.getEventType() != XmlPullParser.START_TAG) {
	                    continue;
	                }
	                String tag = parser.getName();
	                if (tag.equals("title")) {
	                	title = readServiceTitle(parser);
	                	
	                    Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "tag == 'title': " + title);
	                }else {
	                    skip(parser);
	                }
	            
		            
	            }
	            return title;
	      }
		  
		  
		   private String readServiceTitle(XmlPullParser parser) throws IOException,
           XmlPullParserException {
			   	Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readServiceTitle");
			   	parser.require(XmlPullParser.START_TAG, ns, "title");
			   	String title = readText(parser);
			   	parser.require(XmlPullParser.END_TAG, ns, "title");
			   	return title;
		   }	  
		  
		  
		  
		   private String readStart(XmlPullParser parser) throws IOException,
           XmlPullParserException {
			   	Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readStart");
			   	parser.require(XmlPullParser.START_TAG, ns, "start");
			   	String start = readText(parser);
			   	parser.require(XmlPullParser.END_TAG, ns, "start");
			   	return start;
		   }

		   private String readEnd(XmlPullParser parser) throws IOException,
           XmlPullParserException {
			   	Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readStart");
			   	parser.require(XmlPullParser.START_TAG, ns, "end");
			   	String end = readText(parser);
			   	parser.require(XmlPullParser.END_TAG, ns, "end");
			   	return end;
		   }		   
		   
		   
		   
		   
	
		   private String readText(XmlPullParser parser) throws IOException,
           XmlPullParserException {
			   Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "readText");
			   String result = "";
			   if (parser.next() == XmlPullParser.TEXT) {
				   result = parser.getText();
				   parser.nextTag();
			   }
			   return result;
   }	   
		   
		   
		   
		  private void skip(XmlPullParser parser) throws XmlPullParserException,
          IOException {

			  Log.d(MainActivity.MUSIC_SEARCHES_SYMBOL, "skip");

			  if (parser.getEventType() != XmlPullParser.START_TAG) {
				  throw new IllegalStateException();
			  }
			  int depth = 1;
			  while (depth != 0) {
				  switch (parser.next()) {
				  	case XmlPullParser.END_TAG:
				  		depth--;
				  		break;
				  	case XmlPullParser.START_TAG:
				  		depth++;
				  		break;
				  }
			  }
		  }		
		
		
		
	
		public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException
		{
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet method = new HttpGet(new URI(url));
			HttpResponse res = client.execute(method);
			return res.getEntity().getContent();
			
		}
		
		
        public final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException
        {
            int type;
             
            // next() advances to the next element in the XML
            // document being a starting or ending tag, or a value
            // or the END_DOCUMENT
             
            while ((type=parser.next()) != parser.START_TAG
                           && type != parser.END_DOCUMENT) {
                    ;
            }
         
            // Throw an error if a start tag isn't found
             
            if (type != parser.START_TAG) {
                throw new XmlPullParserException("No start tag found");
            }
         
            // Verify that the tag passed in is the first tag in the XML
            // document
            
            String topTag = parser.getName();
             
            if (!parser.getName().equals(firstElementName)) {
                    throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() +
                            ", expected " + firstElementName);
            }
        }
		
	
        
 
        
  
	/**
        

  Uri.Builder builder = Uri.parse(getCalendarUriBase() + "/instances/when").buildUpon();
        long now = new Date().getTime();
        ContentUris.appendId(builder, now - DateUtils.MILLIS_PER_DAY*10000);
        ContentUris.appendId(builder, now + DateUtils.MILLIS_PER_DAY * 10000);
and then let's say you wish to log events ID from calendar with ID = 1

Cursor eventCursor = contentResolver.query(builder.build(),
                new String[] { "event_id"}, "Calendars._id=" + 1,
                null, "startDay ASC, startMinute ASC"); 
        // For a full list of available columns see http://tinyurl.com/yfbg76w
        while (eventCursor.moveToNext()) {
            String uid2 = eventCursor.getString(0);
            Log.v("eventID : ", uid2);

        }      
        
        
        
        
        public static ArrayList<String> nameOfEvent = new ArrayList<String>();
        public static ArrayList<String> startDates = new ArrayList<String>();
        public static ArrayList<String> endDates = new ArrayList<String>();
        public static ArrayList<String> descriptions = new ArrayList<String>();

        public static ArrayList<String> readCalendarEvent(Context context) {
            Cursor cursor = context.getContentResolver()
                    .query(
                            Uri.parse("content://com.android.calendar/events"),
                            new String[] { "calendar_id", "title", "description",
                                    "dtstart", "dtend", "eventLocation" }, null,
                            null, null);
            cursor.moveToFirst();
            // fetching calendars name
            String CNames[] = new String[cursor.getCount()];

            // fetching calendars id
            nameOfEvent.clear();
            startDates.clear();
            endDates.clear();
            descriptions.clear();
            for (int i = 0; i < CNames.length; i++) {

                nameOfEvent.add(cursor.getString(1));
                startDates.add(getDate(Long.parseLong(cursor.getString(3))));
                endDates.add(getDate(Long.parseLong(cursor.getString(4))));
                descriptions.add(cursor.getString(2));
                CNames[i] = cursor.getString(1);
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
        
        
        
        
        
        
        **/
        
        
		
}
