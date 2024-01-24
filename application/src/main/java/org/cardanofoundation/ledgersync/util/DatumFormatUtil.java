package org.cardanofoundation.ledgersync.util;

import org.springframework.util.ObjectUtils;

public final class DatumFormatUtil {

    private DatumFormatUtil() {

    }

    public static String datumJsonRemoveSpace(String json) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }

        return json.replace(" ", "");
    }
}
