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

/**
 * A one-dimensional barcode whose symbol is available as a sequence of modules.
 */
public interface LinearBarcode {
    /**
     * Returns the module sequence, one entry per module: {@code 1} for a bar and
     * {@code 0} for a space. The array is a copy; changing it does not affect this barcode.
     */
    int[] barcode();

    /**
     * Returns the normalized code text encoded in the barcode, including any check digit.
     */
    String code();
}
