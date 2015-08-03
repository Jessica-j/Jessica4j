import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.junit.Test;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class queryTest {
	String aString;
	List<queryTest> aQueryTest = new ArrayList<queryTest>();

	public List<queryTest> getaQueryTest() {
		return aQueryTest;
	}

	public void setaQueryTest(List<queryTest> aQueryTest) {
		this.aQueryTest = aQueryTest;
	}

	public String getaString() {
		return aString;
	}

	public void setaString(String aString) {
		this.aString = aString;
	}

	public int getAint() {
		return aint;
	}

	public void setAint(int aint) {
		this.aint = aint;
	}

	int aint;

	@Test
	public void test() {

		List<queryTest> aQueryTest = new ArrayList<queryTest>();
		for (int i = 0; i < 100000; i++) {
			queryTest A = new queryTest() {
				{
					this.setAint(1);
					this.setaString("ssdsd");
				}
			};
			aQueryTest.add(A);
		}

		JexlEngine _jexlEngine = new org.apache.commons.jexl2.JexlEngine();
		final Expression _expression = _jexlEngine
				.createExpression("aQueryTest.aint > 0  &&  aQueryTest.aQueryTest == null");
		List<queryTest> Beans = new ArrayList<queryTest>();
		Predicate<queryTest> allCaps = new Predicate<queryTest>() {
			public boolean apply(queryTest objectBeans) {
				try {
					JexlContext context = new MapContext();
					context.set("aQueryTest", objectBeans);
					Boolean obj = (Boolean) _expression.evaluate(context);
					return obj;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		};
		Collection<queryTest> a = Collections2.filter(aQueryTest, allCaps);
		System.out.println(a.size());
	}
}
