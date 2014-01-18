package pl.programa.beerquest.model;

public class QuestList {

	Quest[] items;
	
	
	
	public Quest[] getListQuests() {
		return items;
	}



	public void setListQuests(Quest[] listQuests) {
		this.items = listQuests;
	}



	public QuestList(Quest[] listQuests) {
		super();
		this.items = listQuests;
	}




}
