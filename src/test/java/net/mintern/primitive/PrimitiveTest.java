package net.mintern.primitive;

import static org.junit.Assert.assertArrayEquals;
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

    private static final int[] ARRAY_SIZES_TO_TEST = new int[] { 0, 1, 2, 4, 8, 20, 100, 300, 5000, 50000 };

    private static final int REPEAT_WITH_RANDOMNESS = 5;

    String[] names = { "empty", "short", "short reversed", "all byte values", "all byte values reversed", "999 0s",
            "999 random values", "one item" };

    byte[][] bytes = { {}, { -128, -100, -78, -55, -24, -1, 3, 6, 25, 84, 97, 99, 100, 127 },
            { 127, 100, 99, 97, 84, 25, 6, 3, -1, -24, -55, -78, -100, -128 }, new byte[256], new byte[256],
            new byte[999], new byte[999], { 42 } };

    Random rng = new Random(1234567890);

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
    public void testBytesNoBoundsNullComparator() {
        for (int i = 0; i < bytes.length; i++) {
            String name = names[i];
            byte[] a = Arrays.copyOf(bytes[i], bytes[i].length);
            Primitive.sort(a, null);
            for (int j = 1; j < a.length; j++) {
                assertTrue(name, a[j - 1] <= a[j]);
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
            String name = names[i];
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

    @Test
    public void testSortCharArray() {
        for (int r = 0; r < REPEAT_WITH_RANDOMNESS; r++) {
            for (int size : ARRAY_SIZES_TO_TEST) {
                char[] array = new char[size];
                for (int i = 0; i < array.length; i++) {
                    array[i] = (char) rng.nextInt(Character.MAX_CODE_POINT);
                }

                char[] actual = array.clone();
                Primitive.sort(actual, new ReverseCharComparator());

                char[] expected = array.clone();
                Arrays.sort(expected);
                reverse(expected);

                Assert.assertArrayEquals(actual, expected);
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

                short[] actual = array.clone();
                Primitive.sort(actual, new ReverseShortComparator());

                short[] expected = array.clone();
                Arrays.sort(expected);
                reverse(expected);

                Assert.assertArrayEquals(actual, expected);
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

                int[] actual = array.clone();
                Primitive.sort(actual, new ReverseIntComparator());

                int[] expected = array.clone();
                Arrays.sort(expected);
                reverse(expected);

                Assert.assertArrayEquals(actual, expected);
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

                long[] actual = array.clone();
                Primitive.sort(actual, new ReverseLongComparator());

                long[] expected = array.clone();
                Arrays.sort(expected);
                reverse(expected);

                Assert.assertArrayEquals(actual, expected);
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

                float[] actual = array.clone();
                Primitive.sort(actual, new ReverseFloatComparator());

                float[] expected = array.clone();
                Arrays.sort(expected);
                reverse(expected);

                Assert.assertArrayEquals(actual, expected, 0.0f);
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

                double[] actual = array.clone();
                Primitive.sort(actual, new ReverseDoubleComparator());

                double[] expected = array.clone();
                Arrays.sort(expected);
                reverse(expected);

                Assert.assertTrue(Arrays.equals(actual, expected));
            }
        }
    }

    private static final class ReverseShortComparator implements ShortComparator {
        @Override
        public final int compare(short d1, short d2) {
            return Short.compare(d2, d1);
        }
    }

    private static final class ReverseCharComparator implements CharComparator {
        @Override
        public final int compare(char d1, char d2) {
            return Character.compare(d2, d1);
        }
    }

    private static final class ReverseIntComparator implements IntComparator {
        @Override
        public final int compare(int d1, int d2) {
            return Integer.compare(d2, d1);
        }
    }

    private static final class ReverseLongComparator implements LongComparator {
        @Override
        public final int compare(long d1, long d2) {
            return Long.compare(d2, d1);
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

    private void reverse(boolean[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            // swap the elements
            boolean temp = array[i];
            array[i] = array[array.length - (i + 1)];
            array[array.length - (i + 1)] = temp;
        }
    }

    private void sort(boolean[] array) {
        Boolean[] array2 = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            array2[i] = array[i];
        }
        Arrays.sort(array2);
        for (int i = 0; i < array.length; i++) {
            array[i] = array2[i].booleanValue();
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