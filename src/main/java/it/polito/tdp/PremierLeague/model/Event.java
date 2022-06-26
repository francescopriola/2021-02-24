package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		GOAL,
		ESPULSIONE,
		INFORTUNIO
	}
	
	private int time;
	private EventType type;
	private Team team;
	
	public Event(int time, EventType type, Team team) {
		super();
		this.time = time;
		this.type = type;
		this.team = team;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	@Override
	public int compareTo(Event o) {
		return this.time - o.time;
	}
	
	

}
