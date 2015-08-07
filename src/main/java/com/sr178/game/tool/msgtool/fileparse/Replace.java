package com.sr178.game.tool.msgtool.fileparse;

public enum Replace {
	moduleName("@moduleName@"),bigParamName("@bigParamName@"),paramName("@paramName@"),typeName("@typeName@"),note("@note@"),
	packages("@packages@"),imports("@imports@"),className("@className@"),param("@param@"),
	encode("@encode@"),decode("@decode@"),
	getterAndSetter("@getterAndSetter@"),otherMethod("@otherMethod@"),
	valueListDecode("@valueListDecode@"),valueMapDecode("@valueMapDecode@"),
	valueMapEncode("@valueMapEncode@"),valueListEncode("@valueListEncode@"),initValue("@initValue@"),actName("@actName@"),actNoteName("@actNoteName@");
	public String reFinal;
	private Replace(String reFinal){
		this.reFinal = reFinal;
	}
}
