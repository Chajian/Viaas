package com.viaas.docker.util;

/**
 * @Description: 生成字符串随机数（转为Long型）
 * @auther: sn
 * @Date:
 */
public class RandomAccountUtil {
    private static final String DEFAULT_DIGITS = "0";
    private static final String FIRST_DEFAULT_DIGITS = "1";

    /**
     * @param target 目标数字
     * @param length 需要补充到的位数, 补充默认数字[0], 第一位默认补充[1]
     * @return 补充后的结果
     */
    public static String makeUpNewData(String target, int length) {
        return makeUpNewData(target, length, DEFAULT_DIGITS);
    }

    /**
     * @param target 目标数字
     * @param length 需要补充到的位数
     * @param add    需要补充的数字, 补充默认数字[0], 第一位默认补充[1]
     * @return 补充后的结果
     */
    public static String makeUpNewData(String target, int length, String add) {
        if (target.startsWith("-")) target.replace("-", "");
        if (target.length() >= length) return target.substring(0, length);
        StringBuffer sb = new StringBuffer(FIRST_DEFAULT_DIGITS);
        for (int i = 0; i < length - (1 + target.length()); i++) {
            sb.append(add);
        }
        return sb.append(target).toString();
    }

    /**
     * 生产一个随机的指定位数的字符串数字
     *
     * @param length
     * @return
     */
    public static String randomDigitNumber(int length) {
        int start = Integer.parseInt(makeUpNewData("", length));//1000+8999=9999
        int end = Integer.parseInt(makeUpNewData("", length + 1)) - start;//9000
        return (int) (Math.random() * end) + start + "";
    }

    /**
     * 生成10位随机数 numberNine 与 numberOne字符拼接后转换成Long型
     *
     * @param: nine  不能大于9
     * @param: one   不能大于1
     * @return: java.lang.Long
     * @author: sn
     */
    public static Long randomNumberIsTen(int numberNine, int numberOne) throws Exception {
        if (numberNine != 9) {
            Exception exception = new Exception("错误：numberNine必须为9");
            throw exception;
        } else if (numberOne != 1) {
            Exception exception = new Exception("错误：numberOne必须为1");
            throw exception;
        }
        String num1 = RandomAccountUtil.randomDigitNumber(numberNine);
        String num2 = RandomAccountUtil.randomDigitNumber(numberOne);
        String num3 = num1 + num2;
        String num4;
        Long num5;
        Long num = Long.parseLong(num3);
        while (num > 9999999999L || num < 9000000000L) {
            num1 = RandomAccountUtil.randomDigitNumber(numberNine);
            num2 = RandomAccountUtil.randomDigitNumber(numberOne);
            num4 = num1 + num2;
            num5 = Long.parseLong(num4);
            num = num5;
        }
        return num;

    }
}
