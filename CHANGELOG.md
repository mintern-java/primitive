### 1.2.2 (2016-04-13)

#### Fixed
- During a non-stable sort, if two different items compared the same, it was
  possible for one item to be replaced by a copy of the other.

#### Performance improvement
- Incorporates <http://hg.openjdk.java.net/jdk9/dev/jdk/rev/19727f9d42dd>.

### 1.2.1 (2015-03-19)

#### Fixed
- Source packaging

### 1.2 (2015-03-07)

#### Fixed
- [TimSort bug](http://envisage-project.eu/proving-android-java-and-python-sorting-algorithm-is-broken-and-how-to-fix-it)
- Stable sort is now the default, as indicated in the documentation. This bug
  was introduced in 1.1.

### 1.1 (2014-11-25)

#### Added
- Unstable sorting

### 1.0 (2014-11-22)

- Initial revision
