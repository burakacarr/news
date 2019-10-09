

public class TheGuardian extends RSS {

	public TheGuardian() {
		super("The Guardian", "https://www.theguardian.com/world/zimbabwe/rss");
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
	
}