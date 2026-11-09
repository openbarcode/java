/**
 * Copyright 2013 Floms, LLC (Yoel Nunez <y.nunez@developers.floms.com>)
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

/**
 * UPC-A barcode. Accepts 11 digits (check digit is computed) or 12 digits
 * (check digit is verified).
 */
public final class UPC implements LinearBarcode {
    private static final String NAME = "UPC-A";

    private final String code;
    private final int[] bars;

    /**
     * @throws IllegalArgumentException if the code is not 11 or 12 digits, contains a
     *                                  non-digit, or has a wrong check digit
     */
    public UPC(String code) {
        this.code = Gs1.fullCode(code, NAME, 11, 12);
        // UPC-A is EAN-13 with a leading zero; the resulting modules are identical.
        this.bars = Gs1.ean13("0" + this.code);
    }

    @Override
    public int[] barcode() {
        return bars.clone();
    }

    @Override
    public String code() {
        return code;
    }
}
