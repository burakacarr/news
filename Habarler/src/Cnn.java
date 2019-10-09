
public class Cnn extends RSS {

	public Cnn() {
		super("CNN", "https://www.cnnturk.com/feed/rss/dunya/news");
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
	
}
