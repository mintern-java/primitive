/*
 * Copyright 2016 Brandon Mintern <brandon@mintern.net>.
 * DPQS9_REGRESSION Copyright 2016 Everlaw <oss@everlaw.com>.
 * License: GPLv2+CE <http://openjdk.java.net/legal/gplv2+ce.html>
 */
package net.mintern.primitive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import net.mintern.primitive.comparators.ByteComparator;
import net.mintern.primitive.comparators.CharComparator;
import net.mintern.primitive.comparators.DoubleComparator;
import net.mintern.primitive.comparators.FloatComparator;
import net.mintern.primitive.comparators.IntComparator;
import net.mintern.primitive.comparators.LongComparator;
import net.mintern.primitive.comparators.ShortComparator;

import org.junit.Assert;
import org.junit.Test;

public class PrimitiveTest {

    final Random rng = new Random(1234567890);

    final int[] ARRAY_SIZES_TO_TEST = { 0, 1, 2, 4, 8, 20, 100, 300, 5000, 50000 };

    final int REPEAT_WITH_RANDOMNESS = 5;

    final String[] names = {
        "empty",
        "short",
        "short reversed",
        "all byte values",
        "all byte values reversed",
        "999 0s",
        "999 random values",
        "one item"
    };

    final byte[][] bytes = {
        {},
        { -128, -100, -78, -55, -24, -1, 3, 6, 25, 84, 97, 99, 100, 127 },
        { 127, 100, 99, 97, 84, 25, 6, 3, -1, -24, -55, -78, -100, -128 },
        new byte[256],
        new byte[256],
        new byte[999],
        new byte[999],
        { 42 }
    };

    {
        // bytes[3]: all byte values
        byte b = Byte.MIN_VALUE;
        for (int i = 0; i < bytes[3].length; i++) {
            bytes[3][i] = b++;
        }
        // bytes[4]: all byte values reversed
        for (int i = 0; i < bytes[3].length; i++) {
            bytes[4][i] = bytes[3][256 - i - 1];
        }
        // bytes[5]: all 0s
        Arrays.fill(bytes[5], (byte) 0);
        // bytes[6]: random values
        rng.nextBytes(bytes[6]);
    }

    @Test
    public void testBooleanSort() {
        boolean[] a = new boolean[999];
        int falseCount = 0;
        for (int i = 0; i < 999; i++) {
            if (!(a[i] = bytes[6][i] % 2 == 0)) {
                falseCount++;
            }
        }
        Primitive.sort(a);
        for (int i = 0; i < falseCount; i++) {
            assertFalse(a[i]);
        }
        for (int i = falseCount; i < 999; i++) {
            assertTrue(a[i]);
        }
    }

    @Test
    public void testBytesNoBoundsNullComparator() {
        for (int i = 0; i < bytes.length; i++) {
            for (boolean stable: new boolean[]{ false, true }) {
                String name = names[i] + (stable ? ", stable" : "");
                byte[] a = Arrays.copyOf(bytes[i], bytes[i].length);
                Primitive.sort(a, null, stable);
                for (int j = 1; j < a.length; j++) {
                    assertTrue(name, a[j - 1] <= a[j]);
                }
            }
        }
    }

    @Test
    public void testBytesNoBoundsEverythingEqualComparator() {
        for (int i = 0; i < bytes.length; i++) {
            String name = names[i];
            byte[] a = Arrays.copyOf(bytes[i], bytes[i].length);
            Primitive.sort(a, new ByteComparator() {
                @Override
                public int compare(byte b1, byte b2) {
                    return 0;
                }
            });
            assertArrayEquals(name, bytes[i], a);
        }
    }

    @Test
    public void testBytesNoBoundsNaturalComparator() {
        for (int i = 0; i < bytes.length; i++) {
            for (boolean stable: new boolean[]{ false, true }) {
                String name = names[i] + (stable ? ", stable" : "");
                byte[] a = Arrays.copyOf(bytes[i], bytes[i].length);
                Primitive.sort(a, new ByteComparator() {
                    @Override
                    public int compare(byte b1, byte b2) {
                        return b1 < b2 ? -1 : b1 == b2 ? 0 : 1;
                    }
                });
                for (int j = 1; j < a.length; j++) {
                    assertTrue(name, a[j - 1] <= a[j]);
                }
            }
        }
    }

    @Test
    public void testSortCharArray() {
        for (int r = 0; r < REPEAT_WITH_RANDOMNESS; r++) {
            for (int size : ARRAY_SIZES_TO_TEST) {
                char[] array = new char[size];
                for (int i = 0; i < array.length; i++) {
                    array[i] = (char) rng.nextInt(Character.MAX_CODE_POINT);
                }
                for (boolean stable: new boolean[]{ false, true }) {
                    char[] actual = array.clone();
                    Primitive.sort(actual, new ReverseCharComparator(), stable);

                    char[] expected = array.clone();
                    Arrays.sort(expected);
                    reverse(expected);

                    Assert.assertArrayEquals(actual, expected);
                }
            }
        }
    }

