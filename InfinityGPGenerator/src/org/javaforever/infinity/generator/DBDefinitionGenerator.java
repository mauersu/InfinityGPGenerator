package org.javaforever.infinity.generator;

import java.util.ArrayList;
import java.util.List;

import org.javaforever.infinity.domain.Domain;

public abstract class DBDefinitionGenerator {
	protected String dbName;
	protected List<Domain> domains = new ArrayList<Domain>();
	private List<String> contents = new ArrayList<String>();
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName2) {
		dbName = dbName2;
	}
	public List<Domain> getDomains() {
		return domains;
	}
	public void setDomains(List<Domain> domains2) {
		domains = domains2;
	}
	public void addDomain(Domain domain){
		domains.add(domain);
	}
	public List<String> getContents() {
		return contents;
	}
	public void setContents(List<String> contents2) {
		contents = contents2;
	}
	public abstract String generateDBSql(boolean createNew) throws Exception;
}
