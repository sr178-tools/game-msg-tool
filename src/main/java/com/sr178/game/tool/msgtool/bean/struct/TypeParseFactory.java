package com.sr178.game.tool.msgtool.bean.struct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaBoolean;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaByte;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaInt;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaList;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaLong;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaMap;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaSelfDefine;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaShort;
import com.sr178.game.tool.msgtool.bean.struct.java.JavaString;
import com.sr178.game.tool.msgtool.bean.struct.lua.BaseLuaStruct;
import com.sr178.game.tool.msgtool.bean.struct.lua.LuaList;
import com.sr178.game.tool.msgtool.bean.struct.lua.LuaMap;
import com.sr178.game.tool.msgtool.bean.struct.lua.LuaSelfDefine;
import com.sr178.game.tool.msgtool.fileparse.Type;

public class TypeParseFactory {
     private static final TypeParseFactory instance = new TypeParseFactory();
     private static Map<Type,Map<StructType,String>> map = new HashMap<Type,Map<StructType,String>>();
     
     private static Map<StructType,IStruct> javaStructParseMap = new HashMap<StructType,IStruct>();
     
     private static Map<StructType,IStruct> luaStructParseMap = new HashMap<StructType,IStruct>();
    
     public static TypeParseFactory getInstance(){
    	 Map<StructType,String> bmap = new HashMap<StructType,String>();
    	 
    	 bmap.put(StructType.BOOLEAN, "Boolean");
    	 bmap.put(StructType.INT, "Integer");
    	 bmap.put(StructType.SHORT, "Short");
    	 bmap.put(StructType.LONG, "Long");
    	 bmap.put(StructType.BYTE, "Byte");
    	 bmap.put(StructType.LIST, "List");
    	 bmap.put(StructType.MAP, "Map");
    	 bmap.put(StructType.STRING, "String");
    	 
    	 map.put(Type.JAVA, bmap);
    	 
    	 
    	 javaStructParseMap.put(StructType.BOOLEAN, new JavaBoolean());
    	 javaStructParseMap.put(StructType.INT, new JavaInt());
    	 javaStructParseMap.put(StructType.SHORT, new JavaShort());
    	 javaStructParseMap.put(StructType.LONG, new JavaLong());
    	 javaStructParseMap.put(StructType.BYTE, new JavaByte());
    	 javaStructParseMap.put(StructType.LIST, new JavaList());
    	 javaStructParseMap.put(StructType.SELF_DEFINE, new JavaSelfDefine());
    	 javaStructParseMap.put(StructType.STRING, new JavaString());
    	 javaStructParseMap.put(StructType.MAP, new JavaMap());
    	 
    	 
    	 luaStructParseMap.put(StructType.BOOLEAN, new BaseLuaStruct());
    	 luaStructParseMap.put(StructType.INT, new BaseLuaStruct());
    	 luaStructParseMap.put(StructType.SHORT, new BaseLuaStruct());
    	 luaStructParseMap.put(StructType.LONG, new BaseLuaStruct());
    	 luaStructParseMap.put(StructType.BYTE, new BaseLuaStruct());
    	 luaStructParseMap.put(StructType.LIST, new LuaList());
    	 luaStructParseMap.put(StructType.SELF_DEFINE, new LuaSelfDefine());
    	 luaStructParseMap.put(StructType.STRING, new BaseLuaStruct());
    	 luaStructParseMap.put(StructType.MAP, new LuaMap());
    	 
    	 return instance;
     }
     
     public IStruct getJavaStructParseTool(StructType type){
    	  return javaStructParseMap.get(type);
     }
     
     
     public IStruct getLuaStructParseTool(StructType type){
   	  return luaStructParseMap.get(type);
    }
     
     public String getJavaObjectName(StructEntry structEntry){
    		 if(structEntry.getType().isBaseType){
    			 return map.get(Type.JAVA).get(structEntry.getType());
    		 }else{
    			  if(structEntry.getType() == StructType.SELF_DEFINE){
    				  return structEntry.getClassName();
    			  }else{
    				  boolean isbeak =false;
        			  String result = "";
        			  List<String> tempType = Lists.newArrayList();
        			  String value = "";
        			  StructEntry tempEntry = structEntry;
        			  while(!isbeak){
        				  tempType.add(map.get(Type.JAVA).get(tempEntry.getType()));
        				  tempEntry = tempEntry.getValueType();
        				  if(!tempEntry.getType().isBaseType){
        					  if(tempEntry.getType() == StructType.SELF_DEFINE){
        						  value = tempEntry.getClassName();
        						  isbeak = true;
        					  }
        				  }else{
        					  value = map.get(Type.JAVA).get(tempEntry.getType());
        					  isbeak = true;
        				  }
        			  }
        			  //生成结果
        			  for(int i=0;i<tempType.size();i++){
        					  result =result + tempType.get(i)+"<";
        			  }
        			  if(tempType.get(tempType.size()-1).equals(StructType.MAP.productName)){
        				  value = "String,"+value;
        			  }
        			  result = result +value;
        			  for(int i=0;i<tempType.size();i++){
        				  result = result+">";
        			  }
        			  return result;
    			  }
    		 }
     }
}
