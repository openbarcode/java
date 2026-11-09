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

import java.util.Objects;

/**
 * Encoding tables and helpers shared by the GS1 symbologies (UPC-A and EAN-13).
 * Internal to this library.
 */
final class Gs1 {
    /** Module count of a UPC-A or EAN-13 symbol: 3 + 42 + 5 + 42 + 3. */
    static final int MODULES = 95;

    // Digit encodings, 7 modules each, 1 = bar. Source: GS1 General Specifications.
    private static final String[] L = {
        "0001101", "0011001", "0010011", "0111101", "0100011",
        "0110001", "0101111", "0111011", "0110111", "0001011"
    };
    private static final String[] G = {
        "0100111", "0110011", "0011011", "0100001", "0011101",
        "0111001", "0000101", "0010001", "0001001", "0010111"
    };
    private static final String[] R = {
        "1110010", "1100110", "1101100", "1000010", "1011100",
        "1001110", "1010000", "1000100", "1001000", "1110100"
    };

    /**
     * EAN-13 left-half parity for the six digits after the leading digit,
     * indexed by the leading digit. L = odd (A) set, G = even (B) set.
     */
    private static final String[] PARITY = {
        "LLLLLL", "LLGLGG", "LLGGLG", "LLGGGL", "LGLLGG",
        "LGGLLG", "LGGGLL", "LGLGLG", "LGLGGL", "LGGLGL"
    };

    private static final String LEFT_GUARD = "101";
    private static final String CENTER_GUARD = "01010";
    private static final String RIGHT_GUARD = "101";

    private Gs1() {
    }

    /**
     * Validates {@code input} and returns the full code including its check digit.
     *
     * @param input         the code as supplied by the caller
     * @param name          symbology name used in error messages, e.g. "UPC-A"
     * @param payloadLength digits without a check digit
     * @param fullLength    digits including the check digit
     * @throws IllegalArgumentException if the length is wrong, a character is not a digit,
     *                                  or a supplied check digit does not match
     */
    static String fullCode(String input, String name, int payloadLength, int fullLength) {
        Objects.requireNonNull(input, "code must not be null");
        int n = input.length();
        if (n != payloadLength && n != fullLength) {
            throw new IllegalArgumentException(name + " code must be " + payloadLength + " or "
                    + fullLength + " digits, got " + n);
        }
        for (int i = 0; i < n; i++) {
            char c = input.charAt(i);
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException(name + " code must contain only digits 0-9, found '"
                        + c + "' in \"" + input + "\"");
            }
        }

        String payload = input.substring(0, payloadLength);
        int expected = checkDigit(payload);
        if (n == payloadLength) {
            return payload + expected;
        }

        int given = input.charAt(fullLength - 1) - '0';
        if (given != expected) {
            throw new IllegalArgumentException("Invalid check digit for " + name + " code \"" + input
                    + "\": expected " + expected + ", found " + given);
        }
        return input;
    }

    /**
     * GS1 mod-10 check digit. Digits in odd positions counted from the right
     * (the rightmost payload digit has weight 3) are weighted 3, the others 1.
     *
     * @param payload digits only, without the check digit
     */
    static int checkDigit(String payload) {
        int sum = 0;
        int len = payload.length();
        for (int i = 0; i < len; i++) {
            int digit = payload.charAt(i) - '0';
            sum += ((len - 1 - i) % 2 == 0) ? 3 * digit : digit;
        }
        return (10 - sum % 10) % 10;
    }

    /**
     * Encodes a 13-digit EAN-13 code (check digit included) into 95 modules.
     * UPC-A is encoded by passing it with a leading zero, which yields the same modules.
     */
    static int[] ean13(String code13) {
        String parity = PARITY[code13.charAt(0) - '0'];
        StringBuilder sb = new StringBuilder(MODULES);
        sb.append(LEFT_GUARD);
        for (int i = 1; i <= 6; i++) {
            int digit = code13.charAt(i) - '0';
            sb.append(parity.charAt(i - 1) == 'L' ? L[digit] : G[digit]);
        }
        sb.append(CENTER_GUARD);
        for (int i = 7; i <= 12; i++) {
            sb.append(R[code13.charAt(i) - '0']);
        }
        sb.append(RIGHT_GUARD);
        return toModules(sb);
    }

    /** Converts a string of '0' and '1' characters into an array of 0 and 1 ints. */
    static int[] toModules(CharSequence bits) {
        int[] modules = new int[bits.length()];
        for (int i = 0; i < modules.length; i++) {
            modules[i] = bits.charAt(i) == '1' ? 1 : 0;
        }
        return modules;
    }
}
