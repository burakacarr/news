
public class Haberturk extends RSS  {

	public Haberturk() {
		super("Habert�rk", "https://www.haberturk.com/rss");
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
