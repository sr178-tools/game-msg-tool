package com.sr178.game.tool.msgtool.bean;

public class EntryLine {
    private String key;
    private String value;
    private String desc;
	public EntryLine() {
	}
	public EntryLine(String key, String value, String desc) {
		this.key = key;
		this.value = value;
		this.desc = desc;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
