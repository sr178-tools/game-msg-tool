package com.sr178.game.tool.msgtool.fileparse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.utils.FileUtils;

public class StructParse {
	public static Map<String,File> map = null;
	
	private static Map<String,File> getFileTempMap(String path){
		 if(map==null){
		 Map<String,File> structParse = new HashMap<String,File>();
		 String fileRootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		 List<File> list = Lists.newArrayList();
		 FileUtils.readfile(fileRootPath,path,list);
		 for(File file:list){
			 structParse.put(Files.getNameWithoutExtension(file.getName()), file);
		 }
		  map = structParse;
		 }
		 return map;
	}
	public static String returnGetTempFile(Type type){
		Map<String,File> map = getFileTempMap(type.path);
		return fileToString(map.get("getStruct"));
	}
	
	public static String returnSetTempFile(Type type){
		Map<String,File> map = getFileTempMap(type.path);
		return fileToString(map.get("setStruct"));
	}
	
	public static String returnParamTempFile(Type type){
		Map<String,File> map = getFileTempMap(type.path);
		return fileToString(map.get("paramStruct"));
	}
	
	public static String returnAllStructTempFile(Type type){
		Map<String,File> map = getFileTempMap(type.path);
		return fileToString(map.get("AllStruct"));
	}
	public static String returnDecodeTempFile(Type type,StructType stype){
		Map<String,File> map = getFileTempMap(type.path);
		return fileToString(map.get("DecodeStruct"+stype.textName));
	}
	public static String returnEncodeTempFile(Type type,StructType stype){
		Map<String,File> map = getFileTempMap(type.path);
		return fileToString(map.get("EncodeStruct"+stype.textName));
	}
	
	public static String fileToString(File file){
		try {
			return Files.toString(file, Charset.forName("utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> fileToLines(File file){
		List<String> list = null;
		try {
			list = Files.readLines(file, Charset.forName("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static String replace(String str,Replace[] replaces,String[] insteadStrs){
		for(int i=0;i<replaces.length;i++){
			str = str.replaceAll(replaces[i].reFinal, insteadStrs[i]);
		}
		return str;
	}
}
