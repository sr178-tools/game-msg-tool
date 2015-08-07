package com.sr178.game.tool.msgtool;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sr178.game.tool.msgtool.bean.EntryLine;
import com.sr178.game.tool.msgtool.bean.EntryMsgStruct;
import com.sr178.game.tool.msgtool.bean.MsgEntry;
import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.bean.struct.doc.DirBean;
import com.sr178.game.tool.msgtool.fileparse.FileEntry;
import com.sr178.game.tool.msgtool.fileparse.Type;
import com.sr178.game.tool.msgtool.utils.FileUtils;

public class CreateDoc {
	public static final String BR = "<br />";
	public static void main(String[] args) {
		Config config = Config.init();
		String basePath =config.DOC_DERECTORY;
		FileUtils.hold.set(Type.DOC);
		FileEntry.init();
		//初始化元数据
		DirBean dirBean = FileEntry.initDirBean();
        Map<String,Map<String,List<MsgEntry>>> moduleMap = Maps.newHashMap();
        dealDirBean(dirBean,moduleMap);
        //生成主索引文件
        StringBuffer oneStringBuffer = new StringBuffer();
        oneStringBuffer.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"+FileUtils.LINE);
        Map<String,StringBuffer> twomap = Maps.newHashMap();
        for(String bigModule:moduleMap.keySet()){
        	String bigModuleChinanese = "未定义";
        	if(bigModule.equals("client")){
        		bigModuleChinanese = "用户接口";
        	}else if(bigModule.equals("notify")){
        		bigModuleChinanese = "推送接口";
        	}else if(bigModule.equals("server")){
        		bigModuleChinanese = "服务器间接口";
        	}else if(bigModule.equals("test")){
        		bigModuleChinanese = "测试接口";
        	}
        	String bigModuleUrl = bigModule;
        	oneStringBuffer.append("<li><a href='"+bigModuleUrl+"/index.html'>"+bigModuleChinanese+"</a></li>");
        	StringBuffer twoStringBuffer = new StringBuffer();
        	twoStringBuffer.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"+FileUtils.LINE);
        	twoStringBuffer.append("<p align='center'><a href='../index.html'>返回首页</a> </p>"+FileUtils.LINE);
        	for(Entry<String,List<MsgEntry>> entry:moduleMap.get(bigModule).entrySet()){
        		twoStringBuffer.append("<li><B>"+entry.getKey()+"</B></li>");
        		for(MsgEntry msgEntry:entry.getValue()){
        			String actName = msgEntry.getAction().getValue();
        			String classPath = msgEntry.getPakage().getValue().replace(FileEntry.ROOT_PACKAGE, "").split("\\.")[1]+"\\"+actName+".html";
        			twoStringBuffer.append("--<a href='"+classPath+"'>"+msgEntry.getAction().getDesc()+"</a>"+BR);
        		}
        		twomap.put(bigModule, twoStringBuffer);
        		FileUtils.writeFile(twoStringBuffer.toString(), basePath+"\\"+bigModule+"\\index.html");
        	}
        }
        FileUtils.writeFile(oneStringBuffer.toString(), basePath+"\\"+"index.html");
	}
	
	private static void dealDirBean(DirBean dirBean,Map<String,Map<String,List<MsgEntry>>> moduleMap){
		if(!dirBean.isDirectory()){
			MsgEntry msgEntry = FileEntry.getMsgEntryFromFile(dirBean.getFile());
			boolean isSkipTree = false;
			if(msgEntry.getAction()==null){
				msgEntry.setAction(new EntryLine("action", (msgEntry.getMsgStruct()).get(0).getClassName().getValue(), msgEntry.getMsgStruct().get(0).getClassName().getDesc()));  
				isSkipTree = true;
			}
			String basePackage = msgEntry.getPakage().getValue();
			String actName = msgEntry.getAction().getValue();
			String packagePath = Config.init().DOC_DERECTORY+basePackage.replace(FileEntry.ROOT_PACKAGE, "").replace(".", "\\");
			String classPath = packagePath+"\\"+actName+".html";
			String content = generatorDoc(msgEntry);
			if(!isSkipTree){
				String[] pathArray = basePackage.split("\\.");
				String oneModuleName = pathArray[pathArray.length-2];
				String twoModuleName = pathArray[pathArray.length-1];
				if(moduleMap.containsKey(oneModuleName)){
					Map<String,List<MsgEntry>> oneMapTemp = moduleMap.get(oneModuleName);
					if(oneMapTemp.containsKey(twoModuleName)){
						oneMapTemp.get(twoModuleName).add(msgEntry);
					}else{
						List<MsgEntry> list = Lists.newArrayList();
						list.add(msgEntry);
						oneMapTemp.put(twoModuleName, list);
					}
				}else{
					Map<String,List<MsgEntry>> oneMapTemp = Maps.newHashMap();
					List<MsgEntry> list = Lists.newArrayList();
					list.add(msgEntry);
					oneMapTemp.put(twoModuleName, list);
					moduleMap.put(oneModuleName, oneMapTemp);
				}
			}
			FileUtils.writeFile(content, classPath);
		}else{
			for(DirBean dirBeanTemp:dirBean.getList()){
				dealDirBean(dirBeanTemp,moduleMap);
			}
		}
	}
	
