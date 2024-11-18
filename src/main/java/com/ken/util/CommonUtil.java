package com.ken.util;

import java.util.Collection;

public class CommonUtil {

    public static <E> boolean collectIsNotEmpty(Collection<E> collections) {
        return collections != null && !collections.isEmpty();
    }

}
