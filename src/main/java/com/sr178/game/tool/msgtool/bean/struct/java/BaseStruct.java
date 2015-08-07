package com.sr178.game.tool.msgtool.bean.struct.java;

import com.sr178.game.tool.msgtool.bean.StructEntry;
import com.sr178.game.tool.msgtool.bean.StructType;
import com.sr178.game.tool.msgtool.bean.struct.IStruct;
import com.sr178.game.tool.msgtool.bean.struct.TypeParseFactory;
import com.sr178.game.tool.msgtool.fileparse.FileEntry;
import com.sr178.game.tool.msgtool.fileparse.Replace;
import com.sr178.game.tool.msgtool.fileparse.StructParse;
import com.sr178.game.tool.msgtool.fileparse.Type;
import com.sr178.game.tool.msgtool.utils.FileUtils;

public class BaseStruct implements IStruct {
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
				stringBuffer.append("import "+packagePath+"."+structEntry.getClassName()+";");
				stringBuffer.append(FileUtils.LINE);
				return stringBuffer;
	    	}
			return null;
		}

		@Override
		public StringBuffer generatorgetAndSeter(StructEntry structEntry) {
			String strGet = StructParse.returnGetTempFile(Type.JAVA);
			StringBuffer stringBuffer = null;
			String pruductName = TypeParseFactory.getInstance().getJavaObjectName(structEntry);
			String resultGet = StructParse.replace(strGet, new Replace[]{Replace.bigParamName,Replace.paramName,Replace.typeName,Replace.note}, new String[]{structEntry.getBigParamName(),structEntry.getParamName(),pruductName,structEntry.getDesc()});
			stringBuffer = new StringBuffer(resultGet);
			String strSet = StructParse.returnSetTempFile(Type.JAVA);
			String resultSet = StructParse.replace(strSet,new Replace[]{Replace.bigParamName,Replace.paramName,Replace.typeName,Replace.note}, new String[]{structEntry.getBigParamName(),structEntry.getParamName(),pruductName,structEntry.getDesc()});
			stringBuffer.append(FileUtils.LINE);
			stringBuffer.append(resultSet);
			stringBuffer.append(FileUtils.LINE);
		    return stringBuffer;
		}

		@Override
		public StringBuffer generatorEncode(StructEntry structEntry) {
			String strEncode = StructParse.returnEncodeTempFile(Type.JAVA, structEntry.getType());
			return new StringBuffer(StructParse.replace(strEncode, new Replace[]{Replace.paramName}, new String[]{structEntry.getParamName()}));
		}
		@Override
		public StringBuffer generatorDecode(StructEntry structEntry) {
			String strDecode = StructParse.returnDecodeTempFile(Type.JAVA, structEntry.getType());
			return new StringBuffer(StructParse.replace(strDecode, new Replace[]{Replace.paramName,Replace.typeName}, new String[]{structEntry.getParamName(),structEntry.getClassName()}));
		}
		@Override
		public StringBuffer generatorOtherMethod(StructEntry structEntry) {
			return null;
		}
	    public static void main(String[] args) {
//	    	JavaBoolean javaBoolean = new JavaBoolean(new StructEntry(StructType.BOOLEAN,"testBoolean", "测试boolean值"));
//	    	System.out.println(javaBoolean.generatorDecode());
	    }
}
