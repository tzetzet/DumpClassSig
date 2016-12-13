/*
 * (C) 2016 tzetzet
 */
package tzetzet.tool.dumpclasssig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.BasicType;

/**
 * クラスファイルのシグネチャ(およびコンスタントプール)を解析する機能を提供します.
 *
 * 解析したシグネチャとその他情報は PrintWriter に書き込むことができます.
 * シグネチャの書き込み並び順は一意で、オリジナルの並び順に影響されません.
 */
public class ClassSig implements Comparable<ClassSig> {
    // パース済みの対象クラス情報
    private final JavaClass mJavaclass;

    /*
     * クラスファイルからクラス情報を読み取る.
     */
    public ClassSig(File classfile) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(classfile))) {
            ClassParser parser = new ClassParser(bis, classfile.getName());
            mJavaclass = parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e.getClass().getName() + " ; " + e.getMessage());
        }
    }

    /*
     * JAR ファイルからクラス情報を読み取る.
     */
    public ClassSig(ZipFile zipfile, ZipEntry zipentry) {
        String entryname = zipentry.getName();
        int lastslashindex = entryname.lastIndexOf('/');
        if (lastslashindex >= 0) {
            entryname = entryname.substring(lastslashindex + 1);
        }

        try (BufferedInputStream bis = new BufferedInputStream(zipfile.getInputStream(zipentry))) {
            ClassParser parser = new ClassParser(bis, entryname);
            mJavaclass = parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e.getClass().getName() + " ; " + e.getMessage());
        }
    }

    /*
     * 入力ストリームからクラス情報を読み取る.
     *
     * 与えた入力ストリームは閉じられる.
     */
    public ClassSig(InputStream is, String filename) {
        try (BufferedInputStream bis = new BufferedInputStream(is)) {
            ClassParser parser = new ClassParser(bis, filename);
            mJavaclass = parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e.getClass().getName() + " ; " + e.getMessage());
        }
    }

    @Override
    public int compareTo(ClassSig classsig) {
        return getThisClassName().compareTo(classsig.getThisClassName());
    }

    public boolean isPublicOrProtected() {
        return mJavaclass.isPublic() || mJavaclass.isProtected();
    }

    public String getThisClassName() {
        return mJavaclass.getClassName();
    }

    public void printSig(PrintWriter writer) {
        StringBuilder classtype = new StringBuilder();
        if (mJavaclass.isInterface()) {
            classtype.append("interface ");
        }
        // if (javaclass.isEnum()) {
        //     classtype.append("enum ");
        // }
        if (mJavaclass.isClass()) {
            classtype.append("class ");
        }

        String[] interfacenames = mJavaclass.getInterfaceNames();

        writer.println(classtype.insert(0, getModifierStr(mJavaclass)).toString() + mJavaclass.getClassName());
        if (mJavaclass.isClass() && ! "java.lang.Object".equals(mJavaclass.getClassName())) {
            writer.println("  extends " + mJavaclass.getSuperclassName());
        }
        if (interfacenames.length > 0) {
            if (mJavaclass.isInterface()) {
                writer.print("  extends ");
            } else {
                writer.print("  implements ");
            }
            StringBuilder namesline = new StringBuilder();
            for (String interfacename : interfacenames) {
                namesline.append(interfacename).append(", ");
            }
            writer.println(namesline.substring(0, namesline.length() - 2));
        }
        writer.println("{");

        SortedSet<Method> methods = new TreeSet<>(new MethodComparator());
        for (Method method : mJavaclass.getMethods()) {
            if (method.isPublic() || method.isProtected()) {
                methods.add(method);
            }
        }
        if (! methods.isEmpty()) {
            // printout_.println("  // methods");
        }
        for (Method method : methods) {
            writer.println("  " + getMethodSignature(method));
        }

        SortedSet<Field> fields = new TreeSet<>(new FieldComparator());
        for (Field field : mJavaclass.getFields()) {
            if (field.isPublic() || field.isProtected()) {
                fields.add(field);
            }
        }
        if (! fields.isEmpty()) {
            // printout_.println("  // fields");
        }
        for (Field field : fields) {
            writer.println("  " + getFieldSignature(field));
        }

        writer.println("}");
    }

    public void printConstantpool(PrintWriter writer) {
        writer.println();
        writer.println(mJavaclass);
        // writer.println("----");

        ConstantPool constantpool = mJavaclass.getConstantPool();

        // writer.println();
        writer.println("constant pool count: " + constantpool.getLength());
        if (constantpool.getLength() > 0) {
            writer.println("constant pool: {");
            for (int i = 0; i < constantpool.getLength(); ++i) {
                writer.println("  " + i + ") " + constantpool.getConstant(i));
            }
            writer.println("}");
        }
        writer.println();
        writer.println("----");
    }

    private String getMethodSignature(Method method) {
        return getMethodSignature(method, true);
    }

    private String getMethodSignature(Method method, boolean nativeon) {
        StringBuilder line = new StringBuilder();
        line.append(getModifierStr(method, nativeon));
        line.append(method.getReturnType());
        line.append(" ");
        line.append(method.getName());
        line.append("(");
        if (method.getArgumentTypes().length > 0) {
            for (Type type : method.getArgumentTypes()) {
                line.append(type.toString()).append(", ");
            }
            line.delete(line.length() - 2, line.length());
        }
        line.append(")");
        if (method.getExceptionTable() != null && method.getExceptionTable().getLength() > 0) {
            SortedSet<String> excepnames = new TreeSet<>();
            excepnames.addAll(Arrays.asList(method.getExceptionTable().getExceptionNames()));
            if (! excepnames.isEmpty()) {
                line.append("  throws ");
                line.append(String.join(", ", excepnames));
            }
        }
        line.append(";");
        return line.toString();
    }

    private String getFieldSignature(Field field) {
        return getFieldSignature(field, true);
    }

    private String getFieldSignature(Field field, boolean nativeon) {
        StringBuilder line = new StringBuilder();
        line.append(getModifierStr(field, nativeon));
        line.append(field.getType());
        line.append(" ");
        line.append(field.getName());
        line.append(";");
        return line.toString();
    }

    private String getModifierStr(AccessFlags accessflags) {
        return getModifierStr(accessflags, true);
    }

    private String getModifierStr(AccessFlags accessflags, boolean nativeon) {
        StringBuilder modifier = new StringBuilder();
        if (accessflags.isPublic()) {
            modifier.append("public ");
        }
        if (accessflags.isProtected()) {
            modifier.append("protected ");
        }
        if (accessflags.isPrivate()) {
            modifier.append("private ");
        }
        if (accessflags.isStatic()) {
            modifier.append("static ");
        }
        if (accessflags.isFinal()) {
            modifier.append("final ");
        }
        if (accessflags.isAbstract() && ! accessflags.isInterface()) {
            modifier.append("abstract ");
        }
        if (nativeon && accessflags.isNative()) {
            modifier.append("native ");
        }
        return modifier.toString();
    }

    // メソッドのプリント並び順を決める Comparator
    private class MethodComparator implements Comparator<Method> {
        @Override
        public int compare(Method m1, Method m2) {
            int n1 = comparableInteger(m1);
            int n2 = comparableInteger(m2);
            int result = n2 - n1;
            if (result == 0) {
                result = getMethodSignature(m1).compareTo(getMethodSignature(m2));
            }
            return result;
        }

        private int comparableInteger(Method m) {
            int n = 0;
            if (m.isPublic()) {
                n += 10000000;
            }
            if (m.isStatic()) {
                n += 1000000;
            }
            if ("<init>".equals(m.getName())) {
                n += 100000;
            }
            if (m.isFinal()) {
                n += 10000;
            }
            if (m.isAbstract()) {
                n += 1000;
            }
            if (m.getReturnType() instanceof BasicType) {
                n += 100;
            }
            return n;
        }
    }

    // フィールドのプリント並び順を決める Comparator
    private class FieldComparator implements Comparator<Field> {
        @Override
        public int compare(Field f1, Field f2) {
            int n1 = comparableInteger(f1);
            int n2 = comparableInteger(f2);
            int result = n2 - n1;
            if (result == 0) {
                result = getFieldSignature(f1).compareTo(getFieldSignature(f2));
            }
            return result;
        }

        private int comparableInteger(Field f) {
            int n = 0;
            if (f.isPublic()) {
                n += 10000000;
            }
            if (f.isStatic()) {
                n += 1000000;
            }
            if (f.isFinal()) {
                n += 10000;
            }
            if (f.isAbstract()) {
                n += 1000;
            }
            if (f.getType() instanceof BasicType) {
                n += 100;
            }
            return n;
        }
    }
}