	private static String generatorDoc(MsgEntry msgEntry){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"+FileUtils.LINE);
		stringBuffer.append("<p align='center'><a href='../../index.html'>返回首页</a> </p>"+FileUtils.LINE);
		stringBuffer.append("<li>"+msgEntry.getAction().getDesc()+"("+msgEntry.getAction().getValue()+")</li>");
		stringBuffer.append("-----------------------"+BR+BR);
		List<EntryMsgStruct> list = msgEntry.getMsgStruct();
		if(list.size()==1){
			stringBuffer.append("<li>消息体结构</li>");
		}else if(list.size()==2){
			stringBuffer.append("<li>请求消息体结构</li>");
		}
		int i = 0 ;
		for(EntryMsgStruct entryMsgStruct:list){
			FileEntry.dealMsgEntryClassName(msgEntry, entryMsgStruct, i);
			stringBuffer.append(getStruct(entryMsgStruct,i));
			i++;
		}
		//错误码结构
		if(msgEntry.getErroCodeList()!=null&&msgEntry.getErroCodeList().size()>0){
			stringBuffer.append("<li>错误码</li>");
			stringBuffer.append("<table border='1' bordercolor='#000000'>");
			stringBuffer.append("<tr colspan='2'>");
			stringBuffer.append("<td>错误码</td><td>描述</td></tr>");
			for(EntryLine errorLine:msgEntry.getErroCodeList()){
				stringBuffer.append("<tr><td>"+errorLine.getValue()+"</td><td>"+errorLine.getDesc()+"</td></tr>");
			}
			stringBuffer.append("</table>");
		}
		return stringBuffer.toString();
	}
	
	
	private static String getStruct(EntryMsgStruct entryMsgStruct,int i){
		StringBuffer stringBuffer = new StringBuffer();
		if(i==1){
			stringBuffer.append(BR+"<li>响应消息体结构</li>");
		}
		stringBuffer.append("--"+entryMsgStruct.getClassName().getValue()+BR);
		if(entryMsgStruct.getParams().size()>0){
			stringBuffer.append("<table border='1' bordercolor='#000000'>");
			stringBuffer.append("<tr colspan='3'>");
			stringBuffer.append("<td>变量类型</td><td>变量名</td><td>说明</td></tr>");
			for(StructEntry structEntry:entryMsgStruct.getParams()){
				String typeName = "";
				if(structEntry.getType()==StructType.SELF_DEFINE){
					String parentPath = FileEntry.classToPackage.get(entryMsgStruct.getClassName().getValue());
					String childPath = FileEntry.classToPackage.get(structEntry.getClassName());
					String classPath = getUrl(parentPath,childPath);
					//packagePath.replace(FileEntry.ROOT_PACKAGE, "").split("\\.")[1]
					classPath = classPath+structEntry.getClassName()+".html";
					typeName = "<a href='"+classPath+"'>"+structEntry.getClassName()+"</a>";
				}else if((structEntry.getType()==StructType.LIST||structEntry.getType()==StructType.MAP)&&structEntry.getValueType().getType()==StructType.SELF_DEFINE){
					String parentPath = FileEntry.classToPackage.get(entryMsgStruct.getClassName().getValue());
					String childPath = FileEntry.classToPackage.get(structEntry.getValueType().getClassName());
					String classPath = getUrl(parentPath,childPath);
        			classPath = classPath+structEntry.getValueType().getClassName()+".html";
					typeName =structEntry.getType().textName+"["+ "<a href='"+classPath+"'>"+structEntry.getValueType().getClassName()+"</a>"+"]";
				}else{
					typeName = structEntry.getType().textName;
				}
				stringBuffer.append("<tr><td>"+typeName+"</td><td>"+structEntry.getParamName()+"</td><td>"+structEntry.getDesc()+"</td></tr>");
			}
			stringBuffer.append("</table>");
		}else{
			stringBuffer.append("无，这是一个空消息体"+BR);
		}
		return stringBuffer.toString();
	}
	
	private static String getUrl(String parentPath,String childPath){
		String path = "";
		if(parentPath.equals(childPath)){
			return path;
		}
		String[] parentRootArray = parentPath.split("\\.");
		String parentRoot = parentRootArray[parentRootArray.length-2];
		String parentModule = parentRootArray[parentRootArray.length-1];
		String[] chRootArray = childPath.split("\\.");
		String chRoot = chRootArray[chRootArray.length-2];
		String chModule = chRootArray[chRootArray.length-1];
		if(parentRoot.equals(chRoot)){
			if(!parentModule.equals(chModule)){
				path = "..\\"+chModule+"\\";
			}
		}else{
			path = "..\\..\\"+chRoot+"\\"+chModule+"\\";
		}
		return path;
	}
	
}
