package com.laoniu.entity;

public class MusicLrc {

	private String time;
	
	private String lrc;

	
	
	public MusicLrc() {
		super();
	}
	public MusicLrc(String time, String lrc) {
		super();
		this.time = time;
		this.lrc = lrc;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLrc() {
		return lrc;
	}
	public void setLrc(String lrc) {
		this.lrc = lrc;
	}
	
	
}
