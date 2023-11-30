package com.example.knu_administration_android.encrypt;

public class Encryption {

    public String Encoding(String password) {

        // Texts -> Octets -> Bits
        StringBuilder binaryPW = new StringBuilder();
        char[] p = password.toCharArray();

        for (int i = 0 ; i < p.length ; i++) {
            String tmpBinary = Integer.toBinaryString(p[i]);
            int paddingLength = 8 - tmpBinary.length();

            // 비트 수 조정 (한 문자 당 8bits)
            if (paddingLength > 0) tmpBinary = repeat("0", paddingLength) + tmpBinary;

            binaryPW.append(tmpBinary);
        }

        // padding 값 구하기 (bits -> sextets 변환 과정에서 6bits 씩 묶을 수 있도록 패딩 0을 추가)
        int paddingVal = 6 - (binaryPW.length() % 6);

        // Bits -> Sextets
        int sixPad = 0;
        if (paddingVal > 0) sixPad = 1;

        int[] sextetsPW = new int[(binaryPW.length() / 6) + sixPad];
        for (int i = 0 ; i < binaryPW.length() / 6 ; i++) {
            String tmp = binaryPW.substring(i * 6, (i + 1) * 6);
            sextetsPW[i] = Integer.parseInt(tmp, 2);
        }
        if (paddingVal > 0) {
            binaryPW.append(repeat("0", paddingVal));
            String tmp = binaryPW.substring(binaryPW.length() - 6, binaryPW.length());
            sextetsPW[sextetsPW.length - 1] = Integer.parseInt(tmp, 2);
        }

        // Sextets -> Character
        String indexTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        StringBuilder newPW = new StringBuilder();
        for (int dec : sextetsPW) {
            newPW.append(indexTable.charAt(dec));
        }

        // padding 2bits 당 한 개의 ?를 붙임
        if (paddingVal > 0) newPW.append(repeat("?", paddingVal / 2));

        return newPW.toString();
    }



    public String Decoding(String newPassword) {
        String indexTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        // padding 구하기 (encoding 시 추가한 ? 개수를 통해 비트 수 구함)
        int padding = 0;
        for (int i = newPassword.length() - 1 ; i >= 0 ; i--) {
            if (newPassword.charAt(i) == '?') padding++;
            else {
                padding *= 2;
                break;
            }
        }

        // Character -> Sextets
        int[] sextetsPW = new int[newPassword.length() - (padding / 2)];
        for (int i = 0 ; i < sextetsPW.length ; i++) {
            sextetsPW[i] = indexTable.indexOf(newPassword.charAt(i));
        }

        // Sextets -> Bits
        StringBuilder binaryPW = new StringBuilder();
        for (int i = 0 ; i < sextetsPW.length ; i++) {
            String tmpBin = Integer.toBinaryString(sextetsPW[i]);

            // rest는 앞에 붙는 0의 갯수 계산
            int rest = 6 - tmpBin.length();
            if (i == sextetsPW.length - 1 && padding > 0) {
                if(tmpBin.length() <= 1) {
                    binaryPW.append(repeat("0", rest));
                }
                else binaryPW.append(repeat("0", rest)).append(tmpBin.substring(0, tmpBin.length() - padding));
            } else {
                binaryPW.append(repeat("0", rest)).append(tmpBin);
            }
        }

        // Bits -> Octets -> Texts
        StringBuilder originText = new StringBuilder();
        for (int i = 0 ; i < binaryPW.length() - 8 ; i += 8) {
            String tmpBin = binaryPW.substring(i, i + 8);
            int charCode = Integer.parseInt(tmpBin, 2);
            originText.append((char) charCode);
        }

        return originText.toString();
    }

    // XOR operator
    public String XORoperator(String password) {
        byte[] key = { 4, 11 };

        byte[] passwordBytes = password.getBytes();
        byte[] result = new byte[password.length()];

        for(int i = 0 ; i < passwordBytes.length ; i++) {
            result[i] = (byte) (passwordBytes[i] ^ (key[(i + 1) % key.length]));
        }

        String xorResult = new String(result);

        return xorResult;
    }

    public static String repeat(String repeatString, int repeatCount) {
        StringBuilder result = new StringBuilder();
        for(int i = 0 ; i < repeatCount ; i++) {
            result.append(repeatString);
        }

        return result.toString();
    }
}
