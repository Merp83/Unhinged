package com.PremBhoot.unhingedgame;

import java.security.MessageDigest;

public class Hash {
    private final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private byte[] hash;
    public Hash(){
    }
    public String generateHash(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            hash = digest.digest(input.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytesToStringHex(hash);
    }
    public String bytesToStringHex(byte[] bytes){
        char[] hexChars = new char[bytes.length * 2];
        for(int i = 0; i<bytes.length; i++){
            int v=bytes[i] & 0xFF;
            hexChars[i*2] = hexArray[ v >>> 4];
            hexChars[i*2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
