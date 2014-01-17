package pl.programa.beerquest.model;

/**
 * post model
 * @author Programa.pl
 */
public class Post {
	private String json;
	private String url;

	public Post(String json, String url) {
		super();
		this.json = json;
		this.url = url;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}
