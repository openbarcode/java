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

import java.util.Locale;
import java.util.Objects;

/**
 * Code 39 barcode. Input is uppercased; the supported characters are
 * 0-9, A-Z, space, and - . $ / + %. Start and stop characters are added automatically,
 * and a one-module narrow space separates adjacent characters.
 */
public final class Code39 implements LinearBarcode {
    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%";

    // 9-of-12 patterns, one per character of CHARSET, 12 modules each (1 = bar).
    private static final String[] PATTERNS = {
        "101001101101", // 0
        "110100101011", // 1
        "101100101011", // 2
        "110110010101", // 3
        "101001101011", // 4
        "110100110101", // 5
        "101100110101", // 6
        "101001011011", // 7
        "110100101101", // 8
        "101100101101", // 9
        "110101001011", // A
        "101101001011", // B
        "110110100101", // C
        "101011001011", // D
        "110101100101", // E
        "101101100101", // F
        "101010011011", // G
        "110101001101", // H
        "101101001101", // I
        "101011001101", // J
        "110101010011", // K
        "101101010011", // L
        "110110101001", // M
        "101011010011", // N
        "110101101001", // O
        "101101101001", // P
        "101010110011", // Q
        "110101011001", // R
        "101101011001", // S
        "101011011001", // T
        "110010101011", // U
        "100110101011", // V
        "110011010101", // W
        "100101101011", // X
        "110010110101", // Y
        "100110110101", // Z
        "100101011011", // -
        "110010101101", // .
        "100110101101", // space
        "100100100101", // $
        "100100101001", // /
        "100101001001", // +
        "101001001001", // %
    };

    private static final String START_STOP = "100101101101"; // '*'

    private final String code;
    private final int[] bars;

    /**
     * @throws IllegalArgumentException if the input contains an unsupported character
     */
    public Code39(String data) {
        Objects.requireNonNull(data, "code must not be null");

        StringBuilder upper = new StringBuilder(data.length());
        for (int i = 0; i < data.length(); i++) {
            char c = Character.toUpperCase(data.charAt(i));
            if (CHARSET.indexOf(c) < 0) {
                throw new IllegalArgumentException("Code 39 does not support character '"
                        + data.charAt(i) + "' in \"" + data + "\"");
            }
            upper.append(c);
        }
        this.code = upper.toString();

        // Each symbol is 12 modules; a one-module narrow space separates adjacent symbols
        // (start, data..., stop). There is no gap before the start or after the stop.
        StringBuilder sb = new StringBuilder((code.length() + 2) * 13);
        sb.append(START_STOP);
        for (int i = 0; i < code.length(); i++) {
            sb.append('0').append(PATTERNS[CHARSET.indexOf(code.charAt(i))]);
        }
        sb.append('0').append(START_STOP);
        this.bars = Gs1.toModules(sb);
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