    @Test
    public void testSortShortArray() {
        for (int r = 0; r < REPEAT_WITH_RANDOMNESS; r++) {
            for (int size : ARRAY_SIZES_TO_TEST) {
                short[] array = new short[size];
                for (int i = 0; i < array.length; i++) {
                    array[i] = (short) rng.nextInt(Short.MAX_VALUE);
                    if (rng.nextBoolean()) {
                        array[i] *= -1;
                    }
                }
                for (boolean stable: new boolean[]{ false, true }) {
                    short[] actual = array.clone();
                    Primitive.sort(actual, new ReverseShortComparator(), stable);

                    short[] expected = array.clone();
                    Arrays.sort(expected);
                    reverse(expected);

                    Assert.assertArrayEquals(actual, expected);
                }
            }
        }
    }

    @Test
    public void testSortIntArray() {
        for (int r = 0; r < REPEAT_WITH_RANDOMNESS; r++) {
            for (int size : ARRAY_SIZES_TO_TEST) {
                int[] array = new int[size];
                for (int i = 0; i < array.length; i++) {
                    array[i] = rng.nextInt();
                    if (rng.nextBoolean()) {
                        array[i] *= -1;
                    }
                }
                for (boolean stable: new boolean[]{ false, true }) {
                    int[] actual = array.clone();
                    Primitive.sort(actual, new ReverseIntComparator(), stable);

                    int[] expected = array.clone();
                    Arrays.sort(expected);
                    reverse(expected);

                    Assert.assertArrayEquals(actual, expected);
                }
            }
        }
    }

    @Test
    public void testSortLongArray() {
        for (int r = 0; r < REPEAT_WITH_RANDOMNESS; r++) {
            for (int size : ARRAY_SIZES_TO_TEST) {
                long[] array = new long[size];
                for (int i = 0; i < array.length; i++) {
                    array[i] = rng.nextLong();
                    if (rng.nextBoolean()) {
                        array[i] *= -1;
                    }
                }
                for (boolean stable: new boolean[]{ false, true }) {
                    long[] actual = array.clone();
                    Primitive.sort(actual, new ReverseLongComparator(), stable);

                    long[] expected = array.clone();
                    Arrays.sort(expected);
                    reverse(expected);

                    Assert.assertArrayEquals(actual, expected);
                }
            }
        }
    }

    @Test
    public void testSortFloatArray() {
        for (int r = 0; r < REPEAT_WITH_RANDOMNESS; r++) {
            for (int size : ARRAY_SIZES_TO_TEST) {
                float[] array = new float[size];
                for (int i = 0; i < array.length; i++) {
                    array[i] = rng.nextFloat();
                }
                for (boolean stable: new boolean[]{ false, true }) {
                    float[] actual = array.clone();
                    Primitive.sort(actual, new ReverseFloatComparator(), stable);

                    float[] expected = array.clone();
                    Arrays.sort(expected);
                    reverse(expected);

                    Assert.assertArrayEquals(actual, expected, 0.0f);
                }
            }
        }
    }

    @Test
    public void testSortDoubleArray() {
        for (int r = 0; r < REPEAT_WITH_RANDOMNESS; r++) {
            for (int size : ARRAY_SIZES_TO_TEST) {
                double[] array = new double[size];
                for (int i = 0; i < array.length; i++) {
                    array[i] = rng.nextDouble();
                }
                for (boolean stable: new boolean[]{ false, true }) {
                    double[] actual = array.clone();
                    Primitive.sort(actual, new ReverseDoubleComparator(), stable);

                    double[] expected = array.clone();
                    Arrays.sort(expected);
                    reverse(expected);

                    Assert.assertTrue(Arrays.equals(actual, expected));
                }
            }
        }
    }

    /**
     * This data Copyright 2016, Everlaw, licensed under GPLv2+CE. All rights
     * reserved. Licensed per agreement by CEO Ajeet Shankar in a <a
     * href="https://github.com/mintern-java/primitive/issues/6#issuecomment-215259069">
     * GitHub Issue</a>.
     */
    final int[] DPQS9_REGRESSION = {
        999,267,269,356,443,999,100,101,102,103,104,105,106,107,108,109,
        110,111,999,114,115,116,117,118,119,120,121,122,123,124,125,126,
        127,128,130,131,132,129,133,134,135,136,137,138,139,140,141,142,
        143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,
        159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,
        175,176,178,180,181,182,183,184,999,218,221,222,224,225,999,112,
        113,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,
        200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,999,
        227,229,231,233,235,237,244,245,246,249,263,265,999,223,999,228,
        234,266,999,357,359,360,361,362,358,363,364,365,366,367,368,369,
        370,371,372,373,374,375,376,377,378,379,380,381,382,383,384,385,
        386,387,388,389,390,391,392,393,394,396,395,397,398,399,400,401,
        402,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,
        418,419,420,421,422,423,424,425,426,427,428,429,430,431,432,433,
        434,436,435,437,438,439,440,441,442,999,217,230,232,264,999,268,
        999,219,220,250,251,252,253,254,255,256,257,258,259,260,261,262,
        999,179,999,236,999,216,999,226,238,239,240,241,242,999,270,271,
        272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,
        288,289,291,290,292,293,294,295,296,297,298,299,300,301,303,302,
        304,305,306,307,308,309,310,311,312,313,314,316,315,318,317,319,
        320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,336,
        335,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,
        352,353,354,355,177,999,243,999,444,445,446,447,448,449,450,452,
        453,454,451,455,456,457,458,459,460,461,462,463,464,465,466,467,
        468,469,470,471,472,473,474,475,476,477,478,479,480,481,482,483,
        484,485,486,487,488,489,490,491,492,493,495,494,496,497,498,499,
        500,501,502,999,215,999,248,247,999,999,999,999,999,999,999,999
    };

