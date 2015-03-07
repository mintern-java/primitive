This patch is intended to be applied against the JDK's TimSort.java file. At
the time this README was written, the patch was generated against the file at
<http://hg.openjdk.java.net/jdk9/dev/jdk/file/e276aa5b8a4b/src/java.base/share/classes/java/util/TimSort.java>.

After downloading TimSort, apply the patch as follows (from this directory):

    patch -o- ~/Downloads/TimSort.java TimSort.java.ft.patch \
        > ../fmpp/templates/TimSorts.java.ft
