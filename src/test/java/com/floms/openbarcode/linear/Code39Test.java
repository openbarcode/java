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

class Code39Test {

    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%";

    // Published Code 39 narrow/wide patterns (n = narrow, w = wide), aligned with CHARSET.
    private static final String[] NW = {"nnnwwnwnn", "wnnwnnnnw", "nnwwnnnnw", "wnwwnnnnn", "nnnwwnnnw", "wnnwwnnnn", "nnwwwnnnn", "nnnwnnwnw", "wnnwnnwnn", "nnwwnnwnn", "wnnnnwnnw", "nnwnnwnnw", "wnwnnwnnn", "nnnnwwnnw", "wnnnwwnnn", "nnwnwwnnn", "nnnnnwwnw", "wnnnnwwnn", "nnwnnwwnn", "nnnnwwwnn", "wnnnnnnww", "nnwnnnnww", "wnwnnnnwn", "nnnnwnnww", "wnnnwnnwn", "nnwnwnnwn", "nnnnnnwww", "wnnnnnwwn", "nnwnnnwwn", "nnnnwnwwn", "wwnnnnnnw", "nwwnnnnnw", "wwwnnnnnn", "nwnnwnnnw", "wwnnwnnnn", "nwwnwnnnn", "nwnnnnwnw", "wwnnnnwnn", "nwwnnnwnn", "nwnwnwnnn", "nwnwnnnwn", "nwnnnwnwn", "nnnwnwnwn"};

    @Test
    void codeThirtyNineExactModules() {
        Code39 code = new Code39("CODE39");
        assertEquals("CODE39", code.code());
        String modules = UPCTest.modules(code.barcode());
        assertEquals(103, modules.length());
        assertEquals("100101101101011011010010101101", modules.substring(0, 30));
        assertEquals("010101011001011010100101101101", modules.substring(modules.length() - 30));
        assertEquals("1001011011010110110100101011010110100101010110010110110101100101011011001010101011001011010100101101101", modules);
    }

    @Test
    void helloWorldWithSpaceExactModules() {
        Code39 code = new Code39("HELLO WORLD");
        String modules = UPCTest.modules(code.barcode());
        assertEquals(168, modules.length());
        assertEquals("100101101101011010100110101101", modules.substring(0, 30));
        assertEquals("001101010110010110100101101101", modules.substring(modules.length() - 30));
        assertEquals("100101101101011010100110101101011001010101101010011010110101001101101011010010100110101101011001101010101101011010010110101011001010110101001101010110010110100101101101", modules);
    }

    @Test
    void lowercaseInputIsUppercased() {
        Code39 lower = new Code39("code39");
        assertEquals("CODE39", lower.code());
        assertEquals(UPCTest.modules(new Code39("CODE39").barcode()), UPCTest.modules(lower.barcode()));
    }

    @Test
    void unsupportedCharacterThrowsWithCharacterInMessage() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Code39("AB@C"));
        assertTrue(e.getMessage().contains("'@'"), e.getMessage());
    }

    @Test
    void asteriskIsReservedForStartStop() {
        assertThrows(IllegalArgumentException.class, () -> new Code39("*"));
        assertThrows(IllegalArgumentException.class, () -> new Code39("A*B"));
    }

    @Test
    void letterlikeNonAsciiCharactersAreRejected() {
        // Uppercasing is per character, so 'sharp s' (which has no single-char uppercase) stays unsupported.
        assertThrows(IllegalArgumentException.class, () -> new Code39("\u00DF"));
        assertThrows(IllegalArgumentException.class, () -> new Code39("a\u00E9"));
    }

    @Test
    void everyPatternMatchesPublishedNarrowWideTable() {
        // Each character is wrapped in start/stop with a one-module gap: modules [13,25) hold the character.
        for (int i = 0; i < CHARSET.length(); i++) {
            char c = CHARSET.charAt(i);
            String modules = UPCTest.modules(new Code39(String.valueOf(c)).barcode());
            assertEquals(NW[i], toNarrowWide(modules.substring(13, 25)), "character " + c);
        }
    }

    @Test
    void startStopPatternMatchesPublishedTable() {
        String modules = UPCTest.modules(new Code39("").barcode());
        assertEquals(25, modules.length());
        assertEquals("nwnnwnwnn", toNarrowWide(modules.substring(0, 12)));
        assertEquals("0", modules.substring(12, 13));
        assertEquals("nwnnwnwnn", toNarrowWide(modules.substring(13, 25)));
    }

    /** Converts a 12-module pattern to n/w form: a run of 1 module is n, a run of 2 modules is w. */
    static String toNarrowWide(String bits) {
        StringBuilder sb = new StringBuilder(9);
        int i = 0;
        while (i < bits.length()) {
            int j = i;
            while (j < bits.length() && bits.charAt(j) == bits.charAt(i)) {
                j++;
            }
            sb.append(j - i == 1 ? 'n' : 'w');
            i = j;
        }
        return sb.toString();
    }
}
