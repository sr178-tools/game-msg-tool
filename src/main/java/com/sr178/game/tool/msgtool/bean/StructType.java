package com.sr178.game.tool.msgtool.bean;

public enum StructType {
	INT("Integer", "int", true), SHORT("Short", "short", true), BOOLEAN("Boolean",
			"boolean", true), LONG("Long", "long", true), BYTE("Byte", "byte",
			true), LIST("List", "list", false), MAP("Map", "map", false), STRING(
			"String", "string", true),SELF_DEFINE("se","se",false);
   public String productName;
   public String textName;
   public boolean isBaseType;
   private StructType(String productName,String textName,boolean isBaseType){
	   this.productName = productName;
	   this.textName = textName;
	   this.isBaseType = isBaseType;
   }
}
