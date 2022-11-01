package example.testing

import groovy.transform.CompileStatic
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang.math.RandomUtils

import static java.util.Collections.EMPTY_MAP

@CompileStatic
class Generators {
    static String anyString(int size = 10) {
        RandomStringUtils.randomAlphanumeric(size)
    }

    static Integer anyInt(int size = Integer.MAX_VALUE) {
        RandomUtils.nextInt(size)
    }

    static Long anyLong() {
        RandomUtils.nextLong()
    }

    static String anyEmail(Map overrides = EMPTY_MAP) {
        def values = [
                recipient: anyString(6),
                domain   : "${anyString()}.com"
        ] + overrides
        "${values.recipient}@${values.domain}"
    }

    static <T> T pick(Collection<? extends T> from) {
        from[anyInt(from.size())]
    }

    static <K, V> Map.Entry<K, V> pick(Map<K, V> from) {
        pick(from.entrySet())
    }
}
