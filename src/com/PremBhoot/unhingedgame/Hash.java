package com.PremBhoot.unhingedgame;

import java.security.MessageDigest;

public class Hash {
    //used to convert into hexa decimal, for better storage
    private final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private byte[] hash;
    public Hash(){
    }
    public String generateHash(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            hash = digest.digest(input.getBytes());
            //uses messageDigest java security class to encrypt password with SHA-512 - use try catch

        } catch (Exception e) {
            e.printStackTrace();
        }
        //uses private method to convert to hexadecimal (watched video to do this)
        return bytesToStringHex(hash);
    }
    private String bytesToStringHex(byte[] bytes){
        char[] hexChars = new char[bytes.length * 2];
        for(int i = 0; i<bytes.length; i++){
            int v=bytes[i] & 0xFF;
            //represents hexidecimal (base 16) number system
            hexChars[i*2] = hexArray[ v >>> 4];
            hexChars[i*2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
