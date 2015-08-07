package com.sr178.game.tool.msgtool.fileparse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.sr178.game.tool.msgtool.Config;
import com.sr178.game.tool.msgtool.bean.EntryLine;
import com.sr178.game.tool.msgtool.bean.EntryMsgStruct;
import com.sr178.game.tool.msgtool.bean.MsgEntry;
import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.bean.struct.doc.DirBean;
import com.sr178.game.tool.msgtool.utils.FileUtils;


public class FileEntry {
   public static final String MSG_START = "start";
   public static final String MSG_END = "end";
   public static final String ROOT_PACKAGE = "com.fantingame.game.msgbody.";
   public static List<MsgEntry> msgEntrys ;
   public static Map<String,String>  classToPackage = new HashMap<String,String>();
//   public static void main(String[] args) {
//	    String fileRootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//	    List<File> list = Lists.newArrayList();
//		FileUtils.readfile(fileRootPath,"msgentry",list);
//		getMsgEntrysFromFiles(list);
//   }
   
  public static void init(){
	    String fileRootPath = Config.init().msgEntryDerectory;//Thread.currentThread().getContextClassLoader().getResource("").getPath();
	    List<File> list = Lists.newArrayList();
		FileUtils.readfile(fileRootPath,"msgentry",list);
		msgEntrys = getMsgEntrysFromFiles(list);
        for(MsgEntry entry:msgEntrys){
        	String pakage = entry.getPakage().getValue();
        	int i=0;
        	for(EntryMsgStruct entryMsgStruct:entry.getMsgStruct()){
        		dealMsgEntryClassName(entry,entryMsgStruct,i);
        		if(!classToPackage.containsKey(entryMsgStruct.getClassName().getValue())){
        			classToPackage.put(entryMsgStruct.getClassName().getValue(), pakage);
        		}else{
        			throw new RuntimeException("存在相同的类名"+pakage+"."+entryMsgStruct.getClassName().getValue()+" and --"+classToPackage.get(entryMsgStruct.getClassName().getValue())+"."+entryMsgStruct.getClassName().getValue());
        		}
        		i++;
        	}
        }
  }
  
  public static void dealMsgEntryClassName(MsgEntry entry,EntryMsgStruct entryMsgStruct,int i){
	    if(entryMsgStruct.getClassName()==null){
	  	String classNameprefix="";
	  	if(entry.getMsgStruct().size()==2){
	  		if(i==0){
	  			classNameprefix = "Req";
	  		}else{
	  			classNameprefix = "Res";
	  		}
	  	}else if(entry.getMsgStruct().size()==1){
	  		classNameprefix="Notify";
	  	}else{
	  		throw new RuntimeException("不允许一个文件里定义2个以上的class，请遵循此编码规范");
	  	}
		String className = entry.getAction().getValue()+classNameprefix;
		EntryLine entryLine = new EntryLine("classname", className, entry.getAction().getDesc());
		entryMsgStruct.setClassName(entryLine);
	   }else{
		   entry.setAction(new EntryLine("action", entryMsgStruct.getClassName().getValue(), entryMsgStruct.getClassName().getDesc()));  
	   }
  }
  
  public static DirBean initDirBean(){
	    String fileRootPath = Config.init().msgEntryDerectory;
	    DirBean dirBean = new DirBean();
		FileUtils.readfileDirBean(fileRootPath,"msgentry",dirBean);
		return dirBean;
  }
  
