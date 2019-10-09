

public class CNBC extends RSS {

	public CNBC() {
		super("CNBC", "https://www.cnbc.com/id/100727362/device/rss/rss.html");
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
	
}

