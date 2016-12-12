/*
 * (C) 2016 tzetzet
 */
package tzetzet.tool.dumpclasssig;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * このプログラムは -cp パラメータに与えられた JAR ファイルもしくは
 * クラスファイルを分析し、Public もしくは Protected なクラスを選んで
 * クラス名のツリーをプリントした後、メソッドおよびフィールドの
 * シグニチャを javap 類似のフォーマットでプリントします.
 *
 * -ppx パラメータとしてパッケージ名プレフィックスを与えた場合、この
 * プレフィックスにパッケージ名の先頭がマッチするクラスのシグニチャに絞って
 * プリントします.
 *
 * 引数として明示的にクラス名を与えた場合、そのクラスの詳細情報をプリントします.
 * プリントするのは概要および全フィールドと全メソッドのシグニチャ、コンスタント
 * プールの一覧です.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        options.addOption(Option.builder("cp").longOpt("classpath").argName("pathlist").hasArg().desc("classpath to search classes").build());
        options.addOption(Option.builder("ppx").longOpt("pkgprefix").argName("pkglist").hasArg().desc("prefixes of headlined package names").build());

        if (args.length == 0) {
            new HelpFormatter().printHelp("dumpclasssig <options> <classnames>", options);
            System.exit(1);
        }

        CommandLine parsedCommandline = new DefaultParser().parse(options, args);

        String[] givensearchpaths;
        if (parsedCommandline.hasOption("cp")) {
            String pathlist = parsedCommandline.getOptionValue("cp");
            givensearchpaths = pathlist.split(System.getProperty("path.separator"));
        } else {
            givensearchpaths = new String[] {};
        }

        String[] pkgprefixes = null;
        if (parsedCommandline.hasOption("ppx")) {
            String pkglist = parsedCommandline.getOptionValue("ppx");
            pkgprefixes = pkglist.split(System.getProperty("path.separator"));
        }

        List<String> printableSearchPaths = new ArrayList<>();
        ClassSigsPool classSigsPool = new ClassSigsPool();

        for (String arg : givensearchpaths) {
            File fileent = new File(arg);
            if (! fileent.exists()) {
                throw new IOException(fileent + " not found.");
            }
            if (fileent.isDirectory()) {
                printableSearchPaths.add(fileent.getPath());
            } else {
                printableSearchPaths.add(fileent.getName());
            }
            poolClassesFromFile(classSigsPool, fileent);
        }

        String[] classnames = parsedCommandline.getArgs();

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);

        if (classnames.length > 0) {
            for (String classname : classnames) {
                classSigsPool.printConstantpool(writer, classname);
                writer.println();
            }
        } else {
            writer.println("=== Search Paths ===");
            for (String classpath : printableSearchPaths) {
                writer.println(" " + classpath);
            }
            writer.println();
            classSigsPool.printTree(writer, pkgprefixes);
            writer.println();
            classSigsPool.printSigs(writer, pkgprefixes);
        }

        System.out.print(sw.getBuffer());
        System.out.println();
    }

    private static void poolClassesFromFile(ClassSigsPool classSigsPool, File fileent) {
        if (fileent.isFile()) {
            if (fileent.getName().endsWith(".class")) {
                classSigsPool.addClassFile(fileent);
            } else if (fileent.getName().endsWith(".jar") || fileent.getName().endsWith(".zip")) {
                ZipFile zipfile;
                try {
                    zipfile = new ZipFile(fileent);
                } catch (IOException e) {
                    throw new RuntimeException(e.getClass().getName() + " ; " + e.getMessage());
                }
                Enumeration<? extends ZipEntry> e = zipfile.entries();
                while (e.hasMoreElements()) {
                    ZipEntry zipentry = e.nextElement();
                    if (zipentry.getName().endsWith(".class")) {
                        classSigsPool.addClassFile(zipfile, zipentry);
                    }
                }
            }
        } else if (fileent.isDirectory()) {
            for (File subentry : fileent.listFiles()) {
                poolClassesFromFile(classSigsPool, subentry);
            }
        } else {
            assert false;
        }
    }
}
