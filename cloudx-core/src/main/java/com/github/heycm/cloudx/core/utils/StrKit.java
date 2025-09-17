package com.github.heycm.cloudx.core.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 字符处理工具
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 18:27
 */
public class StrKit {

    private StrKit() {
    }

    /**
     * 格式化字符串，对模板中的 "{}" 按顺序填充参数
     * @param template 模板
     * @param args     参数
     * @return 格式化后的字符串
     */
    public static String format(String template, Object... args) {
        if (template == null || template.isEmpty()) {
            return "";
        }
        if (args == null || args.length == 0) {
            return template;
        }

        StringBuilder sb = new StringBuilder(template.length() + 32);

        final String placeholder = "{}"; // 占位符
        final int placeholderLength = placeholder.length();
        final int templateLength = template.length();

        int readPointer = 0; // 从左往右读 template 的游标
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            // 从游标位置开始查找占位符
            int placeholderIndex = template.indexOf(placeholder, readPointer);

            // 找不到占位符说明剩余部分无占位符
            if (placeholderIndex == -1) {
                // 游标没移动过，说明 template 中没有占位符
                if (readPointer == 0) return template;
                // 游标移动过，提取从游标位置的剩余部分加入返回结果
                sb.append(template, readPointer, templateLength);
                return sb.toString();
            }

            // 碰到转义字符: "\\{"
            if (placeholderIndex > 0 && template.charAt(placeholderIndex - 1) == '\\') {
                sb.append(template, readPointer, placeholderIndex - 1);
                sb.append(placeholder);
                readPointer = placeholderIndex + placeholderLength;
                argIndex--;
                continue;
            }

            // 正常站位替换
            sb.append(template, readPointer, placeholderIndex);
            sb.append(StrKit.toString(args[argIndex]));
            readPointer = placeholderIndex + placeholderLength;
        }

        // 参数替换完后加入剩余部分
        sb.append(template, readPointer, templateLength);
        return sb.toString();
    }

    private static String toString(Object obj) {
        return switch (obj) {
            case null -> null;
            case String s -> s;
            case byte[] bytes -> StrKit.bytesToString(bytes, StandardCharsets.UTF_8);
            case Byte[] bytes -> StrKit.bytesToString(bytes, StandardCharsets.UTF_8);
            case ByteBuffer bytes -> StrKit.bytesToString(bytes, StandardCharsets.UTF_8);
            case short[] shorts -> Arrays.toString(shorts);
            case int[] ints -> Arrays.toString(ints);
            case long[] longs -> Arrays.toString(longs);
            case float[] floats -> Arrays.toString(floats);
            case double[] doubles -> Arrays.toString(doubles);
            case boolean[] booleans -> Arrays.toString(booleans);
            case char[] chars -> Arrays.toString(chars);
            case Object[] objects -> Arrays.deepToString(objects);
            default -> obj.toString();
        };
    }

    private static String bytesToString(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }
        return charset == null ? new String(data) : new String(data, charset);
    }

    private static String bytesToString(Byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }
        byte[] bytes = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            Byte datum = data[i];
            bytes[i] = datum == null ? -1 : datum;
        }
        return StrKit.bytesToString(bytes, charset);
    }

    private static String bytesToString(ByteBuffer data, Charset charset) {
        if (data == null) {
            return null;
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }
}
