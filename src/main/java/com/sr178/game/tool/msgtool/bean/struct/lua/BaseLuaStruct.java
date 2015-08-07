package com.sr178.game.tool.msgtool.bean.struct.lua;

import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.bean.struct.IStruct;
import com.sr178.game.tool.msgtool.fileparse.FileEntry;
import com.sr178.game.tool.msgtool.fileparse.Replace;
import com.sr178.game.tool.msgtool.fileparse.StructParse;
import com.sr178.game.tool.msgtool.fileparse.Type;
import com.sr178.game.tool.msgtool.utils.FileUtils;

public class BaseLuaStruct implements IStruct {
     public static final String IMPORT_LIST = "import java.util.List;"+FileUtils.LINE+"import java.util.ArrayList;"+FileUtils.LINE;
     public static final String IMPORT_MAP = "import java.util.Map;"+FileUtils.LINE+"import java.util.HashMap;"+FileUtils.LINE+"import java.util.Map.Entry;"+FileUtils.LINE;
	    @Override
		public StringBuffer generatorImport(StructEntry structEntry) {
	    	if(structEntry.getType()==StructType.SELF_DEFINE){
	    		StringBuffer stringBuffer = new StringBuffer();
				String packagePath = FileEntry.classToPackage.get(structEntry.getClassName());
				if(packagePath==null){
					throw new NullPointerException("找不到类"+structEntry.getClassName());
				}
				stringBuffer.append("require(\""+structEntry.getClassName()+"\")");
				stringBuffer.append(FileUtils.LINE);
				return stringBuffer;
	    	}
			return null;
		}

		@Override
		public StringBuffer generatorgetAndSeter(StructEntry structEntry) {
			structEntry = structEntry.coloneLuaBean();
			StringBuffer stringBuffer = new StringBuffer();
			String strSet = StructParse.returnSetTempFile(Type.LUA);
			String paramName = structEntry.getParamName();
			String resultSet = StructParse.replace(strSet,new Replace[]{Replace.bigParamName,Replace.paramName,Replace.note}, new String[]{structEntry.getBigParamName(),paramName,structEntry.getDesc()});
			stringBuffer.append(resultSet);
			stringBuffer.append(FileUtils.LINE);
		    return stringBuffer;
		}

		@Override
		public StringBuffer generatorEncode(StructEntry structEntry) {
				structEntry = structEntry.coloneLuaBean();
			String strEncode = StructParse.returnEncodeTempFile(Type.LUA, structEntry.getType());
			return new StringBuffer(StructParse.replace(strEncode, new Replace[]{Replace.paramName}, new String[]{structEntry.getParamName()}));
		}
		@Override
		public StringBuffer generatorDecode(StructEntry structEntry) {
//			structEntry = structEntry.coloneLuaBean();
			String strDecode;
			if(structEntry.getType()== StructType.SELF_DEFINE){
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("        local "+structEntry.getParamName()+"Temp = "+structEntry.getClassName()+":New()");
				stringBuffer.append(FileUtils.LINE);
				stringBuffer.append("        body."+structEntry.getParamName()+"="+structEntry.getParamName()+"Temp:decode(inputStream)");
				stringBuffer.append(FileUtils.LINE);
				strDecode = stringBuffer.toString();
			}else{
				 strDecode = StructParse.returnDecodeTempFile(Type.LUA, structEntry.getType());
			}
			return new StringBuffer(StructParse.replace(strDecode, new Replace[]{Replace.paramName}, new String[]{structEntry.getParamName()}));
		}
		@Override
		public StringBuffer generatorOtherMethod(StructEntry structEntry) {
			return null;
		}
	    public static void main(String[] args) {
	    	BaseLuaStruct baseLuaStruct = new BaseLuaStruct();
	    	
	    	System.out.println(baseLuaStruct.generatorDecode(new StructEntry(StructType.SELF_DEFINE,"testBoolean", "测试boolean值")));
	    }
}
