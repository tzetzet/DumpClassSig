/*
 * (C) 2016 tzetzet
 */
package tzetzet.tool.dumpclasssig;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * クラスファイルの解析情報をプールし、選択的にプリントする機能を提供します.
 */
public class ClassSigsPool {
    private final SortedSet<ClassSig> mClassSigs = new TreeSet<>();

    public void addClassFile(File classfile) {
        addClassSig(new ClassSig(classfile));
    }

    public void addClassFile(ZipFile zipfile, ZipEntry zipentry) {
        addClassSig(new ClassSig(zipfile, zipentry));
    }

    public void addClassFile(InputStream is, String filename) {
        addClassSig(new ClassSig(is, filename));
    }

    public void printTree(PrintWriter writer, String[] pkgprefixes) {
        if (pkgprefixes == null) {
            pkgprefixes = new String[] { "." };
        }
        List<Pattern> patterns = new ArrayList<>();
        for (String pkgprefix : pkgprefixes) {
            patterns.add(Pattern.compile(pkgprefix));
        }

        ClassTreePrinter classTreePrinter = new ClassTreePrinter();
        for (ClassSig classsig : mClassSigs) {
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(classsig.getThisClassName());
                if (matcher.lookingAt()) {
                    classTreePrinter.addClass(classsig.getThisClassName().replace('.', '/'));
                    break;
                }
            }
        }

        classTreePrinter.print(writer);
    }

    public void printSigs(PrintWriter writer, String[] pkgprefixes) {
        if (pkgprefixes == null) {
            pkgprefixes = new String[] { "." };
        }
        List<Pattern> patterns = new ArrayList<>();
        for (String pkgprefix : pkgprefixes) {
            patterns.add(Pattern.compile(pkgprefix));
        }

        for (ClassSig classsig : mClassSigs) {
            if (!classsig.isPublicOrProtected()) {
                continue;
            }

            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(classsig.getThisClassName());
                if (matcher.lookingAt()) {
                    classsig.printSig(writer);
                    break;
                }
            }
        }
    }

    public void printConstantpool(PrintWriter writer, String classname) {
        for (ClassSig classsig : mClassSigs) {
            if (classname.equals(classsig.getThisClassName())) {
                classsig.printConstantpool(writer);
                break;
            }
        }
    }

    private void addClassSig(ClassSig classsig) {
        if (classsig.isPublicOrProtected()) {
            mClassSigs.add(classsig);
        }
    }
}
