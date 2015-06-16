package com.xqbase.baiji.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Utility methods used for generic object.
 *
 * @author Tony He
 */
public class ObjectUtil {

    public static int hashCode(Object obj) {
        return obj != null ? obj.hashCode() : 0;
    }

    public static boolean equals(Map<?, ?> map1, Map<?, ?> map2) {
        if (null == map1) {
            return null == map2;
        }
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Map.Entry<?, ?> entry1 : map1.entrySet()) {
            if (!map2.containsKey(entry1.getKey())) {
                return false;
            }
            if (!equals(entry1.getValue(), map2.get(entry1.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Object obj1, Object obj2) {
        if (null == obj1) {
            return null == obj2;
        }
        if (obj1 instanceof byte[] && obj2 instanceof byte[]) {
            return Arrays.equals((byte[]) obj1, (byte[]) obj2);
        } else if (obj1 instanceof Object[] && obj2 instanceof Object[]) {
            return Arrays.deepEquals((Object[]) obj1, (Object[]) obj2);
        } else {
            return obj1.equals(obj2);
        }
    }

    public static boolean equals(List<?> list1, List<?> list2) {
        if (null == list1)
            return null == list2;

        if (list1.size() != list2.size())
            return false;

        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i) != list2.get(i))
                return false;
        }

        return true;
    }
}
