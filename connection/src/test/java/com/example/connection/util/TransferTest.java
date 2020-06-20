package com.example.connection.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    @Test
    void transferBToI() {
        assertEquals(6, Transfer.transferBToI("0110"));
    }

    @Test
    void transferIToB() {
        assertEquals("00000110", Transfer.transferIToB(6, 8));
    }

    @Test
    void transferCToB() {
        assertEquals("00110000", Transfer.transferCToB('0', 8));
    }

    @Test
    void transferSToB() {
        assertEquals("001100000011000100110010", Transfer.transferSToB("012"));
    }

    @Test
    void f() {
        assertEquals("1111", Transfer.f('F'));
    }

}