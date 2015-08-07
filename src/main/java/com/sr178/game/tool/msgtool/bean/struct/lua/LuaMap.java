package com.sr178.game.tool.msgtool.bean.struct.lua;

import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.fileparse.FileEntry;
import com.sr178.game.tool.msgtool.fileparse.Replace;
import com.sr178.game.tool.msgtool.fileparse.StructParse;
import com.sr178.game.tool.msgtool.fileparse.Type;
import com.sr178.game.tool.msgtool.utils.FileUtils;
import com.sr178.game.tool.msgtool.utils.StructUtils;

public class LuaMap extends BaseLuaStruct {

	@Override
	public StringBuffer generatorImport(StructEntry structEntry) {
		//只处理
		StringBuffer stringBuffer = new StringBuffer();
		//如果容器内是基本数据类型 则不用导入其他
		if(!structEntry.getValueType().getType().isBaseType){
			    StructEntry tempStructEntry = structEntry.getValueType();
				//如果是map 导入固定类型
				if(tempStructEntry.getType()==StructType.SELF_DEFINE){
					String packagePath = FileEntry.classToPackage.get(tempStructEntry.getClassName());
					if(packagePath==null){
						throw new NullPointerException("找不到类"+tempStructEntry.getClassName());
					}
					stringBuffer.append("require(\""+tempStructEntry.getClassName()+"\")");
					stringBuffer.append(FileUtils.LINE);
				}else{
					throw new RuntimeException("暂不支持list中再放list或map的结构");
				}
		}
		return stringBuffer;
	}

	@Override
	public StringBuffer generatorEncode(StructEntry structEntry) {
		String strEncode = StructParse.returnEncodeTempFile(Type.LUA, structEntry.getType());
		structEntry = structEntry.coloneLuaBean();
		StructEntry tempstructEntry = structEntry.getValueType();
		StringBuffer stringBuffer = new StringBuffer();
		if(tempstructEntry.getType().isBaseType){
			String s = StructUtils.getLuaWriteMethod(tempstructEntry.getType());
			stringBuffer.append("outputStream:"+s+"(v)");
			stringBuffer.append(FileUtils.LINE);
		}else{
			if(tempstructEntry.getType()== StructType.SELF_DEFINE){
				stringBuffer.append("v:encode(outputStream)");
			}else{
				throw new RuntimeException("暂不支持list中再放list或map的结构");
			}
		}
		return new StringBuffer(StructParse.replace(strEncode, new Replace[]{Replace.paramName,Replace.valueMapEncode}, new String[]{structEntry.getParamName(),stringBuffer.toString()}));
	}

	@Override
	public StringBuffer generatorDecode(StructEntry structEntry) {
		String strDecode = StructParse.returnDecodeTempFile(Type.LUA, structEntry.getType());
//		structEntry = structEntry.coloneLuaBean();
		StructEntry tempstructEntry = structEntry.getValueType();
		StringBuffer stringBuffer = new StringBuffer();
		if(tempstructEntry.getType().isBaseType){
			String s = StructUtils.getLuaReadMethod(tempstructEntry.getType());
			String temp = structEntry.getParamName()+"Temp[key] = (inputStream:"+s+"())";
			stringBuffer.append(temp);
		}else{
			if(tempstructEntry.getType()== StructType.SELF_DEFINE){
				stringBuffer.append("local entry = "+tempstructEntry.getClassName()+":New()");
				stringBuffer.append(FileUtils.LINE);
				stringBuffer.append("		  local by = entry:decode(inputStream)");
				stringBuffer.append(FileUtils.LINE);
				stringBuffer.append("		  "+structEntry.getParamName()+"Temp"+"[key] = by");
			}else{
				throw new RuntimeException("暂不支持Map中再放list或map的结构");
			}
		}
		return new StringBuffer(StructParse.replace(strDecode, new Replace[]{Replace.paramName,Replace.valueMapDecode}, new String[]{structEntry.getParamName(),stringBuffer.toString()}));
	}

	@Override
	public StringBuffer generatorOtherMethod(StructEntry structEntry) {
		return null;
	}
	
	public static void main(String[] args) {
		StructEntry structEntry = new StructEntry(StructType.MAP, "mapBean", new StructEntry(StructType.LONG,"test","Test","desc"), "ok");
//		System.out.println(new LuaMap().generatorDecode(structEntry));
		System.out.println(new LuaMap().generatorEncode(structEntry));
//		System.out.println(new LuaMap().generatorImport(structEntry));
	}

}
