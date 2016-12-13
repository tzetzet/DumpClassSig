<h1>DumpClassSig</h1>

<div align="right">2016-12-13 tzetzet</div>

Purpose of the tool
-------------------

The tool prints javap-like but more-controlled class signatures of given
class libraries or class files. The tool prints class signatures of public
and protected to grab the outlines.

Sample output
-------------

Below is a part of sample output from Apache Commons IO commons-io-1.3.1.jar.
The tool prints initially a public/protected class tree then public/protected
signatures.

    === Search Paths ===
     commons-io-1.3.1.jar
    
      |-+/
         |-+org
            |-+apache
               |-+commons
                  |-+io
                     |--CopyUtils
                     |--DirectoryWalker
                     |--DirectoryWalker$CancelException
                     |--EndianUtils
                     |--FileCleaner
                     |--FileDeleteStrategy
                     |--FileSystemUtils
                     |--FileUtils
                     |--FilenameUtils
                     |--HexDump
                     |--IOCase
                     |--IOUtils
                     |--LineIterator
                     |-+filefilter
                        |--AbstractFileFilter
                        |--AgeFileFilter
                        |--AndFileFilter
                        |--CanReadFileFilter
                        |--CanWriteFileFilter
                        |--ConditionalFileFilter
                        |--DelegateFileFilter
                        |--DirectoryFileFilter
                        |--EmptyFileFilter
                        |--FalseFileFilter
                        |--FileFileFilter
                        |--FileFilterUtils
                        |--HiddenFileFilter
                        |--IOFileFilter
                        |--NameFileFilter
                        |--NotFileFilter
                        |--OrFileFilter
                        |--PrefixFileFilter
                        |--SizeFileFilter
                        |--SuffixFileFilter
                        |--TrueFileFilter
                        |--WildcardFileFilter
                        |--WildcardFilter
                     |-+input
                        |--ClassLoaderObjectInputStream
                        |--CountingInputStream
                        |--DemuxInputStream
                        |--NullInputStream
                        |--NullReader
                        |--ProxyInputStream
                        |--ProxyReader
                        |--SwappedDataInputStream
                     |-+output
                        |--ByteArrayOutputStream
                        |--CountingOutputStream
                        |--DeferredFileOutputStream
                        |--DemuxOutputStream
                        |--LockableFileWriter
                        |--NullOutputStream
                        |--NullWriter
                        |--ProxyOutputStream
                        |--ProxyWriter
                        |--TeeOutputStream
                        |--ThresholdingOutputStream
    
    public class org.apache.commons.io.CopyUtils
      extends java.lang.Object
    {
      public static int copy(java.io.InputStream, java.io.OutputStream)  throws java.io.IOException;
      public static int copy(java.io.Reader, java.io.Writer)  throws java.io.IOException;
      public static void copy(byte[], java.io.OutputStream)  throws java.io.IOException;
      public static void copy(byte[], java.io.Writer)  throws java.io.IOException;
      public static void copy(byte[], java.io.Writer, java.lang.String)  throws java.io.IOException;
      public static void copy(java.io.InputStream, java.io.Writer)  throws java.io.IOException;
      public static void copy(java.io.InputStream, java.io.Writer, java.lang.String)  throws java.io.IOException;
      public static void copy(java.io.Reader, java.io.OutputStream)  throws java.io.IOException;
      public static void copy(java.lang.String, java.io.OutputStream)  throws java.io.IOException;
      public static void copy(java.lang.String, java.io.Writer)  throws java.io.IOException;
      public void <init>();
    }
    public abstract class org.apache.commons.io.DirectoryWalker
      extends java.lang.Object
    {
      protected void <init>();
      protected void <init>(java.io.FileFilter, int);
      protected void <init>(org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.filefilter.IOFileFilter, int);
      protected final void checkIfCancelled(java.io.File, int, java.util.Collection)  throws java.io.IOException;
      protected final void walk(java.io.File, java.util.Collection)  throws java.io.IOException;
      protected boolean handleDirectory(java.io.File, int, java.util.Collection)  throws java.io.IOException;
      protected boolean handleIsCancelled(java.io.File, int, java.util.Collection)  throws java.io.IOException;
      protected void handleCancelled(java.io.File, java.util.Collection, org.apache.commons.io.DirectoryWalker$CancelException)  throws java.io.IOException;
      protected void handleDirectoryEnd(java.io.File, int, java.util.Collection)  throws java.io.IOException;
      protected void handleDirectoryStart(java.io.File, int, java.util.Collection)  throws java.io.IOException;
      protected void handleEnd(java.util.Collection)  throws java.io.IOException;
      protected void handleFile(java.io.File, int, java.util.Collection)  throws java.io.IOException;
      protected void handleRestricted(java.io.File, int, java.util.Collection)  throws java.io.IOException;
      protected void handleStart(java.io.File, java.util.Collection)  throws java.io.IOException;
    }
    ...
    (signatures snipped)
    ...
    public class org.apache.commons.io.output.TeeOutputStream
      extends org.apache.commons.io.output.ProxyOutputStream
    {
      public void <init>(java.io.OutputStream, java.io.OutputStream);
      public void close()  throws java.io.IOException;
      public void flush()  throws java.io.IOException;
      public void write(byte[])  throws java.io.IOException;
      public void write(byte[], int, int)  throws java.io.IOException;
      public void write(int)  throws java.io.IOException;
      protected java.io.OutputStream branch;
    }
    public abstract class org.apache.commons.io.output.ThresholdingOutputStream
      extends java.io.OutputStream
    {
      public void <init>(int);
      public boolean isThresholdExceeded();
      public int getThreshold();
      public long getByteCount();
      public void close()  throws java.io.IOException;
      public void flush()  throws java.io.IOException;
      public void write(byte[])  throws java.io.IOException;
      public void write(byte[], int, int)  throws java.io.IOException;
      public void write(int)  throws java.io.IOException;
      protected abstract void thresholdReached()  throws java.io.IOException;
      protected abstract java.io.OutputStream getStream()  throws java.io.IOException;
      protected void checkThreshold(int)  throws java.io.IOException;
    }

Command options
---------------

    usage: dumpclasssig <options> <classnames>
     -cp,--classpath <pathlist>   classpath to search classes
     -ppx,--pkgprefix <pkglist>   prefixes of headlined package names
