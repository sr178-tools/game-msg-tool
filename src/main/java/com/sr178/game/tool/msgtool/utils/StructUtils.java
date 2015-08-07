package com.sr178.game.tool.msgtool.utils;

import com.sr178.game.tool.msgtool.bean.StructType;

public class StructUtils {
    public static String getJavaReadMethod(StructType structType){
    	 String result = "";
    	 if(structType==StructType.INT){
    		 result = "readInt";
    	 }else if(structType==StructType.BOOLEAN){
    		 result = "readBoolean";
    	 }else if(structType==StructType.LONG){
    		 result = "readLong";
    	 }else if(structType==StructType.SHORT){
    		 result = "readShort";
    	 }else if(structType==StructType.BYTE){
    		 result = "readByte";
    	 }else if(structType==StructType.STRING){
    		 result = "readUTF";
    	 }
    	 else{
    		 throw new RuntimeException("不支持该类型取方法名"+structType);
    	 }
    	 return result;
    }
    public static String getLuaReadMethod(StructType structType){
   	 String result = "";
   	 if(structType==StructType.INT){
   		 result = "ReadInt";
   	 }else if(structType==StructType.BOOLEAN){
   		 result = "ReadBoolean";
   	 }else if(structType==StructType.LONG){
   		 result = "ReadLong";
   	 }else if(structType==StructType.SHORT){
   		 result = "ReadShort";
   	 }else if(structType==StructType.BYTE){
   		 result = "ReadBYTE";
   	 }else if(structType==StructType.STRING){
   		 result = "ReadUTFString";
   	 }
   	 else{
   		 throw new RuntimeException("不支持该类型取方法名"+structType);
   	 }
   	 return result;
   }
    
    public static String getLuaWriteMethod(StructType structType){
      	 String result = "";
      	 if(structType==StructType.INT){
      		 result = "WriteInt";
      	 }else if(structType==StructType.BOOLEAN){
      		 result = "WriteBoolean";
      	 }else if(structType==StructType.LONG){
      		 result = "WriteLong";
      	 }else if(structType==StructType.SHORT){
      		 result = "WriteShort";
      	 }else if(structType==StructType.BYTE){
      		 result = "WriteBYTE";
      	 }else if(structType==StructType.STRING){
      		 result = "WriteUTFString";
      	 }
      	 else{
      		 throw new RuntimeException("不支持该类型取方法名"+structType);
      	 }
      	 return result;
      }
}
