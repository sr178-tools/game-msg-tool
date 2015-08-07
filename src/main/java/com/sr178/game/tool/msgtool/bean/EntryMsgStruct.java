package com.sr178.game.tool.msgtool.bean;

import java.util.List;

/**
 * 消息体内结构定义
 * @author mc
 *
 */
public class EntryMsgStruct {
	 private EntryLine className;
     private List<StructEntry> params;

	public List<StructEntry> getParams() {
		return params;
	}

	public void setParams(List<StructEntry> params) {
		this.params = params;
	}

	public EntryLine getClassName() {
		return className;
	}

	public void setClassName(EntryLine className) {
		this.className = className;
	}
	
}
