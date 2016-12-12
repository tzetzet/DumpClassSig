package tzetzet.tool.dumpclasssig;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassTreePrinterTest {
    public ClassTreePrinterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPrinter1() {
        ClassTreePrinter classTreePrinter = new ClassTreePrinter();
        classTreePrinter.addClass("def/ghi/jk/BClass1");
        classTreePrinter.addClass("abc/def/Class1");
        classTreePrinter.addClass("Root");
        classTreePrinter.addClass("abc/def/Class2");
        classTreePrinter.addClass("def/ghi/jk/AClass2");
        classTreePrinter.addClass("def/ghi/jkAClass1");
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        classTreePrinter.print(writer);

        String expected =
            "  |-+/\r\n" +
            "     |--Root\r\n" +
            "     |-+abc\r\n" +
            "        |-+def\r\n" +
            "           |--Class1\r\n" +
            "           |--Class2\r\n" +
            "     |-+def\r\n" +
            "        |-+ghi\r\n" +
            "           |-+jk\r\n" +
            "              |--AClass2\r\n" +
            "              |--BClass1\r\n" +
            "           |--jkAClass1\r\n";

        String actual = sw.getBuffer().toString();
        assertEquals(expected.length(), actual.length());
        assertEquals(expected, actual);
    }
}
