package com.sr178.game.tool.msgtool.bean;


public class StructEntry {
   private StructType type;
   private String paramName;
   //list或map的值类型
   private StructEntry valueType;
   //如果是自定义类型 则此处放置的为自定义类型的类名字
   private String className;
   //map的键类型  默认为string
   private StructType keyType= StructType.STRING;
   //描述
   private String desc;
   
	public StructType getType() {
		return type;
	}
	public void setType(StructType type) {
		this.type = type;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public StructEntry getValueType() {
		return valueType;
	}
	public void setValueType(StructEntry valueType) {
		this.valueType = valueType;
	}
	public StructType getKeyType() {
		return keyType;
	}
	public void setKeyType(StructType keyType) {
		this.keyType = keyType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getBigParamName(){
		return paramName.substring(0,1).toUpperCase()+paramName.substring(1);
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public StructEntry(StructType type, String paramName, StructEntry valueType,
			StructType keyType, String desc) {
		super();
		this.type = type;
		this.paramName = paramName;
		this.valueType = valueType;
		this.keyType = keyType;
		this.desc = desc;
	}
	public StructEntry(StructType type, String paramName, String desc) {
		super();
		this.type = type;
		this.paramName = paramName;
		this.desc = desc;
	}
	public StructEntry(StructType type, String paramName, StructEntry valueType,
			String desc) {
		super();
		this.type = type;
		this.paramName = paramName;
		this.valueType = valueType;
		this.desc = desc;
	}
	
	public StructEntry(StructType type, String paramName, String className, String desc) {
		super();
		this.type = type;
		this.paramName = paramName;
		this.className = className;
	}
	
	
	public StructEntry(StructType type, String paramName,
			StructEntry valueType, String className, StructType keyType,
			String desc) {
		super();
		this.type = type;
		this.paramName = paramName;
		this.valueType = valueType;
		this.className = className;
		this.keyType = keyType;
		this.desc = desc;
	}
	public StructEntry() {
	}
	
	public StructEntry coloneLuaBean(){
		StructEntry structEntry = new StructEntry(this.type, this.paramName,
				this.valueType, this.className, this.keyType,
				this.desc);
		if(type==StructType.SELF_DEFINE){
			structEntry.setParamName(className+"_"+paramName);
		}else{
			structEntry.setParamName(type.textName+"_"+paramName);
		}
		
		return structEntry;
	}
}
