package org.javaforever.infinity.generator;

import org.javaforever.infinity.domain.Domain;
import org.javaforever.infinity.domain.Type;
import org.javaforever.infinity.domain.Var;

public class NamedTokenGenerator {
	public static String getStringtoNumberToken(Type type){
		if (type.getTypeName().equals("int")) return "Integer.parseInt(";
		else if (type.getTypeName().equals("long")) return "Long.parseLong(";
		else if (type.getTypeName().equals("double")) return "Double.parseDouble(";
		else if (type.getTypeName().equals("float")) return "Float.parseFloat(";
		else if (type.getTypeName().equals("boolean")) return "Boolean.parseBoolean(";
		else return "";
	}
	
	public static String getDaomainList(Domain domain, Var list){
		String result = "List<"+domain.getStandardName()+"> "+list.getVarName();
		return result;
	}
}
