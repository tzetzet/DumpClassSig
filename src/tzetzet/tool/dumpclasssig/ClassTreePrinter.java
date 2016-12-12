/*
 * (C) 2016 tzetzet
 */
package tzetzet.tool.dumpclasssig;

import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * クラス名一覧を所属パッケージごとにツリー形式でプリントする機能を提供します.
 */
public class ClassTreePrinter {
    private static final String ROOT_NAME = "/";

    private final TreeElement mClassTree = new TreeElement(ROOT_NAME);

    public void addClass(String classname) {
        String[] nodes = classname.split("/");
        TreeElement current = mClassTree ;
        for (String node : nodes) {
            TreeElement child = current.getChild(node);
            if (child == null) {
                child = new TreeElement(node);
                current.addChild(child);
            }
            current = child;
        }
    }

    public void print(PrintWriter writer) {
        printElement(writer, mClassTree , "  ");
    }

    private void printElement(PrintWriter writer, TreeElement elem, String prevprefix) {
        StringBuilder prefix = new StringBuilder(prevprefix).append("|-");
        if (elem.hasChild()) {
            prefix.append("+");
        } else {
            prefix.append("-");
        }
        writer.println(prefix.append(elem.getName()));

        prefix.replace(0, prefix.length(), prevprefix);
        if (elem.isLastChild()) {
            prefix.append("   ");
        } else {
            prefix.append("   ");
            ///// prefix.append("|  ");
        }
        for (TreeElement child : elem) {
            printElement(writer, child, prefix.toString());
        }
    }
}

class TreeElement implements Comparable<TreeElement>, Iterable<TreeElement> {
    private final String mName;
    private TreeElement mParent;
    private final SortedSet<TreeElement> mChilds = new TreeSet<>();

    public TreeElement(String name) {
        if (name == null) {
            throw new RuntimeException("name: " + name);
        }
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void addChild(TreeElement elem) {
        assert(elem.mParent == null);
        elem.mParent = this;
        mChilds.add(elem);
    }

    public TreeElement getChild(String name) {
        if (name == null) {
            return null;
        }
        for (TreeElement child : mChilds) {
            if (name.equals(child.mName)) {
                return child;
            }
        }
        return null;
    }

    public boolean hasChild() {
        return (mChilds.size() > 0);
    }

    public boolean isLastChild() {
        return (mParent == null) || (mParent.mChilds.size() > 0 && mParent.mChilds.last() == this);
    }

    @Override
    public Iterator<TreeElement> iterator() {
        return mChilds.iterator();
    }

    @Override
    public int compareTo(TreeElement elem) {
        return mName.compareTo(elem.mName);
    }
}
