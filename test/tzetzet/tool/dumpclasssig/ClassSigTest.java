package tzetzet.tool.dumpclasssig;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassSigTest {
    public ClassSigTest() {
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
    public void testDataSample1() {
        String classname = "DataSample1";
        String filename = classname + ".class";
        InputStream is = getClass().getResourceAsStream(filename);
        ClassSig classSig = new ClassSig(is, filename);

        // 入力ストリームが閉じられていることを確認
        try {
            is.read();
            fail();
        } catch (IOException e) {
            Logger.getLogger(ClassSigTest.class.getName()).log(Level.FINE, null, e);
        }

        assertEquals(getClass().getPackage().getName() + "." + classname, classSig.getThisClassName());
        assertTrue(classSig.isPublicOrProtected());

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        classSig.printSig(writer);

        String expected =
            "public class tzetzet.tool.dumpclasssig.DataSample1\r\n" +
            "  extends java.lang.Object\r\n" +
            "{\r\n" +
            "  public void <init>();\r\n" +
            "  public double publicMethod3(float, long);\r\n" +
            "  public int publicMethod1();\r\n" +
            "  public void publicMethod2(int);\r\n" +
            "  protected void protedtedMetho1();\r\n" +
            "}\r\n";
        String actual = sw.getBuffer().toString();
        assertEquals(expected.length(), actual.length());
        assertEquals(expected, actual);
    }

    @Test
    public void testDataSample2() {
        String classname = "DataSample2";
        String filename = classname + ".class";
        InputStream is = getClass().getResourceAsStream(filename);
        ClassSig classSig = new ClassSig(is, filename);

        // 入力ストリームが閉じられていることを確認
        try {
            is.read();
            fail();
        } catch (IOException e) {
            Logger.getLogger(ClassSigTest.class.getName()).log(Level.FINE, null, e);
        }

        assertEquals(getClass().getPackage().getName() + "." + classname, classSig.getThisClassName());
        assertTrue(classSig.isPublicOrProtected());

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        classSig.printSig(writer);

        String expected =
            "public class tzetzet.tool.dumpclasssig.DataSample2\r\n" +
            "  extends java.lang.Object\r\n" +
            "{\r\n" +
            "  public void <init>();\r\n" +
            "  public java.lang.String getPublicString();\r\n" +
            "  protected int getPrivateInt();\r\n" +
            "  public java.lang.String publicString;\r\n" +
            "}\r\n";

        String actual = sw.getBuffer().toString();
        assertEquals(expected.length(), actual.length());
        assertEquals(expected, actual);
    }
}
