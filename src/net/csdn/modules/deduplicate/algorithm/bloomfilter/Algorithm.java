package net.csdn.modules.deduplicate.algorithm.bloomfilter;

import net.csdn.modules.analyzer.mmseg4j.analysis.MMSegAnalyzer;

import java.util.*;

/**
 * User: william
 * Date: 12-4-13
 * Time: 上午11:49
 */
public class Algorithm {

    public static Comparator comparator = new Comparator<FixSizedBloomFilter>() {
        @Override
        public int compare(FixSizedBloomFilter o1, FixSizedBloomFilter o2) {
            byte[] o1_bytes = o1.bitSet;
            byte[] o2_bytes = o2.bitSet;
            int len = o1_bytes.length;
            for (int i = 0; i < len; i++) {
                for (int j = 7; j >= 0; j--) {
                    int o1_byte = ((o1_bytes[i] & ((byte) (1 << j))) == 0 ? 0 : 1);
                    int o2_byte = ((o2_bytes[i] & ((byte) (1 << j))) == 0 ? 0 : 1);
                    if (o1_byte == o2_byte) continue;
                    return o1_byte - o2_byte;
                }
            }
            return 0;
        }
    };

    public static <T> List<T> binarySearch(List<T> l, T key, Comparator<T> c) {
        int low = 0;
        int len = l.size() - 1;
        int high = l.size() - 1;
        List<T> results = new ArrayList<T>();

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = l.get(mid);
            int cmp = c.compare(midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else {
                results.add(l.get(mid));

                int range_low = mid;
                int range_high = mid;

                while (range_low > 0 && c.compare(l.get(--range_low), key) == 0) {
                    results.add(l.get(range_low));
                }
                while (range_high < len && c.compare(l.get(++range_high), key) == 0) {
                    results.add(l.get(range_high));
                }
                return results;
            }
        }
        return results;
    }


    public static int hammingDistanceSimilarity(byte[] a, byte[] b) {
        int bit_num = 0;
        int len = a.length;
        for (int i = 0; i < len; i++) {
            byte wow = (byte) (a[i] ^ b[i]);
            for (int j = 0; j < 8; j++) {
                if ((wow & (1 << j)) != 0) bit_num++;
            }
        }
        return bit_num;
    }


