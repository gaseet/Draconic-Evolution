package com.brandon3055.draconicevolution.lib;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.lib.OPStorageOP;
import com.google.common.math.BigIntegerMath;
import net.minecraft.client.resources.language.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by brandon3055 on 21/09/2022
 */
public class OPStorageOPTest {
    public static final Logger LOGGER = LogManager.getLogger("OPStorageOPTest");

    @Test
    public void testToString() {
        OPStorageOP storageOP = new OPStorageOP(null, () -> -1L);

        //9,999,999,999,999                                         Tera            numprefix.draconicevolution.10-12
        storageOP.receiveOP(9876999999999L, false);
        assertEquals("9.876numprefix.draconicevolution.10-12", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999                                     Peta            numprefix.draconicevolution.10-15
        storageOP.receiveOP(9876999999999999L - 9999999999999L, false);
        assertEquals("9.876numprefix.draconicevolution.10-15", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999,999                                 Exa             numprefix.draconicevolution.10-18
        storageOP.overflowCount = BigInteger.valueOf(1);
        storageOP.valueStorage = 776627963145224191L;
        assertEquals("9.999numprefix.draconicevolution.10-18", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999,999,999                             Zetta           numprefix.draconicevolution.10-21
        storageOP.overflowCount = BigInteger.valueOf(1084);
        storageOP.valueStorage = 1864712049423024127L;
        assertEquals("9.999numprefix.draconicevolution.10-21", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999,999,999,999                         Yotta           numprefix.draconicevolution.10-24
        storageOP.overflowCount = BigInteger.valueOf(1084202);
        storageOP.valueStorage = 1590897978359414783L;
        assertEquals("9.999numprefix.draconicevolution.10-24", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999,999,999,999,999                     Octillion       numprefix.draconicevolution.10-27
        storageOP.overflowCount = BigInteger.valueOf(1084202172);
        storageOP.valueStorage = 4477988020393345023L;
        assertEquals("9.999numprefix.draconicevolution.10-27", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999,999,999,999,999,999                 Nonillion       numprefix.draconicevolution.10-30
        storageOP.overflowCount = BigInteger.valueOf(1084202172485L);
        storageOP.valueStorage = 4652582518778757119L;
        assertEquals("9.999numprefix.draconicevolution.10-30", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999,999,999,999,999,999,999             Decillion       numprefix.draconicevolution.10-33
        storageOP.overflowCount = BigInteger.valueOf(1084202172485504L);
        storageOP.valueStorage = 4003012203950112767L;
        assertEquals("9.999numprefix.draconicevolution.10-33", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999,999,999,999,999,999,999,999         Undecillion     numprefix.draconicevolution.10-36
        storageOP.overflowCount = BigInteger.valueOf(1084202172485504434L);
        storageOP.valueStorage = 68739955140067327L;
        assertEquals("9.999numprefix.draconicevolution.10-36", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        //9,999,999,999,999,999,999,999,999,999,999,999,999,999     Duodecillion    numprefix.draconicevolution.10-39
        storageOP.overflowCount = new BigInteger("1084202172485504434007");
        storageOP.valueStorage = 4176350882083897343L;
        assertEquals("9.999numprefix.draconicevolution.10-39", storageOP.getReadable().getString());
//        LOGGER.info(storageOP.getScientific());

        storageOP.valueStorage = 0;
        storageOP.overflowCount = BigInteger.ZERO;

        BigInteger total = BigInteger.ZERO;
        Random random = new Random(0);
        for (int i = 0; i < 1000000; i++) {
            long toAdd = Math.abs(random.nextLong());
            total = total.add(BigInteger.valueOf(toAdd));
            storageOP.receiveOP(toAdd, false);

            int digits = BigIntegerMath.log10(total, RoundingMode.DOWN);
            int prefixStep = (digits / 3) * 3;
            BigDecimal decimal = new BigDecimal(total).divide(BigDecimal.valueOf(10).pow(prefixStep), 3, RoundingMode.DOWN);
            assertEquals(decimal.doubleValue() + "" + "numprefix.draconicevolution.10-" + prefixStep, storageOP.getReadable().getString());
        }

    }

    @Test
    public void testReceiveLongValues() {
        // Test that receiveOP handles long values beyond Integer.MAX_VALUE
        OPStorageOP storageOP = new OPStorageOP(null, () -> -1L);

        // Receive Integer.MAX_VALUE
        long received1 = storageOP.receiveOP(Integer.MAX_VALUE, false);
        assertEquals(Integer.MAX_VALUE, received1);
        assertEquals(BigInteger.valueOf(Integer.MAX_VALUE), storageOP.getStoredBig());

        // Receive Long.MAX_VALUE on top of that
        long received2 = storageOP.receiveOP(Long.MAX_VALUE, false);
        assertEquals(Long.MAX_VALUE, received2);
        BigInteger expected = BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.valueOf(Long.MAX_VALUE));
        assertEquals(expected, storageOP.getStoredBig());
    }

    @Test
    public void testExtractOverflowRollover() {
        // Test that extractOP correctly handles overflow rollover without off-by-one
        OPStorageOP storageOP = new OPStorageOP(null, () -> -1L);

        // Set up: overflowCount=1, valueStorage=5 → total = Long.MAX_VALUE + 5
        storageOP.overflowCount = BigInteger.ONE;
        storageOP.valueStorage = 5;
        BigInteger initialTotal = storageOP.getStoredBig();
        assertEquals(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(5)), initialTotal);

        // Extract 100
        long extracted = storageOP.extractOP(100, false);
        assertEquals(100, extracted);
        BigInteger afterExtract = storageOP.getStoredBig();
        assertEquals(initialTotal.subtract(BigInteger.valueOf(100)), afterExtract);
    }

    @Test
    public void testExtractOverflowMultipleRollovers() {
        // Test repeated receive and extract cycles with overflow
        OPStorageOP storageOP = new OPStorageOP(null, () -> -1L);

        BigInteger total = BigInteger.ZERO;
        Random random = new Random(42);

        // Add a bunch of energy to create overflow
        for (int i = 0; i < 100; i++) {
            long toAdd = Math.abs(random.nextLong());
            total = total.add(BigInteger.valueOf(toAdd));
            storageOP.receiveOP(toAdd, false);
        }
        assertEquals(total, storageOP.getStoredBig());

        // Now extract random amounts and verify total stays consistent
        for (int i = 0; i < 50; i++) {
            long toExtract = Math.abs(random.nextLong()) % 1000000L + 1;
            total = total.subtract(BigInteger.valueOf(toExtract));
            storageOP.extractOP(toExtract, false);
            assertEquals(total, storageOP.getStoredBig(), "Mismatch after extract iteration " + i);
        }
    }

    @Test
    public void testLimitedCapacityReceive() {
        // Test that limited capacity cores accept the correct amount
        long capacity = 9_880_000_000L; // Tier 4 capacity
        OPStorageOP storageOP = new OPStorageOP(null, () -> capacity);

        // Try to receive Long.MAX_VALUE - should only accept up to capacity
        long received = storageOP.receiveOP(Long.MAX_VALUE, false);
        assertEquals(capacity, received);
        assertEquals(BigInteger.valueOf(capacity), storageOP.getStoredBig());
    }

    @Test
    public void testLimitedCapacityReceiveDifference() {
        // Test that limited capacity cores accept different amounts for int vs long input
        long capacity = 9_880_000_000L; // Tier 4 capacity (> Integer.MAX_VALUE)
        OPStorageOP storageOP1 = new OPStorageOP(null, () -> capacity);
        OPStorageOP storageOP2 = new OPStorageOP(null, () -> capacity);

        long receivedInt = storageOP1.receiveOP(Integer.MAX_VALUE, false);
        long receivedLong = storageOP2.receiveOP(Long.MAX_VALUE, false);

        // Integer.MAX_VALUE < capacity, so it should accept all of Integer.MAX_VALUE
        assertEquals(Integer.MAX_VALUE, receivedInt);
        // Long.MAX_VALUE > capacity, so it should accept exactly capacity
        assertEquals(capacity, receivedLong);
        // The amounts should be different
        assertEquals(BigInteger.valueOf(Integer.MAX_VALUE), storageOP1.getStoredBig());
        assertEquals(BigInteger.valueOf(capacity), storageOP2.getStoredBig());
    }

    @Test
    public void testReceiveWhenOverCapacityReturnsZero() {
        // Test that receiveOP returns 0 (not negative) when valueStorage exceeds limit
        long capacity = 1_000_000L;
        OPStorageOP storageOP = new OPStorageOP(null, () -> capacity);

        // Manually set valueStorage above the limit (simulates config change or edge case)
        storageOP.valueStorage = capacity + 500_000L;

        // receiveOP should return 0 (not a negative value) since storage > capacity
        long received = storageOP.receiveOP(Long.MAX_VALUE, false);
        assertEquals(0, received);
        // valueStorage should not change
        assertEquals(capacity + 500_000L, storageOP.valueStorage);
    }

    @Test
    public void testReceiveWhenOverCapacitySimulate() {
        // Test simulate mode also returns 0 when over capacity
        long capacity = 1_000_000L;
        OPStorageOP storageOP = new OPStorageOP(null, () -> capacity);

        storageOP.valueStorage = capacity + 100L;

        long received = storageOP.receiveOP(1000, true);
        assertEquals(0, received);
        // valueStorage should not change in simulate mode
        assertEquals(capacity + 100L, storageOP.valueStorage);
    }
}
