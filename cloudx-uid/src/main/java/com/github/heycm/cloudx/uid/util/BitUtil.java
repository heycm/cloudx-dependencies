package com.github.heycm.cloudx.uid.util;

/**
 * 位运算工具
 * @author heycm
 * @version 1.0
 * @since 2025/10/12 0:55
 */
public class BitUtil {

    /**
     * 填充指定区间的位
     * @param target 目标值
     * @param value  填充值
     * @param start  区间起始位，从左侧开始，从0开始计数
     * @param end    区间结束位
     * @return 填充后的值
     */
    public static long fillBits(long target, long value, int start, int end) {
        int width = end - start + 1;
        long mask = ((1L << width) - 1) << start;   // 构造区间掩码
        target &= ~mask;                            // 清空目标区间
        target |= (value << start) & mask;          // 写入值
        return target;
    }

    public static void main(String[] args) {
        long target = 210811657914548225L;

        System.out.println("target bits = " + Long.toBinaryString(target));

        for (int i = 0; i < (1<<6); i++) {
            long x = fillBits(target, i, 17, 17+6);
            System.out.println("x = " + x + " bits = " + Long.toBinaryString(x));
        }
    }
}