  public static List<MsgEntry> getMsgEntrysFromFiles(List<File> files){
	  List<MsgEntry> list = Lists.newArrayList();
	  for(File file:files){
		  list.add(getMsgEntryFromFile(file));
	  }
	  return list;
  }
  public static MsgEntry getMsgEntryFromFile(File file){
	   if(file.isDirectory()){
		   throw new RuntimeException("FILE IS A DERECTORY!!!"+file.getName()+",path="+file.getAbsolutePath());
	   }
	   MsgEntry msgEntry = new MsgEntry();
	   List<String> list = null;
	   try {
		   list = Files.readLines(file, Charset.forName("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	   String packagePrefix = file.getParent();
	   packagePrefix = packagePrefix.replace(Config.init().msgEntryDerectory+"\\msgentry\\", "");
	   packagePrefix = packagePrefix.replace("\\", ".");
	   generatorHeadInfo(list,msgEntry,packagePrefix);
	   generatorMsgInfo(list,msgEntry);
	  return msgEntry;  
   } 
  
  
  private static void generatorMsgInfo(List<String> list,MsgEntry msgEntry){
	  boolean nextIsStartMsgStruct = false;
	  List<String> bodyStrList = null;
	  List<EntryMsgStruct> result = new ArrayList<EntryMsgStruct>();
	  for(String line:list){
		   if(Strings.isNullOrEmpty(line)){
			   continue;
		   }
		   line = line.replace(" ", "");
		   if(!line.equals(MSG_START)&&!line.equals(MSG_END)){
			   if(nextIsStartMsgStruct){
				   bodyStrList.add(line);
			   }
		   }else{
			   if(line.equals(MSG_START)){
				   nextIsStartMsgStruct = true;
				   bodyStrList = new ArrayList<String>();
			   }
			   if(line.equals(MSG_END)){
				   nextIsStartMsgStruct = false;
				   result.add(getEntryMsgStruct(bodyStrList));
				   bodyStrList = new ArrayList<String>();
			   }
		   }
	   }
	  msgEntry.setMsgStruct(result);
  }
  
  private static void generatorHeadInfo(List<String> list,MsgEntry msgEntry,String moduleName){
	   List<EntryLine> errorLine = Lists.newArrayList();
	   for(String line:list){
		   if(Strings.isNullOrEmpty(line)){
			   continue;
		   }
		   line = line.replace(" ", "");
		   if(line.startsWith("action")){
			   EntryLine actionLine =  getEntryLine(line);
			   msgEntry.setAction(actionLine);
		   }
		   if(line.startsWith("error")){
			   EntryLine errorLineEntry =  getEntryLine(line);
			   errorLine.add(errorLineEntry);
		   }
		}
	   msgEntry.setErroCodeList(errorLine);
	   msgEntry.setPakage(new EntryLine("package", ROOT_PACKAGE+moduleName, "包名"));
  }
  
  private static EntryLine getEntryLine(String line){
		  EntryLine entryLine = new EntryLine();
		  String[] strArray = line.split(":");
		  if(strArray.length<2){
			  throw new RuntimeException("头数据结构有错"+line);
		  }
		  if(strArray.length<3){
			  throw new RuntimeException("请写注释，thank you!!"+line);
		  }
		  entryLine.setKey(strArray[0]);
		  entryLine.setValue(strArray[1]);
		  entryLine.setDesc(strArray[2]);
      return entryLine;
  }
  
  private static EntryMsgStruct getEntryMsgStruct(List<String> lines){
	  EntryMsgStruct e = new EntryMsgStruct();
	  List<StructEntry> list = new ArrayList<StructEntry>();
	  for(String str:lines){
		  if(str.startsWith("classname")){
			  e.setClassName(getEntryLine(str));
		  }else{
		  list.add(getStructEntry(str));
		  }
	  }
	  e.setParams(list);
	  return e;
  }
  private static StructEntry getStructEntry(String line){
	  if(Strings.isNullOrEmpty(line)){
		  throw new RuntimeException("有空行");
	  }
	  StructEntry structEntry = new StructEntry();
	  String[] strArray = line.split(":");
	  if(strArray.length<2){
		  throw new RuntimeException("属性数据结构有错"+line);
	  }
	  if(strArray.length<3){
		  throw new RuntimeException("请写属性的注释，thank you!!"+line);
	  }
	  dealStructEntry(strArray[0],structEntry);
	  structEntry.setParamName(strArray[1]);
	  structEntry.setDesc(strArray[2]);
	  return structEntry;
  }
  
  private static void dealStructEntry(String str,StructEntry structEntry){
	  StructType nowType = null;
	  for(StructType st:StructType.values()){
		  if(str.toLowerCase().startsWith(st.textName)){
			  nowType = st;
			  break;
		  }
	  }
	  if(nowType==null){
		  nowType = StructType.SELF_DEFINE;
		  structEntry.setClassName(str);
	  }
	  structEntry.setType(nowType);
	  if(nowType==StructType.LIST||nowType==StructType.MAP){
		  StructEntry valueType = new StructEntry();
		  int a = str.indexOf("<");
		  int c = str.lastIndexOf(">");
		  String valueTypeStr = str.substring(a+1,c);
		  structEntry.setValueType(valueType);
		  dealStructEntry(valueTypeStr,valueType);
	  }
  }
  
  public static void main(String[] args) {
//	 String line = "List<List<Map<TTT>>>:bb:zhushi";
//	 StructEntry entry = FileEntry.getStructEntry(line);
//	 System.out.println("ok");
//	 System.out.println("ok");
  }
}
