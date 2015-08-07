package com.sr178.game.tool.msgtool;


import com.sr178.game.tool.msgtool.bean.EntryLine;
import com.sr178.game.tool.msgtool.bean.EntryMsgStruct;
import com.sr178.game.tool.msgtool.bean.MsgEntry;
import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.bean.struct.TypeParseFactory;
import com.sr178.game.tool.msgtool.fileparse.FileEntry;
import com.sr178.game.tool.msgtool.fileparse.Replace;
import com.sr178.game.tool.msgtool.fileparse.StructParse;
import com.sr178.game.tool.msgtool.fileparse.Type;
import com.sr178.game.tool.msgtool.utils.FileUtils;


public class CreateLua {
	public static void main(String[] args) {
		Config config = Config.init();
		String basePath =config.LUA_DERECTORY;
		FileUtils.hold.set(Type.LUA);
		//初始化元数据
		FileEntry.init();
		StringBuffer luaHeadFile = new StringBuffer();
		StringBuffer errorSourceFile = new StringBuffer();
		errorSourceFile.append("ErrorCode={}"+FileUtils.LINE);
        //生成文件
		for(MsgEntry msgEntry:FileEntry.msgEntrys){
//			String moduleName=msgEntry.getModule().getValue();
//			String basePackage = msgEntry.getPakage().getValue();
			String actName = msgEntry.getAction().getValue();
			for(EntryMsgStruct entryMsgStruct:msgEntry.getMsgStruct()){
				String className = entryMsgStruct.getClassName().getValue();
				String classPath = basePath+className+".lua";
				//获取模板
				String allStruct = StructParse.returnAllStructTempFile(Type.LUA);
				//引用生成
				StringBuffer importStr = new StringBuffer();
				StringBuffer getSetStr = new StringBuffer();
				StringBuffer decodeStr = new StringBuffer();
				StringBuffer encodeStr = new StringBuffer();
				StringBuffer paramStr = new StringBuffer();
				StringBuffer otherMethodStr = new StringBuffer();
//				Map<String,String> muiltimport = Maps.newHashMap(); 
				for(StructEntry structEntry:entryMsgStruct.getParams()){
//					StringBuffer importEntry = TypeParseFactory.getInstance().getLuaStructParseTool(structEntry.getType()).generatorImport(structEntry);
//					if(importEntry!=null&&!muiltimport.containsKey(importEntry.toString())){
//						importStr.append(importEntry);
//						muiltimport.put(importEntry.toString(), "");
//					}
					StringBuffer getSetEntry = TypeParseFactory.getInstance().getLuaStructParseTool(structEntry.getType()).generatorgetAndSeter(structEntry);
					if(getSetEntry!=null){
						getSetStr.append(StructParse.replace(getSetEntry.toString(), new Replace[]{Replace.className},new String[]{className}));
					}
					StringBuffer decodeEntry = TypeParseFactory.getInstance().getLuaStructParseTool(structEntry.getType()).generatorDecode(structEntry);
					if(decodeEntry!=null){
						decodeStr.append(decodeEntry);
					}
					StringBuffer encodeEntry = TypeParseFactory.getInstance().getLuaStructParseTool(structEntry.getType()).generatorEncode(structEntry);
					if(encodeEntry!=null){
						encodeStr.append(encodeEntry);
					}
					String paramEntry = CreateLua.getParamString(structEntry);
					if(paramEntry!=null){
						paramStr.append(paramEntry);
					}
				}
				Replace[] replaceArray = new Replace[]{Replace.imports,Replace.className,Replace.note,Replace.param,Replace.getterAndSetter,Replace.decode,Replace.encode,Replace.otherMethod,Replace.actName};
				String[] beArray = new String[]{importStr.toString(),className,msgEntry.getAction().getDesc(),paramStr.toString(),getSetStr.toString(),decodeStr.toString(),encodeStr.toString(),otherMethodStr.toString(),actName};
				//合并文件
				String all = StructParse.replace(allStruct,replaceArray,beArray);
				FileUtils.writeFile(all, classPath);
				luaHeadFile.append("require(\"webApi/"+className+"\")"+FileUtils.LINE);
			}
			//错误码
			if(msgEntry.getErroCodeList()!=null&&msgEntry.getErroCodeList().size()>0){
				for(EntryLine errorCode:msgEntry.getErroCodeList()){
					errorSourceFile.append("ErrorCode."+actName+"_"+errorCode.getValue()+"="+"\""+errorCode.getDesc()+"\""+FileUtils.LINE);
				}
			}
		}
		//写错误码文件
		FileUtils.writeFile(errorSourceFile.toString(), basePath+"ErrorCode.lua");
		luaHeadFile.append("require(\"webApi/ErrorCode.lua\")"+FileUtils.LINE);
		//写头文件
		FileUtils.writeFile(luaHeadFile.toString(), basePath+"ActFileHead.lua");
	}
	
	public static String getParamString(StructEntry structEntry){
		String paramStr = StructParse.returnParamTempFile(Type.LUA);
		structEntry = structEntry.coloneLuaBean();
		String initValue = "";
		if(structEntry.getType().isBaseType){
			if(structEntry.getType()==StructType.STRING){
				initValue = "\"\"";
			}else if(structEntry.getType()==StructType.BOOLEAN){
				initValue = "false";
			}else{
				initValue = "0";
			}
		}else if(structEntry.getType() == StructType.SELF_DEFINE){
			initValue = "nil";
		}else{
			initValue = "{}";
		}
		return StructParse.replace(paramStr, new Replace[]{Replace.paramName,Replace.note,Replace.initValue}, new String[]{structEntry.getParamName(),structEntry.getDesc(),initValue})+""+FileUtils.LINE;
	}

}
