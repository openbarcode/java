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
 * EAN-13 barcode. Accepts 12 digits (check digit is computed) or 13 digits
 * (check digit is verified).
 */
public final class EAN implements LinearBarcode {
    private static final String NAME = "EAN-13";

    private final String code;
    private final int[] bars;

    /**
     * @throws IllegalArgumentException if the code is not 12 or 13 digits, contains a
     *                                  non-digit, or has a wrong check digit
     */
    public EAN(String code) {
        this.code = Gs1.fullCode(code, NAME, 12, 13);
        this.bars = Gs1.ean13(this.code);
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
