package com.company;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

    public static void main(String[] args) {

        byte[] plaintext = {0,1,0,1,0,1,0,1};
        byte[] rawkey = {1,1,1,0,0,0,1,1,1,0};
        //byte[] finalText = Encrypt(rawkey, plaintext);
        DES des1 = new DES(2,null, null);


        m("Completed Encryption: ");
        p(des1.Encrypt(rawkey,plaintext)); // Test case
        System.out.println("------------------");

        byte[] ciphertext = {1,0,0,1,0,0,0,0};
        byte[] rawkey2 = {0,0,1,0,0,1,1,1,1,1};

        //byte[] finalText2 = Decrypt(rawkey2, ciphertext);
        DES des2 = new DES(2, null, null);
        System.out.println("Completed Decryption: ");
        p(des2.Decrypt(rawkey2, ciphertext));


        m("----------------------");
        m("TripleSDES");
        byte[] r1 = {0,0,0,0,0,0,0,0,0,0};
        byte[] r2 = {0,0,0,0,0,0,0,0,0,0};
        byte[] pl = {0,0,0,0,0,0,0,0};
        DES des3 = new DES(3, r1, r2 );
        p(des3.TripleDESEncryption(pl));

    }


    public static void p(byte[] a) {
        for(byte i: a) {
            System.out.print(i);
        }
        System.out.println("");
    }
    // Input: String
    // Just print message on a new line.
    public static void m(String n){
        System.out.println(n);
    }


}
