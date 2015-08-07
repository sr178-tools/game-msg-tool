package com.sr178.game.tool.msgtool.fileparse;

public enum Type{
	JAVA("struct//java"),LUA("struct//lua"),DOC("struct//doc");
	public String path;
	Type(String pathstr){
		this.path = pathstr;
	}
}