#! /bin/sh

## set -xv

inidir=`pwd`
cmdpath=$0
while [ -h $cmdpath ] ; do
  cmdpath=`readlink $0`
done
cd `dirname $cmdpath`
moddir=`pwd`
cd $inidir

java -cp ${moddir}/dist/lib/bcel-6.0.jar\;${moddir}/dist/lib/commons-cli-1.3.1.jar\;${moddir}/dist/DumpClassSig.jar tzetzet.tool.dumpclasssig.Main $*
