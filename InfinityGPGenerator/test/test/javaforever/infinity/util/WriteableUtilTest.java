package test.javaforever.infinity.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.javaforever.infinity.core.Writeable;
import org.javaforever.infinity.domain.Statement;
import org.javaforever.infinity.domain.StatementList;
import org.javaforever.infinity.utils.WriteableUtil;
import org.junit.Test;

public class WriteableUtilTest extends TestCase{
	@Test
	public void testMerge(){
		Statement s = new Statement(200L,"select * from ");
		StatementList sc = new StatementList();
		sc.addStatement(new Statement(200L,"Update my cookie"));
		sc.addStatement(new Statement(100L,"delete my cookie"));
		sc.setSerial(100L);
		
		List<Writeable> wc = new ArrayList<Writeable>();
		wc.add(s);
		wc.add(sc);
		
		StatementList mysc = WriteableUtil.merge(wc);
		
		System.out.println(mysc.getContentWithSerial());
		System.out.println(s.getContentWithSerial());
	}
}
