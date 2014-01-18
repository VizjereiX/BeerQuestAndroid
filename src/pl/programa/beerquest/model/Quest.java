package pl.programa.beerquest.model;

import java.util.Random;

import com.google.gson.Gson;

public class Quest {

	int id;
	String name;
	int startTs;
	int type = 1;
	int minGuests = 1;
	int confirmTs;
	String status;
	boolean participate;
	
	public static final String STATUS_NEW = "new";
	public static final String STATUS_CONFIRMED = "confirmed";
	public static final String STATUS_ACTIVE = "active";
	public static final String STATUS_DONE = "done";

	/**
	 * 
	 */
	public Quest() {
		name = "Quest_" + new Random().nextInt(10000);
		startTs = 1390075200 + new Random().nextInt(100);
		confirmTs = 1390071600 + new Random().nextInt(100);
		minGuests = 2;
	}

	public Quest(int id, String name, int startTs, int type, int minGuests,
			int confirmTs, String status, boolean participate) {
		super();
		this.id = id;
		this.name = name;
		this.startTs = startTs;
		this.type = type;
		this.minGuests = minGuests;
		this.confirmTs = confirmTs;
		this.status = status;
		this.participate = participate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getParticipate() {
		return participate;
	}

	public void setParticipate(boolean participate) {
		this.participate = participate;
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

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static Quest  fromJson(String json) {
		return new Gson().fromJson(json, Quest.class);
	}
}
