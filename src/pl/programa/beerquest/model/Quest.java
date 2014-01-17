package pl.programa.beerquest.model;

import java.util.Random;

public class Quest {

	String name;
	int startTs;
	int type = 1;
	int minGuests = 1;
	int confirmTs;

	/**
	 * 
	 */
	public Quest() {
		name = "Quest_" + new Random().nextInt(10000);
		startTs = 1390075200 + new Random().nextInt(100);
		startTs = 1390071600 + new Random().nextInt(100);
		minGuests = 2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartTs() {
		return startTs;
	}

	public void setStartTs(int startTs) {
		this.startTs = startTs;
	}

	public int getType() {
		return type;
	}

	public int getMinGuests() {
		return minGuests;
	}

	public void setMinGuests(int minGuests) {
		this.minGuests = minGuests;
	}

	public int getConfirmTs() {
		return confirmTs;
	}

	public void setConfirmTs(int confirmTs) {
		this.confirmTs = confirmTs;
	}

}
