
public class Haberturk extends RSS  {

	public Haberturk() {
		super("Habertürk", "https://www.haberturk.com/rss");
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
