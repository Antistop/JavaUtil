package com.hanhan.javautil.check;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.hanhan.javautil.exception.BizError;
import com.hanhan.javautil.exception.BizException;
import com.hanhan.javautil.exception.Errors;

/**
 * @author pl
 */
public class CheckUtil {
    public static void assertTrue(boolean bool, String error) {
        if (!bool) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertTrue(boolean bool, BizException exception) {
        if (!bool) {
            throw exception;
        }
    }

    public static void assertFalse(boolean bool, String error) {
        if (bool) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertFalse(boolean bool, BizException exception) {
        if (bool) {
            throw exception;
        }
    }

    public static void assertGtZero(Integer obj, String error) {
        if (obj == null || obj <= 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertGtZero(Long obj, String error) {
        if (obj == null || obj <= 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertGtZero(BigDecimal obj, String error) {
        if (obj == null || obj.doubleValue() <= 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertGtZero(BigDecimal obj, Errors error) {
        if (obj == null || obj.doubleValue() <= 0) {
            throw new BizException(error);
        }
    }

    public static void assertGtZero(Double obj, String error) {
        if (obj == null || obj <= 0D) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertGteZero(Integer obj, String error) {
        if (obj == null || obj < 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertGteZero(BigDecimal obj, String error) {
        if (obj == null || obj.doubleValue() < 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertNotNull(Object obj, Errors error) {
        if (obj == null) {
            throw new BizException(error);
        }
    }

    public static void assertNotNull(Object obj, String error) {
        if (obj == null) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertNull(Object obj, String error) {
        if (obj != null) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertEmpty(Collection<?> coll, String error) {
        if (coll != null && coll.size() > 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertNotEmpty(Collection<?> coll, String error) {
        if (coll == null || coll.size() == 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertEmpty(Map<?, ?> map, String error) {
        if (map != null && map.size() > 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertNotEmpty(Map<?, ?> map, String error) {
        if (map == null || map.size() == 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertEmpty(String str, String error) {
        if (str != null && str.length() > 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertNotEmpty(String str, String error) {
        if (str == null || str.length() == 0) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertNotEmpty(BigDecimal str, String error) {
        if (str == null) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertNotEmpty(String str, Errors error) {
        if (str == null || str.length() == 0) {
            throw new BizException(error);
        }
    }

    public static void setDefault(Supplier<String> supplier, Consumer<String> consumer) {
        String value = supplier.get();
        if (value == null) {
            consumer.accept("");
        }
    }

    public static <T> void setDefault(Supplier<T> supplier, Consumer<T> consumer, T defaultValue) {
        T value = supplier.get();
        if (value == null) {
            consumer.accept(defaultValue);
        } else {
            consumer.accept(value);
        }
    }

    public static void assertNotEmpty(String str, Errors error, Object... params) {
        if (str == null || str.length() == 0) {
            throw new BizException(error, params);
        }
    }

    public static void assertIntervalValid(Long begin, Long end, long interval, String error) {
        if (begin == null || end == null || end < begin || (end - begin) > interval) {
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static void assertIntegerZero(Integer obj,String error){
        if(null != obj && obj <= 0){
            throw new BizException(BizError.PARAM_ERROR, error);
        }
    }

    public static String getOrDefault(String source, String df) {
        return source == null ? df : source;
    }
}
