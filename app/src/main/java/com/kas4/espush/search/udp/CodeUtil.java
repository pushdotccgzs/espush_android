package com.kas4.espush.search.udp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/29.
 */
public class CodeUtil {

    private static int iAt(String s, int index) {
        if (index < s.length()) {
            return (((int) s.charAt(index)) & 0xff);
        } else {
            return -1;
        }

    }

    public static List<Byte> asciiToBytes(String ascii, int fillMax) {

        int val1, val2, val3;
        char c1, c2;

        List<Byte> bytes = new ArrayList<Byte>();

        ascii = ascii.replace("\\r", "\\0d");
        ascii = ascii.replace("\\n", "\\0a");

        for (int i = 0; i < ascii.length(); i++) {
            val1 = iAt(ascii, i);
            if (val1 >= 0x20 && val1 <= 0x7E) {

                if (val1 == (((int) '\\') & 0xff)) {
                    val2 = iAt(ascii, i + 1);
                    val3 = iAt(ascii, i + 2);
                    if (val2 > -1 && val3 > -1) {
                        c1 = ascii.charAt(i + 1);
                        c2 = ascii.charAt(i + 2);


                        try {

                            val2 = Integer.parseInt((c1 + "") + (c2 + ""), 16) & 0xff;
                            val3 = 0;


                            bytes.add((byte) (val2 + val3));

                        } catch (NumberFormatException e) {


                        }

                        i += 2;
                        continue;
                    }

                } else {
                    val1 = iAt(ascii, i);
                    bytes.add((byte) (val1));

                }


            }

        }

        for (int i = bytes.size(); i < fillMax; i++) {
            bytes.add((byte) 0x00);
        }

        return bytes;
    }

    public byte[] fill(byte[] b, int max) {
        byte[] res = new byte[max];
        int i = 0;
        for (; i < max && i < b.length; i++) {
            res[i] = b[i];
        }
        for (; i < max; i++) {
            res[i] = 0x00;
        }
        return res;
    }

    public static byte[] asciiToBytes(String ascii) {

        int val1, val2, val3;
        char c1, c2;

        List<Byte> bytes = new ArrayList<Byte>();

        ascii = ascii.replace("\\r", "\\0d");
        ascii = ascii.replace("\\n", "\\0a");

        for (int i = 0; i < ascii.length(); i++) {
            val1 = iAt(ascii, i);
            if (val1 >= 0x20 && val1 <= 0x7E) {

                if (val1 == (((int) '\\') & 0xff)) {
                    val2 = iAt(ascii, i + 1);
                    val3 = iAt(ascii, i + 2);
                    if (val2 > -1 && val3 > -1) {
                        c1 = ascii.charAt(i + 1);
                        c2 = ascii.charAt(i + 2);


                        try {

                            val2 = Integer.parseInt((c1 + "") + (c2 + ""), 16) & 0xff;
                            val3 = 0;


                            bytes.add((byte) (val2 + val3));

                        } catch (NumberFormatException e) {


                        }

                        i += 2;
                        continue;
                    }

                } else {
                    val1 = iAt(ascii, i);
                    bytes.add((byte) (val1));

                }


            }

        }

        return toByteArray(bytes);
    }

    public static byte[] toByteArray(List<Byte> list) {
        byte[] ret = new byte[list.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = list.get(i);
        return ret;
    }


    public static String toAscii(byte[] data) {
        String returnString = "";
        for (int item : data) {
            if (item == 0x0A) {
                returnString = returnString + "\\n";

            } else if (item == 0x0D) {
                returnString = returnString + "\\r";

            } else if (item >= 0x20 && item <= 0x7E) {
                returnString = returnString + (char) item;
            } else {
                String hex = Integer.toHexString(item & 0xff);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                returnString = returnString + "\\" + hex;
            }
        }

        return returnString;
    }


    public static List<Byte> intToByte(int iSource, int fillMax) {
        List<Byte> bytes = new ArrayList<Byte>();
        for (int i = 0; (i < 4) && (i < fillMax); i++) {
            bytes.add((byte) (iSource >> (8 * (fillMax - 1 - i)) & 0xFF));
        }
        for (int i = bytes.size(); i < fillMax; i++) {
            bytes.add((byte) 0x00);
        }
        return bytes;
    }


}
