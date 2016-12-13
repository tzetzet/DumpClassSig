package tzetzet.tool.dumpclasssig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
    public MainTest() {
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
    public void testAndroidJar1() throws IOException {
        File jarfile = new File("samples/android.jar");
        assertTrue(jarfile.exists());
        File dmpfile = new File("samples/android.dump");
        assertTrue(dmpfile.exists());

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        Main.printClassSigs(writer, new String[] { jarfile.getPath() }, null, null);

        diffPrintedOutputs(dmpfile, sw);
    }

    @Test
    public void testAndroidJar2() throws IOException {
        File jarfile = new File("samples/android.jar");
        assertTrue(jarfile.exists());
        File dmpfile = new File("samples/android.partial.dump");
        assertTrue(dmpfile.exists());

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        Main.printClassSigs(writer, new String[] { jarfile.getPath() },  new String[] {"android\\.webkit\\.", "org\\.json\\." }, null);

        diffPrintedOutputs(dmpfile, sw);
    }

    @Test
    public void testCommonsIoJar1() throws IOException {
        File jarfile = new File("samples/commons-io-1.3.1.jar");
        assertTrue(jarfile.exists());
        File dmpfile = new File("samples/commons-io-1.3.1.dump");
        assertTrue(dmpfile.exists());

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        Main.printClassSigs(writer, new String[] { jarfile.getPath() }, null, null);

        diffPrintedOutputs(dmpfile, sw);
    }

    private void diffPrintedOutputs(File expectedFile, StringWriter actualWriter) throws IOException {
        try (BufferedReader actReader = new BufferedReader(new StringReader(actualWriter.getBuffer().toString()))) {
            try (BufferedReader expReader = new BufferedReader(new FileReader(expectedFile))) {
                int linecounter = 0;
                while (true) {
                    String expLine = expReader.readLine();
                    String actLine = actReader.readLine();
                    assertEquals("linecounter: " + linecounter++, expLine, actLine);
                    if (expLine == null) {
                        break;
                    }
                }
            }
        }
    }
}
