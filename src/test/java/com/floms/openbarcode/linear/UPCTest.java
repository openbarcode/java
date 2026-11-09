/**
 * Copyright 2013 Floms, LLC (Yoel Nunez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.floms.openbarcode.linear;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UPCTest {

    static final String EXPECTED_MODULES = "10100011010111101010111100011010001101000110101010110110011101001100110101110010011101101100101";

    static String modules(int[] bits) {
        StringBuilder sb = new StringBuilder(bits.length);
        for (int b : bits) {
            sb.append(b);
        }
        return sb.toString();
    }

    @Test
    void elevenDigitsGetCheckDigitAndExactModules() {
        UPC upc = new UPC("03600029145");
        assertEquals("036000291452", upc.code());
        assertEquals(95, upc.barcode().length);
        assertEquals(EXPECTED_MODULES, modules(upc.barcode()));
    }

    @Test
    void twelveDigitsWithCorrectCheckDigitGiveSameModules() {
        UPC upc = new UPC("036000291452");
        assertEquals("036000291452", upc.code());
        assertEquals(EXPECTED_MODULES, modules(upc.barcode()));
    }

    @Test
    void wrongCheckDigitIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new UPC("036000291453"));
        assertEquals("Invalid check digit for UPC-A code \"036000291453\": expected 2, found 3",
                e.getMessage());
    }

    @Test
    void nonDigitIsRejectedWithUpcMessage() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new UPC("03600A29145"));
        assertTrue(e.getMessage().startsWith("UPC-A code must contain only digits"), e.getMessage());
    }

    @Test
    void wrongLengthIsRejectedWithUpcMessage() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new UPC("0360002914"));
        assertEquals("UPC-A code must be 11 or 12 digits, got 10", e.getMessage());
    }

    @Test
    void thirteenDigitsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new UPC("0360002914523"));
    }
}
