package fr.eurecom.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    void isIpv4() {
        final boolean ipV4Address = Utils.isIpV4Address("130.117.255.1");
        assertTrue(ipV4Address);
    }

    @Test
    void isIpv4_zeroes() {
        final boolean ipV4Address = Utils.isIpV4Address("0.0.0.0");
        assertTrue(ipV4Address);
    }

    @Test
    void isIpv6() {
        final boolean ipV4Address = Utils.isIpV4Address("2a07:a40::");
        assertFalse(ipV4Address);
    }

}