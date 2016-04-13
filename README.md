## Java Primitive

Provides utility methods for functionality related to primitive types.
Currently, the only functionality is the ability to sort primitive arrays
using custom comparators.

To include the library in your project (Java 6+ supported), add the following
to your POM:

```xml
<project>
...
    <dependencies>
        ...
        <dependency>
            <groupId>net.mintern</groupId>
            <artifactId>primitive</artifactId>
            <version>1.2.2</version>
        </dependency>
        ...
    </dependencies>
...
</project>
```

- [Sort arrays using primitive comparators](#sort-arrays-using-primitive-comparators)
    - [Sample usage](#sample-usage)
- [Why not `Primitives`?](#why-not-primitives)
- [Contributing](#contributing)
    - [Building](#building)
    - [Where are all the source files?](#where-are-all-the-source-files)
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
a shameless copy-paste-edit of the latest JDK version of Joshua Bloch's
`java.util.TimSort` implementation.

The unstable algorithm is based on Java's default primitives `Arrays.sort`
implementation, a [Dual-Pivot
Quicksort](http://en.wikipedia.org/wiki/Quicksort#Variants). For some inputs,
it may be up to twice as fast as stable sorting.

Although this library is built for Java 6+, the comparators are more
convenient when used with Java 8 (as the sample usage illustrates).

#### Sample usage

```java
int[] arr = ...;
// Sort in reverse order
Primitive.sort(arr, (i1, i2) -> Integer.compare(i2, i1));
// Sort by absolute values
Primitive.sort(arr, (i1, i2) -> Math.abs(i1) - Math.abs(i2));
```

### Why not `Primitives`?

Since Java's built-in sorting methods are called `Arrays.sort`, it seems
reasonable to call these ones `Primitives.sort`. I would prefer that as well,
but several popular libraries (most notably `gson`, `guava`, and
`jetty-websocket-client-impl`) already define a `Primitives` class, so I opted
for a name with fewer collisions.

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

### Related projects

All of my Java libraries are available in the
[mintern-java](https://github.com/mintern-java) organization.
