package com.sr178.game.tool.msgtool;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sr178.game.tool.msgtool.bean.EntryMsgStruct;
import com.sr178.game.tool.msgtool.bean.MsgEntry;
import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.bean.struct.TypeParseFactory;
import com.sr178.game.tool.msgtool.bean.struct.java.BaseStruct;
import com.sr178.game.tool.msgtool.fileparse.FileEntry;
import com.sr178.game.tool.msgtool.fileparse.Replace;
import com.sr178.game.tool.msgtool.fileparse.StructParse;
import com.sr178.game.tool.msgtool.fileparse.Type;
import com.sr178.game.tool.msgtool.utils.FileUtils;


public class CreateJava {
	public static void main(String[] args) {
		Config config = Config.init();
		String basePath =config.JAVA_DERECTORY;
		FileUtils.hold.set(Type.JAVA);
		//初始化元数据
		FileEntry.init();
        //生成文件
		for(MsgEntry msgEntry:FileEntry.msgEntrys){
//			String moduleName=msgEntry.getModule().getValue();
			String basePackage = msgEntry.getPakage().getValue();
			String packagePath = basePath+basePackage.replace(".", "\\");
			for(EntryMsgStruct entryMsgStruct:msgEntry.getMsgStruct()){
				String className = entryMsgStruct.getClassName().getValue();
				String classPath = packagePath+"\\"+className+".java";
				//获取模板
				String allStruct = StructParse.returnAllStructTempFile(Type.JAVA);
				//引用生成
				StringBuffer importStr = new StringBuffer();
				StringBuffer getSetStr = new StringBuffer();
				StringBuffer decodeStr = new StringBuffer();
				StringBuffer encodeStr = new StringBuffer();
				StringBuffer paramStr = new StringBuffer();
				StringBuffer otherMethodStr = new StringBuffer();
				Map<String,String> muiltimport = Maps.newHashMap(); 
				for(StructEntry structEntry:entryMsgStruct.getParams()){
					StringBuffer importEntry = TypeParseFactory.getInstance().getJavaStructParseTool(structEntry.getType()).generatorImport(structEntry);
					if(importEntry!=null&&!muiltimport.containsKey(importEntry.toString())){
						importStr.append(importEntry);
						muiltimport.put(importEntry.toString(), "");
					}
					if(structEntry.getType()==StructType.LIST&&!muiltimport.containsKey(BaseStruct.IMPORT_LIST)){
						importStr.append(BaseStruct.IMPORT_LIST);
						muiltimport.put(BaseStruct.IMPORT_LIST, "");
					}
					if(structEntry.getType()==StructType.MAP&&!muiltimport.containsKey(BaseStruct.IMPORT_MAP)){
						importStr.append(BaseStruct.IMPORT_MAP);
						muiltimport.put(BaseStruct.IMPORT_MAP, "");
					}
					StringBuffer getSetEntry = TypeParseFactory.getInstance().getJavaStructParseTool(structEntry.getType()).generatorgetAndSeter(structEntry);
					if(getSetEntry!=null){
						getSetStr.append(getSetEntry);
					}
					StringBuffer decodeEntry = TypeParseFactory.getInstance().getJavaStructParseTool(structEntry.getType()).generatorDecode(structEntry);
					if(decodeEntry!=null){
						decodeStr.append(decodeEntry);
					}
					StringBuffer encodeEntry = TypeParseFactory.getInstance().getJavaStructParseTool(structEntry.getType()).generatorEncode(structEntry);
					if(encodeEntry!=null){
						encodeStr.append(encodeEntry);
					}
					String paramEntry = CreateJava.getParamString(structEntry);
					if(paramEntry!=null){
						paramStr.append(paramEntry);
					}
				}
				Replace[] replaceArray = new Replace[]{Replace.imports,Replace.packages,Replace.className,Replace.note,Replace.param,Replace.getterAndSetter,Replace.decode,Replace.encode,Replace.otherMethod};
				String[] beArray = new String[]{importStr.toString(),basePackage,className,msgEntry.getAction().getDesc(),paramStr.toString(),getSetStr.toString(),decodeStr.toString(),encodeStr.toString(),otherMethodStr.toString()};
				//合并文件
				String all = StructParse.replace(allStruct,replaceArray,beArray);
				FileUtils.writeFile(all, classPath);
			}
		}
	}
	
	public static String getParamString(StructEntry structEntry){
		String paramStr = StructParse.returnParamTempFile(Type.JAVA);
		String initValue = "";
		if(structEntry.getType().isBaseType){
			if(structEntry.getType()==StructType.STRING){
				initValue = "\"\"";
			}else if(structEntry.getType()==StructType.LONG){
				initValue = "0l";
			}else if(structEntry.getType()==StructType.BOOLEAN){
				initValue = "false";
			}else{
				initValue = "0";
			}
		}else{
			initValue = "null";
		}
		return StructParse.replace(paramStr, new Replace[]{Replace.paramName,Replace.note,Replace.typeName,Replace.initValue}, new String[]{structEntry.getParamName(),structEntry.getDesc(),TypeParseFactory.getInstance().getJavaObjectName(structEntry),initValue})+""+FileUtils.LINE;
	}

}
