package org.javaforever.infinity.springjpa.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javaforever.infinity.core.Verb;
import org.javaforever.infinity.domain.Controller;
import org.javaforever.infinity.domain.Domain;
import org.javaforever.infinity.domain.Field;
import org.javaforever.infinity.domain.Method;
import org.javaforever.infinity.domain.Type;
import org.javaforever.infinity.domain.ValidateInfo;
import org.javaforever.infinity.domain.Var;
import org.javaforever.infinity.exception.ValidateException;
import org.javaforever.infinity.utils.InterVarUtil;
import org.javaforever.infinity.utils.StringUtil;

public class SpringMVCController extends Controller{
	protected List<Verb> verbs = new ArrayList<Verb>();
	public List<Verb> getVerbs() {
		return verbs;
	}

	public void setVerbs(List<Verb> verbs) {
		this.verbs = verbs;
	}
	
	public void addVerb(Verb verb){
		this.verbs.add(verb);
	}

	public SpringMVCController(Domain domain) throws ValidateException{
		super();
		fixingDecorate(domain);
	}
	
	public void fixingDecorate(Domain domain){
		Field serviceField = new Field("service",domain.getStandardName()+"Service");
		serviceField.addAnnotation("Autowired");
		this.addField(serviceField);
		this.addField(new Field(domain.getLowerFirstDomainName(),domain.getType().getTypeName()));
		this.addField(new Field("pagesize","int"));
		this.addField(new Field("pagenum","int"));
		this.addField(new Field(domain.getLowerFirstDomainName()+"Request", "Map<String,Object>","java.util"));
		this.addClassImports(domain.getPackageToken()+".service."+domain.getStandardName()+"Service");
		this.addClassImports(domain.getPackageToken()+".domain."+domain.getStandardName());
		this.addClassImports("org.apache.struts2.interceptor.RequestAware");
		this.addClassImports("org.springframework.beans.factory.annotation.Autowired");
		this.addClassImports("java.util.List");
		this.addClassImports("java.util.Map");
		this.addClassImports("com.opensymphony.xwork2.ActionSupport");
		this.addClassImports("com.opensymphony.xwork2.Preparable");
//		this.addMethod(S2SHNamedUtilMethodGenerator.generateStruts2PrepareMethod(1));
		Var domainRequest = new Var(domain.getLowerFirstDomainName()+"Request",new Type("Map<String,Object>","java.util"));		
//		this.addMethod(S2SHNamedUtilMethodGenerator.generateStruts2SetRequestMethod(1,InterVarUtil.Servlet.request, domainRequest));
	}

	public String generateControllerString() throws ValidateException{
		StringBuilder sb = new StringBuilder();
		if (this.packageToken !=null && !"".equalsIgnoreCase(this.packageToken)) sb.append("package ").append(this.packageToken).append(".").append("action").append(";\n\n");
		Set<String> imports = this.generateImportStrings();
		imports.addAll(this.classImports);
		for (String s: imports){
			sb.append("import ").append(s).append(";\n");
		}
		sb.append("\n");
	
		sb.append("public class ").append(this.getStandardName()).append(" extends ActionSupport implements RequestAware,Preparable{\n");		
		
		//generate fields notions
		for (Field f : this.getFields()) {
				for (String annotation:f.getAnnotations()){
					sb.append("\t@").append(annotation).append("\n");
				}
		        sb.append("\tprotected ").append(f.getFieldType()).append(" ").append(f.getLowerFirstFieldName()).append(";\n");
	    }
		  //generate getter setter notions

		for (Field f : this.getFields()) {	
		        // generate setters
		        sb.append("\tpublic void set").append(StringUtil.capFirst(f.getCapFirstFieldName())).append("(").append(f.getFieldType()).append(" ").append(StringUtil.lowerFirst(f.getLowerFirstFieldName())).append("){\n");
		        sb.append("\t\tthis.").append(StringUtil.lowerFirst(f.getLowerFirstFieldName())).append(" = ").append(StringUtil.lowerFirst(f.getLowerFirstFieldName())).append(";\n");
		        sb.append("\t}\n\n");
		        // generate getters
		        sb.append("\tpublic ").append(f.getFieldType()).append(" get").append(StringUtil.capFirst(f.getLowerFirstFieldName())).append("(){\n");
		        sb.append("\t\treturn this.").append(StringUtil.lowerFirst(f.getLowerFirstFieldName())).append(";\n");
		        sb.append("\t}\n\n");
	        }
		  
		  Iterator it2 = this.getMethods().iterator();
		  while (it2.hasNext()){
			  sb.append(((Method)it2.next()).generateMethodString());
		  }
		  sb.append("}\n");
		return sb.toString();
	}
	
	public ValidateInfo validate(){
		ValidateInfo info = new ValidateInfo();
		info.setSuccess(true);
		if (this.verbs == null || this.verbs.size() == 0){
			info.setSuccess(false);
			info.addCompileError("Action "+this.standardName+ " verb is null.");
		}
		for (Verb v:this.verbs){
			if (v != null && v.getDomain() == null){
				info.setSuccess(false);
				info.addCompileError("Action "+this.standardName+ " verb "+v.getVerbName()+"domain is null.");
			}
		}
		return info;			
	}
	
	public SpringMVCController(List<Verb>  verbs, Domain domain) throws Exception{
		super();			
		for (Verb v:verbs){
			v.setDomain(domain);
		}
		this.verbs = verbs;
		this.standardName = domain.getCapFirstDomainName() + "Action";
		
		ValidateInfo info = this.validate();
		if (info.success()){
			for (Verb v:this.verbs){
				Method verbMethods = v.generateControllerMethod();
				this.addMethod(verbMethods);
			}
			fixingDecorate(domain);
		}else {
			ValidateException e = new ValidateException(info);
			throw e;
		}
	}	

	public String getCapFirstActionName(){
		return StringUtil.capFirst(this.getStandardName());
	}
	
	public String getLowerFirstActionName(){
		return StringUtil.lowerFirst(this.getStandardName());
	}
	
	public Domain getDomain(){
		if (this.verbs!=null && this.verbs.size()>0) return this.verbs.get(0).getDomain();
		else return null;
	}
	
	public Method findVerbMethodByName(String methodname) throws Exception{
		for (Verb vb : this.verbs){
			Method vbm = vb.generateControllerMethod();
			if (vbm.getStandardName().equalsIgnoreCase(methodname)) return vbm;
		}
		return null;
	}

}
