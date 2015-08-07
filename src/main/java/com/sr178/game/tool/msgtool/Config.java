package com.sr178.game.tool.msgtool;


import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

public class Config {
   private static Config config;
   public  String JAVA_DERECTORY ="";
   public  String LUA_DERECTORY = "";
   public  String msgEntryDerectory = "";
   public  String DOC_DERECTORY ="";
   private Config(String java,String lua,String msgEntry,String doc){
	   JAVA_DERECTORY = java;
	   LUA_DERECTORY = lua;
	   msgEntryDerectory = msgEntry;
	   DOC_DERECTORY = doc;
   }
   public static Config init(){
	   if(config==null){
		   Config.class.getClassLoader();
		   ResourceBundle rs = ResourceBundle.getBundle("config");
		   String java = rs.getString("java_derectory");
		   String lua = rs.getString("lua_derectory");
		   String msgentry = rs.getString("msgentry_derectory");
		   String doc = rs.getString("doc_derectory");
		   
		   try {
			   java =new String(java.getBytes("iso8859-1"),"utf-8");
			   lua = new String(lua.getBytes("iso8859-1"),"utf-8");
			   msgentry = new String(msgentry.getBytes("iso8859-1"),"utf-8");
			   doc = new String(doc.getBytes("iso8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		   config = new Config(java,lua,msgentry,doc);
		   System.out.println("加载配置文件信息");
		   System.out.println("java文件输出目录:"+java);
		   System.out.println("lua文件输出目录:"+lua);
		   System.out.println("消息定义元数据文件目录:"+msgentry);
		   System.out.println("文档目录:"+doc);
	   }
	   return config;
   } 
}
