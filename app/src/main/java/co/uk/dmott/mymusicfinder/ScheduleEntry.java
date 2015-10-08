package co.uk.dmott.mymusicfinder;

public class ScheduleEntry {

	private String start;
	private String end;
	private String serviceTypeTitle;
	private String displayTitlesTitle;
	private String shortSynopsis;
	private String mediaType;
	
	
	
	public ScheduleEntry(String start, String end, String serviceTypeTitle, String shortSynopsis, String mediaType, String displayTitlesTitle) {
		// TODO Auto-generated constructor stub
		this.start = start;
		this.end = end;
		this.serviceTypeTitle = serviceTypeTitle;
		this.displayTitlesTitle = displayTitlesTitle;	
		this.shortSynopsis = shortSynopsis;
		this.mediaType = mediaType;		
	
	
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getServiceTypeTitle() {
		return serviceTypeTitle;
	}
	public void setServiceTypeTitle(String serviceTypeTitle) {
		this.serviceTypeTitle = serviceTypeTitle;
	}
	public String getDisplayTitlesTitle() {
		return displayTitlesTitle;
	}
	public void setDisplayTitlesTitle(String displayTitlesTitle) {
		this.displayTitlesTitle = displayTitlesTitle;
	}
	public String getShortSynopsis() {
		return shortSynopsis;
	}
	public void setShortSynopsis(String shortSynopsis) {
		this.shortSynopsis = shortSynopsis;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	
	
	
	
	
}
