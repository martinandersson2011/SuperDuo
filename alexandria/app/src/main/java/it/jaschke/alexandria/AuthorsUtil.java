package it.jaschke.alexandria;

import android.text.TextUtils;

/**
 * Created by martin.andersson on 12/4/15.
 */
public final class AuthorsUtil {

    private AuthorsUtil() {
        // Hidden constructor
    }

    public static String[] getAuthorArray(String authors) {
        String[] authorsArr;
        if (TextUtils.isEmpty(authors)) {
            authorsArr = new String[1];
            authorsArr[0] = "Unknown author";
        } else {
            authorsArr = authors.split(",");
        }
        return authorsArr;
    }
}
