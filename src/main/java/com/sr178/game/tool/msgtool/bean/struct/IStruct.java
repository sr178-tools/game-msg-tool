package com.sr178.game.tool.msgtool.bean.struct;

import com.sr178.game.tool.msgtool.bean.StructEntry;


public interface IStruct {
	
	public abstract StringBuffer generatorImport(StructEntry structEntry);
	
	public abstract StringBuffer generatorgetAndSeter(StructEntry structEntry);
	
	public abstract StringBuffer generatorEncode(StructEntry structEntry);
	
	public abstract StringBuffer generatorDecode(StructEntry structEntry);
	
	public abstract StringBuffer generatorOtherMethod(StructEntry structEntry);
}
