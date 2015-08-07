package com.sr178.game.tool.msgtool.bean;

import java.util.List;

public class MsgEntry {
   private EntryLine pakage;
   private EntryLine action;
   private List<EntryMsgStruct> msgStruct;
   private List<EntryLine> erroCodeList;
	public EntryLine getPakage() {
		return pakage;
	}
	public void setPakage(EntryLine pakage) {
		this.pakage = pakage;
	}
	public EntryLine getAction() {
		return action;
	}
	public void setAction(EntryLine action) {
		this.action = action;
	}
	public List<EntryMsgStruct> getMsgStruct() {
		return msgStruct;
	}
	public void setMsgStruct(List<EntryMsgStruct> msgStruct) {
		this.msgStruct = msgStruct;
	}
	public List<EntryLine> getErroCodeList() {
		return erroCodeList;
	}
	public void setErroCodeList(List<EntryLine> erroCodeList) {
		this.erroCodeList = erroCodeList;
	}
}
