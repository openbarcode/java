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

import org.junit.jupiter.api.Test;

class EANTest {

    private static final String EXPECTED_MODULES = "10100011010100111010111101111010001001011001101010100001010000101000010111010010000101100110101";

    @Test
    void twelveDigitsGetCheckDigitAndExactModules() {
        EAN ean = new EAN("400638133393");
        assertEquals("4006381333931", ean.code());
        assertEquals(95, ean.barcode().length);
        assertEquals(EXPECTED_MODULES, UPCTest.modules(ean.barcode()));
    }

    @Test
    void thirteenDigitsWithCorrectCheckDigitGiveSameModules() {
        EAN ean = new EAN("4006381333931");
        assertEquals("4006381333931", ean.code());
        assertEquals(EXPECTED_MODULES, UPCTest.modules(ean.barcode()));
    }

    @Test
    void wrongCheckDigitIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new EAN("4006381333932"));
        assertEquals("Invalid check digit for EAN-13 code \"4006381333932\": expected 1, found 2",
                e.getMessage());
    }

    @Test
    void nonDigitIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new EAN("40063813339X"));
        assertThrows(IllegalArgumentException.class, () -> new EAN("4006381333 1"));
    }

    @Test
    void wrongLengthIsRejectedWithEanMessage() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new EAN("40063813339"));
        assertEquals("EAN-13 code must be 12 or 13 digits, got 11", e.getMessage());
    }

    @Test
    void leadingDigitFiveUsesLGGLLGParity() {
        // 5901234123457 is the GS1 example for leading digit 5 (left parity LGGLLG).
        EAN ean = new EAN("5901234123457");
        assertEquals("5901234123457", ean.code());
        assertEquals("10100010110100111011001100100110111101001110101010110011011011001000010101110010011101000100101", UPCTest.modules(ean.barcode()));
    }
}
