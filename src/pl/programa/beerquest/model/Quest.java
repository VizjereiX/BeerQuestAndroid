package pl.programa.beerquest.model;

import java.util.Random;

import com.google.gson.Gson;

public class Quest {

	int questid;
	String name;
	String startts;
	int type = 1;
	int minguests = 1;
	String confirmts;
	String status;
	boolean participate;
	String[] members = {};
	
	public static final String STATUS_NEW = "new";
	public static final String STATUS_CONFIRMED = "confirmed";
	public static final String STATUS_ACTIVE = "active";
	public static final String STATUS_DONE = "done";

	/**
	 * 
	 */
	public Quest() {
		name = "Quest_" + new Random().nextInt(10000);
		startts = "18-01-2014 18:00";
		confirmts = "18-01-2014 15:00";
		minguests = 2;
	}

	public Quest(int id, String name, String startTs, int type, int minGuests,
			String confirmTs, String status, boolean participate) {
		super();
		this.questid = id;
		this.name = name;
		this.startts = startTs;
		this.type = type;
		this.minguests = minGuests;
		this.confirmts = confirmTs;
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


	public int getType() {
		return type;
	}

	public int getMinGuests() {
		return minguests;
	}

	public void setMinGuests(int minGuests) {
		this.minguests = minGuests;
	}

 
	public void setId(int id) {
		this.questid = id;
	}

	public int getId() {
		return this.questid;
	}

	public static Quest  fromJson(String json) {
		return new Gson().fromJson(json, Quest.class);
	}
	
	public String[] getMembers() {
		return this.members;
	}
	
	public void setMembers(String[] members) {
		this.members = members;
	}

	public String getStartTs() {
		return startts;
	}

	public void setStartTs(String startTs) {
		this.startts = startTs;
	}

	public String getConfirmTs() {
		return confirmts;
	}

	public void setConfirmTs(String confirmTs) {
		this.confirmts = confirmTs;
	}
}
