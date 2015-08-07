package com.sr178.game.tool.msgtool.bean.struct.java;

import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.bean.struct.TypeParseFactory;
import com.sr178.game.tool.msgtool.fileparse.FileEntry;
import com.sr178.game.tool.msgtool.fileparse.Replace;
import com.sr178.game.tool.msgtool.fileparse.StructParse;
import com.sr178.game.tool.msgtool.fileparse.Type;
import com.sr178.game.tool.msgtool.utils.FileUtils;
import com.sr178.game.tool.msgtool.utils.StructUtils;

public class JavaList extends BaseStruct {
    
    
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
					stringBuffer.append("import "+packagePath+"."+tempStructEntry.getClassName()+";");
					stringBuffer.append(FileUtils.LINE);
				}else{
					throw new RuntimeException("暂不支持list中再放list或map的结构");
				}
		}
		return stringBuffer;
	}
	@Override
	public StringBuffer generatorEncode(StructEntry structEntry) {
		
		
		String strEncode = StructParse.returnEncodeTempFile(Type.JAVA, structEntry.getType());
		StructEntry tempstructEntry = structEntry.getValueType();
		StringBuffer stringBuffer = new StringBuffer();
		String className = "";
		if(tempstructEntry.getType().isBaseType){
			tempstructEntry.setParamName(structEntry.getParamName()+".get("+structEntry.getParamName()+"i)");
			stringBuffer.append(TypeParseFactory.getInstance().getJavaStructParseTool(tempstructEntry.getType()).generatorEncode(tempstructEntry));
			className = TypeParseFactory.getInstance().getJavaObjectName(tempstructEntry);
		}else{
			if(tempstructEntry.getType()== StructType.SELF_DEFINE){
				stringBuffer.append(structEntry.getParamName()+".get("+structEntry.getParamName()+"i).encode(outputStream);");
				className = tempstructEntry.getClassName();
			}else{
				throw new RuntimeException("暂不支持list中再放list或map的结构");
			}
		}
		return new StringBuffer(StructParse.replace(strEncode, new Replace[]{Replace.paramName,Replace.valueListEncode,Replace.typeName}, new String[]{structEntry.getParamName(),stringBuffer.toString(),className}));
		
		
//		
//		String strEncode = StructParse.returnEncodeTempFile(Type.JAVA, structEntry.getType());
//		
//		StructEntry tempstructEntry = structEntry.getValueType();
//		StringBuffer stringBuffer = new StringBuffer();
//		if(tempstructEntry.getType().isBaseType){
//			tempstructEntry.setParamName(structEntry.getParamName()+".get("+structEntry.getParamName()+"i)");
//			stringBuffer.append(TypeParseFactory.getInstance().getJavaStructParseTool(structEntry.getType()).generatorEncode(structEntry));
//		}else{
//			if(tempstructEntry.getType()== StructType.SELF_DEFINE){
//				stringBuffer.append(structEntry.getParamName()+".get("+structEntry.getParamName()+"i).encode(outputStream)");
//			}else{
//				throw new RuntimeException("暂不支持list中再放list或map的结构");
//			}
//		}
//		return new StringBuffer(StructParse.replace(strEncode, new Replace[]{Replace.paramName,Replace.valueListEncode}, new String[]{structEntry.getParamName(),stringBuffer.toString()}));
//	
//	
	
	}

	@Override
	public StringBuffer generatorDecode(StructEntry structEntry) {
		
		
		String strDecode = StructParse.returnDecodeTempFile(Type.JAVA, structEntry.getType());
		StructEntry tempstructEntry = structEntry.getValueType();
		StringBuffer stringBuffer = new StringBuffer();
		String className = "";
		if(tempstructEntry.getType().isBaseType){
			String s = StructUtils.getJavaReadMethod(tempstructEntry.getType());
			String temp = structEntry.getParamName()+".add(inputStream."+s+"());";
			stringBuffer.append(temp);
			className = TypeParseFactory.getInstance().getJavaObjectName(tempstructEntry);
		}else{
			if(tempstructEntry.getType()== StructType.SELF_DEFINE){
				stringBuffer.append(tempstructEntry.getClassName()+" entry = new "+tempstructEntry.getClassName()+"();");
				stringBuffer.append("entry.decode(inputStream);");
				stringBuffer.append(structEntry.getParamName()+".add(entry);");
				className = tempstructEntry.getClassName();
			}else{
				throw new RuntimeException("暂不支持list中再放list或map的结构");
			}
		}
		return new StringBuffer(StructParse.replace(strDecode, new Replace[]{Replace.paramName,Replace.valueListDecode,Replace.typeName}, new String[]{structEntry.getParamName(),stringBuffer.toString(),className}));
		
		
		
//		String strDecode = StructParse.returnDecodeTempFile(Type.JAVA, structEntry.getType());
//		StructEntry tempstructEntry = structEntry.getValueType();
//		StringBuffer stringBuffer = new StringBuffer();
//		if(tempstructEntry.getType().isBaseType){
//			String s = StructUtils.getReadMethod(tempstructEntry.getType());
//			String temp = structEntry.getParamName()+".add(inputStream。"+s+"())";
//			stringBuffer.append(temp);
//		}else{
//			if(tempstructEntry.getType()== StructType.SELF_DEFINE){
//				stringBuffer.append(structEntry.getClassName()+" "+structEntry.getParamName()+"entry = new "+structEntry.getClassName()+"();");
//				stringBuffer.append(structEntry.getParamName()+".get("+structEntry.getParamName()+"i).decode(outputStream)");
//			}else{
//				throw new RuntimeException("暂不支持list中再放list或map的结构");
//			}
//		}
		
//		return new StringBuffer(StructParse.replace(strDecode, new Replace[]{Replace.paramName,Replace.valueListDecode}, new String[]{structEntry.getParamName(),stringBuffer.toString()}));
	}
	
	@Override
	public StringBuffer generatorOtherMethod(StructEntry structEntry) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String[] args) {
		StructEntry structEntry = new StructEntry(StructType.LIST, "listBean", new StructEntry(StructType.SELF_DEFINE,"test","Test","desc"), "ok");
//		System.out.println(new LuaList().generatorDecode(structEntry));
		System.out.println(new JavaList().generatorEncode(structEntry));
//		System.out.println(new LuaList().generatorImport(structEntry));
	}
	
}
