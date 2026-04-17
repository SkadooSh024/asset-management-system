package com.assetmanagement.backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class CodeGeneratorUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private CodeGeneratorUtil() {
    }

    public static String generateFormCode(String prefix) {
        return prefix + "-" + LocalDateTime.now().format(FORMATTER) + "-" + ThreadLocalRandom.current().nextInt(100, 1000);
    }
}
