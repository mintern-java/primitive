## Java Primitive

Provides utility methods for functionality related to primitive types.
This currently includes sorting and searching based on custom comparators.

To include the library in your project (Java 8+ supported), add the following
to your POM:

```xml
<project>
...
    <dependencies>
        ...
        <dependency>
            <groupId>net.mintern</groupId>
            <artifactId>primitive</artifactId>
            <version>1.3</version>
        </dependency>
        ...
    </dependencies>
...
</project>
```

- [Sort arrays using primitive comparators](#sort-arrays-using-primitive-comparators)
- [Binary search using primitive comparators](#binary-search-using-primitive-comparators)
- [Sample usage](#sample-usage)
- [Contributing](#contributing)
    - [Building](#building)
    - [Where are all the source files?](#where-are-all-the-source-files)
    - [Deploy new release](#deploy-new-release)
- [Related projects](#related-projects)

### Sort arrays using primitive comparators

The `Primitive.sort(...)` methods allow primitive arrays to be sorted using
custom comparators (defined in `net.mintern.primitive.comparators`). This
library provides both stable and unstable sorting algorithms. When the stable
algorithm is used, it keeps two values in the same relative position when the
comparator returns `0` for those two values.

The stable algorithm is based on
[TimSort](http://en.wikipedia.org/wiki/Timsort), the sorting algorithm
originally developed for use in Python. The implementation in this library is
a shameless copy-paste-edit of the JDK 8 version of Joshua Bloch's
`java.util.TimSort` implementation.

The unstable algorithm is based on Java's default primitives `Arrays.sort`
implementation, a [Dual-Pivot
Quicksort](http://en.wikipedia.org/wiki/Quicksort#Variants). For some inputs,
it may be up to twice as fast as stable sorting.

### Binary search using primitive comparators

After sorting an array using a provided comparator, binary search can be
performed using that same comparator. The implementation is another
copy-paste-edit of the JDK 8 implementation.

### Sample usage

```java
int[] arr = ...;

// Sort by absolute values
Primitive.sort(arr, (i1, i2) -> Math.abs(i1) - Math.abs(i2));

// Sort in reverse order
IntComparator revCmp = (i1, i2) -> Integer.compare(i2, i1);
Primitive.sort(arr, revCmp);

// Binary search in reverse order (now that it's sorted)
int n = ...;
Primitive.binarySearch(arr, n, revCmp);
```

### Contributing

I will happily accept Pull Requests. If you have any questions, ask away.
Please keep changes to a minimum; do not make gratuitous style changes.

#### Building

In the root directory, run `mvn install`. That will build everything.

#### Where are all the source files?

For this project, I used an ancient-but-solid (by modern standards) template
language called [FreeMarker](http://freemarker.org) to provide templates that
generate the source. A handy [FreeMarker PreProcessor
(FMPP)](http://fmpp.sourceforge.net/index.html)&mdash;in conjunction with an
[FMPP Maven plugin](https://code.google.com/p/freemarkerpp-maven-plugin/) that
I came across on [Stack
Overflow](http://stackoverflow.com/a/3925944/1237044)&mdash;turns those
templates into the Java source files.

The template configuration and source is in
[`src/main/fmpp`](https://github.com/mintern-java/primitive/tree/master/src/main/fmpp).

#### Deploy new release

    mvn clean deploy -P release

### Related projects

All of my Java libraries are available in the
[mintern-java](https://github.com/mintern-java) organization.
