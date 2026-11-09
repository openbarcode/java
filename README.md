<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="assets/branding/open-barcode-logo-dark.svg">
    <img src="assets/branding/open-barcode-logo.svg" alt="Open Barcode by Floms" width="420">
  </picture>
</p>

<p align="center"><em>Barcodes, made open.</em></p>

# Open Barcode for Java

Barcode generation library for Java (Code 39, EAN, UPC).

Install
-------

Maven:

```xml
<dependency>
  <groupId>dev.openbarcode</groupId>
  <artifactId>openbarcode</artifactId>
  <version>2.0.0</version>
</dependency>
```

Gradle:

```groovy
implementation 'dev.openbarcode:openbarcode:2.0.0'
```

Open Barcode has no runtime dependencies and targets Java 8 bytecode.

Usage
-----

Each barcode is built from its code string. `barcode()` returns the module
sequence, one `int` per module: `1` is a bar and `0` is a space. `code()`
returns the code text, including the check digit.

```java
import com.floms.openbarcode.linear.Code39;
import com.floms.openbarcode.linear.EAN;
import com.floms.openbarcode.linear.UPC;

UPC upc = new UPC("03600029145");      // 11 digits: check digit is computed
upc.code();                             // "036000291452"
int[] modules = upc.barcode();          // 95 modules

EAN ean = new EAN("4006381333931");    // 13 digits: check digit is verified
Code39 code39 = new Code39("hello");    // uppercased to "HELLO"
```

- `UPC` accepts 11 digits (UPC-A without a check digit) or 12 digits (with it).
- `EAN` accepts 12 digits (EAN-13 without a check digit) or 13 digits (with it).
- A supplied check digit is verified; a mismatch throws `IllegalArgumentException`.
- `Code39` accepts 0-9, A-Z, space, and `- . $ / + %`. Other characters throw `IllegalArgumentException`.
- The module sequence has no quiet zones. `Code39` includes one narrow space between adjacent characters, including the start and stop characters.

Testing
-------

The project uses Maven and JUnit 5. To build and run the tests:

```sh
mvn -B verify
```

The tests check exact module sequences for known codes, check-digit
computation, invalid input, and that barcode instances do not share state.

Open Barcode is available in several languages:

- Java: https://github.com/openbarcode/java
- C#: https://github.com/openbarcode/csharp
- Python: https://github.com/openbarcode/python
- PHP: https://github.com/openbarcode/php
- JavaScript: https://github.com/openbarcode/javascript

Project homepage: https://openbarcode.dev

This repository was split from the original multi-language project at
https://github.com/floms/Open-Barcode. The commit history was preserved
with git-filter-repo; commit hashes differ from the original.

Copyright 2013 Floms, LLC (Yoel Nunez)

Copyright 2026 Floms, Inc.

Licensed under the Apache License, Version 2.0. See LICENSE.md for details.
