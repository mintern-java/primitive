package net.mintern.primitive;

import java.util.Arrays;
import java.util.Random;
import net.mintern.primitive.comparators.ByteComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitiveTest {

    String[] names = {
        "empty",
        "short",
        "short reversed",
        "all byte values",
        "all byte values reversed",
        "999 0s",
        "999 random values",
        "one item"
    };

    byte[][] bytes = {
        {},
        { -128, -100, -78, -55, -24, -1, 3, 6, 25, 84, 97, 99, 100, 127 },
        { 127, 100, 99, 97, 84, 25, 6, 3, -1, -24, -55, -78, -100, -128 },
        new byte[256],
        new byte[256],
        new byte[999],
        new byte[999],
        { 42 }
    };

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
                    return b1 < b2 ? -1
                            : b1 == b2 ? 0
                            : 1;
                }
            });
            for (int j = 1; j < a.length; j++) {
                assertTrue(name, a[j - 1] <= a[j]);
            }
        }
    }
}