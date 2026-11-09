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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

/**
 * Regression test: barcode instances must not share mutable state. The Python
 * port once kept a class-level list that every instance appended to.
 */
class IndependenceTest {

    @Test
    void twoUpcInstancesDoNotShareModules() {
        UPC first = new UPC("03600029145");
        int[] before = first.barcode();
        UPC second = new UPC("04900000019");
        assertArrayEquals(before, first.barcode());
        assertArrayEquals(new UPC("03600029145").barcode(), first.barcode());
        assertNotSame(first.barcode(), second.barcode());
    }

    @Test
    void mutatingReturnedArrayDoesNotChangeBarcode() {
        EAN ean = new EAN("400638133393");
        int[] expected = new EAN("400638133393").barcode();
        int[] copy = ean.barcode();
        for (int i = 0; i < copy.length; i++) {
            copy[i] = 1 - copy[i];
        }
        assertArrayEquals(expected, ean.barcode());
    }

    @Test
    void twoCode39InstancesAreIndependent() {
        Code39 a = new Code39("CODE39");
        int[] aBefore = a.barcode();
        Code39 b = new Code39("HELLO WORLD");
        assertArrayEquals(aBefore, a.barcode());
        assertArrayEquals(new Code39("HELLO WORLD").barcode(), b.barcode());
    }
}
