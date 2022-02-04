package xyz.jocn.shortenurl.core;

public class Base62 {

    private static final String BASE62_INLINE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final char[] BASE62_TABLE = BASE62_INLINE.toCharArray();

    /**
     * RADIX: 숫자 표현(진법체계)에 기준이 되는 수
     */
    private static final int RADIX = BASE62_TABLE.length;

    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int index = (int) (num % RADIX);
            sb.append(BASE62_TABLE[index]);
            num /= RADIX;
        }
        return sb.toString();
    }

    public static long decode(String encoded) {
        long origin = 0;
        long power = 1;
        for (int i = 0; i < encoded.length(); i++) {
            origin += BASE62_INLINE.indexOf(encoded.charAt(i)) * power;
            power *= RADIX;
        }
        return origin;
    }
}
