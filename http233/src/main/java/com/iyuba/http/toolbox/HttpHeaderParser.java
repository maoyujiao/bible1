package com.iyuba.http.toolbox;

import com.iyuba.http.LOGUtils;

import java.util.Map;

/**
 * Utility methods for parsing HTTP headers.
 */
public class HttpHeaderParser {
    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * Returns the charset specified in the Content-Type of this header, or the
     * HTTP default (utf-8) if none can be found.
     */
    public static String parseCharset(Map<String, String> headers) {
        String contentType = headers.get("Content-Type");
        if (contentType != null) {
            String[] params = contentType.split(";");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2 && pair[0].equals("charset")) {
                    LOGUtils.e("http233", "charset parsed from header : " + pair[1]);
                    return pair[1];
                }
            }
        }

        return DEFAULT_CHARSET;
    }
}
