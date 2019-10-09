import java.util.ArrayList;
import java.util.List;

public abstract class RSS {

	private String link,name;
	public RSS(String name, String link) {
		this.link = link;
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	public  List<News> newsList =  new ArrayList<News>();
	
	
}