    public static void main(String[] args) {
        // String abc = "000010010000011100000000001000011000000000000000100100000000010000010000001011000000110000000011000100000001000000000000000000000000000000000101100000000000100010000000000000000001000000001000000001010011000010000000000000000000001000000000010010000000000000000000000000000000000010000000000001000000010010000000100100000000011000000101000000100001010000000010000000000000100000010000001000001000001000000000000000001000001000000000000000101000000000101000001000001000001000001000010000001000000000000100000000000010000000000000000000000000000000000000000000000000000000000000000001100000101001000000000000000000010000000000000000000000000000000000000000000000001000000000001000010010000000000000000100000000011000000000000000000000000100000000000000010000000000000000000000000000000000000010011101000000000000100001000000000000110000000000000000000000000000000000100000100000000000000000000000000000000000000000010000000010000010000000010001000000000100000100111000000000001000001000000000000110001010000000000000000001000000000000000000100010000010000100011000000100010000000000000000000000000000010000000100010100000000000001100010000010000000000000000001000000100010000000000100000000000000000100000001110000010011000000000000000000000000001000000000000000100000000000000100000100000100000000000000001000001010001110000000000000010000100000100000000000000000000100000010000000010000101000000000000000100001000000010000010000011000001010000000000000000000001000000000010000000000000000000000000001000001000100000000000000101000000000000000000000000000000000001000000000100100000000010000000000000010000000000000100000001000000000000000000000000000000000000000100000000000000000000000101000000011000000100000000000000000010010000000010000000000000000000101010000000000000000100001001010000000010001000001000100000000100100000000000010000000001001010000001000000000100000000000000010000000000000100000000000000000000000000000000000000001000000000100000000001000000000100000000011000100000100000000000000000000000000000000101000001000000000000000000001000000000000000000000000000100000000000000000000000100000000000000000000000000001000010000000000000000000100100000011100101000000010000001000000000001010001001000001000101000001000001000010000000000000000000000000000000000000000000000001000000000100010010000001000000000000000110000000000000000000000000000000000000000000001000000000000000000100011";
        // String bbc = "000000010000011100000000001000011000000000000000100100000000010000000000001011000000110000000011000100000001000000000000000000000000000000000101000000000000000010000000000000000001000000000000000000010001000011000000000000000000001000000000010010000000000000000000000000000000000010000000000001100000010010000000100100000000011000000101000000100001010000000010000000000000100000010000001000001000001000000000000011001000001000000000000000111000000000101000001000001000001000001000010000000000000000000100000000000010000000000000000100000000000010000000000000000000000000000000000001100000101001000000000000000000010000000000000000000000000000000000000001000000000000000000001000010010000000000000000100000000011000000000000000000000000100000000000000010000000000000000000000000000000000000010011101000000000000100001000000000000110000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000010000010000000000001000000000100000100111000000000001000000000000000000110001010000000000000000001000000000000000010100000000010000000011000000100000000000000000000000000000000010000000100010100000000000001100010000010000000000000000001000000100010001000000100000000000000000100000001110000010011000000000000000000000000001000000000000000100000000000000100000000000110000000001000001000011010001110000000000000010000100000100000000000000000000100000000000000010000101000000000000000100001000000010000010000011010000010000000000000000000001000000000010000000000000000000000000001010001000100000000000000101000000000000000000000000000000000001000000000100100000000010000000000000010000000000000100000001000000000000000000000000000000000000000100000000100000000000000101000000011000000100000000000000000010000000000010000000000000000000101010000000000000000100001001000000000010001000001000100000000000000000000000000000000101001010000001000000000100000000000000010000000000000100000000000000000000000000000000000000001000000000100000000001000000000100000000011000100000100000000000000000000000000000000001000001000000000000000000001000000000000001000000000000100000000000000000000000100000000010000000000000000001000010000000000000000000100000000011000101000000010001001000000000001010001001000001000101000001000001000010000000000000000000000000000000000000010000000001000000000100010010000000000000000000000110000000000000000000000000000000000000000000001000000000000000000100011";
//        System.out.println(abc);
//        printByte(binaryStrToBytes(abc));

//        System.out.println(hammingDistanceSimilarity(binaryStrToBytes(abc), binaryStrToBytes(bbc)));
//        byte[] wow = binaryStrToBytes(abc);
//        byte[] wow2 = new byte[300];
//        List<byte[]> wow3 = new ArrayList<byte[]>();
//        print(wow.length);
//        int countj = 0;
//        for (int i = 0; i < 60; i++) {
//            byte[] wowm = substituteBits(wow, i, 60);
//            print("="+wowm.length);
//            for (byte b : wowm) {
//                wow2[countj++] = b;
//            }
//        }
        //     print(hammingDistanceSimilarity(binaryStrToBytes(abc),binaryStrToBytes(bbc)));
        String abc = "11111111";
        print(binaryStrToBytes(abc)[0]);
    }


    public static void print(Object input) {
        System.out.println(input);
    }

    public static byte[] substituteBits(byte[] bytes, int k, int num) {
        int len = bytes.length;
        int slop = len / num;
        byte[] byte_copy = new byte[slop];
        int inc = 0;
        for (int i = k * slop; i < (k + 1) * slop; i++) {
            byte_copy[inc++] = bytes[i];
        }
        return byte_copy;
    }

    public static String printByte(byte[] a) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < 8; j++) {
                if ((a[i] & (1 << (7 - j))) != 0) {
                    stringBuffer.append(1);
                } else {
                    stringBuffer.append(0);
                }
            }
        }
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();

    }

    public static byte[] binaryStrToBytes(String str) {

        int len = str.length() / 8;
        byte[] results = new byte[len];
        for (int i = 0; i < len; i++) {
            String wow = str.substring(i * 8, (i + 1) * 8);
            byte temp = (byte) 0;
            for (int j = 0; j < 8; j++) {
                if (wow.charAt(j) == '1') {
                    temp = (byte) (temp | (1 << (7 - j)));
                }
            }
            results[i] = temp;
        }
        return results;
    }

//    public static String stripHTMLTag(String input) {
//        if (input == null) return "";
//        return input.replaceAll("</?\\w++[^>]*+>", " ");
//    }

    public static String[] chunking(MMSegAnalyzer mmSegAnalyzer, String a) {
        //a = stripHTMLTag(a);
        if (a == null) return new String[0];
        String[] blocks = mmSegAnalyzer.toWhiteSpaceString(a).split("\\s+");
        Set<String> word_wow = new HashSet<String>();
        for (String block : blocks) {
            if (block == null || block.isEmpty() || block.length() < 2) continue;
            word_wow.add(block);
        }

        String[] output = new String[word_wow.size()];
        word_wow.toArray(output);
        return output;
    }
}