    @Test
    public void testDualPivotQuicksort9Regression() {
        int[] copy = Arrays.copyOf(DPQS9_REGRESSION, DPQS9_REGRESSION.length);
        Primitive.sort(copy, new IntComparator() {
            @Override
            public int compare(int i1, int i2) {
                return i1 < i2 ? -1 : i1 == i2 ? 0 : 1;
            }
        }, false);
        int[] expected = Arrays.copyOf(DPQS9_REGRESSION, DPQS9_REGRESSION.length);
        Arrays.sort(expected);
        assertArrayEquals(expected, copy);
    }

    @Test
    public void testBinarySearchNaturalOrder() {
        int[] data = { 1, 2, 3, 4, 5 };
        IntComparator naturalComparator = new IntComparator() {
            @Override
            public int compare(int i1, int i2) {
                return Integer.compare(i1, i2);
            }
        };

        assertEquals(0, Primitive.binarySearch(data, 1, naturalComparator));
        assertEquals(3, Primitive.binarySearch(data, 4, naturalComparator));
        assertEquals(4, Primitive.binarySearch(data, 5, naturalComparator));
        assertEquals(-1, Primitive.binarySearch(data, 0, naturalComparator));
        assertEquals(-6, Primitive.binarySearch(data, 6, naturalComparator));
    }

    @Test
    public void testBinarySearchReversedOrder() {
        int[] data = { 6, 5, 4, 3, 2, 1 };
        IntComparator reverseComparator = new ReverseIntComparator();

        assertEquals(5, Primitive.binarySearch(data, 1, reverseComparator));
        assertEquals(2, Primitive.binarySearch(data, 4, reverseComparator));
        assertEquals(0, Primitive.binarySearch(data, 6, reverseComparator));
        assertEquals(-7, Primitive.binarySearch(data, 0, reverseComparator));
        assertEquals(-1, Primitive.binarySearch(data, 7, reverseComparator));
    }

    private static final class ReverseShortComparator implements ShortComparator {
        @Override
        public final int compare(short d1, short d2) {
            return d1 < d2 ? 1 : d1 == d2 ? 0 : -1;
        }
    }

    private static final class ReverseCharComparator implements CharComparator {
        @Override
        public final int compare(char d1, char d2) {
            return d1 < d2 ? 1 : d1 == d2 ? 0 : -1;
        }
    }

    private static final class ReverseIntComparator implements IntComparator {
        @Override
        public final int compare(int d1, int d2) {
            return d1 < d2 ? 1 : d1 == d2 ? 0 : -1;
        }
    }

    private static final class ReverseLongComparator implements LongComparator {
        @Override
        public final int compare(long d1, long d2) {
            return d1 < d2 ? 1 : d1 == d2 ? 0 : -1;
        }
    }

    private static final class ReverseFloatComparator implements FloatComparator {

        @Override
        public final int compare(float d1, float d2) {
            return Float.compare(d2, d1);
        }

    }

    private static final class ReverseDoubleComparator implements DoubleComparator {

        @Override
        public final int compare(double d1, double d2) {
            return Double.compare(d2, d1);
        }

    }

    private void reverse(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            // swap the elements
            int temp = array[i];
            array[i] = array[array.length - (i + 1)];
            array[array.length - (i + 1)] = temp;
        }
    }

    private void reverse(char[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            // swap the elements
            char temp = array[i];
            array[i] = array[array.length - (i + 1)];
            array[array.length - (i + 1)] = temp;
        }
    }

    private void reverse(short[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            // swap the elements
            short temp = array[i];
            array[i] = array[array.length - (i + 1)];
            array[array.length - (i + 1)] = temp;
        }
    }

    private void reverse(long[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            // swap the elements
            long temp = array[i];
            array[i] = array[array.length - (i + 1)];
            array[array.length - (i + 1)] = temp;
        }
    }

    private void reverse(float[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            // swap the elements
            float temp = array[i];
            array[i] = array[array.length - (i + 1)];
            array[array.length - (i + 1)] = temp;
        }
    }

    private void reverse(double[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            // swap the elements
            double temp = array[i];
            array[i] = array[array.length - (i + 1)];
            array[array.length - (i + 1)] = temp;
        }
    }
}
