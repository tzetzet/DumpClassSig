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

public class ClassSigsPoolTest {
    public ClassSigsPoolTest() {
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

    static final String EXPECTED = ClassSigTest.EXPECTED1 + ClassSigTest.EXPECTED2;

    @Test
    public void testDataSamples1() {
        ClassSigsPool classSigsPool = new ClassSigsPool();
        for (int i = 1; i <= 2; i++) {
            String classname = "DataSample" + i;
            String filename = classname + ".class";
            InputStream is = getClass().getResourceAsStream(filename);
            classSigsPool.addClassFile(is, filename);

            // 入力ストリームが閉じられていることを確認
            try {
                is.read();
                fail();
            } catch (IOException e) {
                Logger.getLogger(ClassSigTest.class.getName()).log(Level.FINE, null, e);
            }
        }

        {
            StringWriter sw = new StringWriter();
            PrintWriter writer = new PrintWriter(sw);
            classSigsPool.printSigs(writer, null);

            String expected = EXPECTED;
            String actual = sw.getBuffer().toString();
            assertEquals(expected.length(), actual.length());
            assertEquals(expected, actual);
        }

        {
            StringWriter sw = new StringWriter();
            PrintWriter writer = new PrintWriter(sw);
            classSigsPool.printSigs(writer, new String[] { "tzetzet." });

            String expected = EXPECTED;
            String actual = sw.getBuffer().toString();
            assertEquals(expected.length(), actual.length());
            assertEquals(expected, actual);
        }

        {
            StringWriter sw = new StringWriter();
            PrintWriter writer = new PrintWriter(sw);
            classSigsPool.printSigs(writer, new String[] { "net." });

            String expected = "";
            String actual = sw.getBuffer().toString();
            assertEquals(expected.length(), actual.length());
            assertEquals(expected, actual);
        }
    }

    // クラスファイルを逆順追加
    @Test
    public void testDataSamples2() {
        ClassSigsPool classSigsPool = new ClassSigsPool();
        for (int i = 2; i >= 1; i--) {
            String classname = "DataSample" + i;
            String filename = classname + ".class";
            InputStream is = getClass().getResourceAsStream(filename);
            classSigsPool.addClassFile(is, filename);

            // 入力ストリームが閉じられていることを確認
            try {
                is.read();
                fail();
            } catch (IOException e) {
                Logger.getLogger(ClassSigTest.class.getName()).log(Level.FINE, null, e);
            }
        }

        {
            StringWriter sw = new StringWriter();
            PrintWriter writer = new PrintWriter(sw);
            classSigsPool.printSigs(writer, null);

            String expected = EXPECTED;
            String actual = sw.getBuffer().toString();
            assertEquals(expected.length(), actual.length());
            assertEquals(expected, actual);
        }

        {
            StringWriter sw = new StringWriter();
            PrintWriter writer = new PrintWriter(sw);
            classSigsPool.printSigs(writer, new String[] { "tzetzet." });

            String expected = EXPECTED;
            String actual = sw.getBuffer().toString();
            assertEquals(expected.length(), actual.length());
            assertEquals(expected, actual);
        }

        {
            StringWriter sw = new StringWriter();
            PrintWriter writer = new PrintWriter(sw);
            classSigsPool.printSigs(writer, new String[] { "net." });

            String expected = "";
            String actual = sw.getBuffer().toString();
            assertEquals(expected.length(), actual.length());
            assertEquals(expected, actual);
        }
    }
}
