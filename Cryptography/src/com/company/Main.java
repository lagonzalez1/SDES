package com.company;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Main {


    public static int rounds = 2;
    public static byte[][] s0 = new byte[4][4];
    public static byte[][] s1 = new byte[4][4];
    public static byte[] key1;

    public static void main(String[] args) {
        fillMatrix();
        byte[] plaintext = {0,1,0,1,0,1,0,1};
        byte[] rawkey = {1,1,1,0,0,0,1,1,1,0};

        p(Encrypt(rawkey, plaintext)); // Test case
    }


    public static byte[] combineBytes(byte[] a, byte[] b) {
        byte[] combined = new byte[a.length + b.length];
        for (int i = 0; i < a.length; i ++) {
            combined[i] = a[i];
        }
        int index = 0;
        for(int i = a.length; i < combined.length; i ++) {
            combined[i] = b[index];
            index++;
        }
        return combined;
    }


    // Convert binary to int, we assume max size 2.
    // Argument must be len of 2
    public static int convertBinaryInt(byte[] arr) {
        int number = 0;
        if(arr.length != 2) {return -1;}

        int k = 0;
        for(int i = arr.length - 1; i >= 0; i--) {
            if(arr[i] == 1) {number += Math.pow(2,k); k++;}
            else {k++;}
        }
        return number;
    }

    // Final step
    public static byte[] getP4(byte[] left, byte[] right) {
        byte[] encrypted = null;
        byte[] leftValueRow = {left[0],left[3]};
        byte[] leftValueCol = {left[1],left[2]};
        int leftRow = convertBinaryInt(leftValueRow);
        int leftCol = convertBinaryInt(leftValueCol);

        byte[] rightValueRow = {right[0],right[3]};
        byte[] rightValueCol = {right[1],right[2]};
        int rightRow = convertBinaryInt(rightValueRow);
        int rightCol = convertBinaryInt(rightValueCol);

        byte[] leftBinary = convertIntBinary(searchMatrix(s0,leftRow, leftCol));
        byte[] rightBinary = convertIntBinary(searchMatrix(s1,rightRow, rightCol));
        encrypted = p4(leftBinary, rightBinary);
        return encrypted;
    }

    // Convert Int to binary
    // Note: this will work for small numbers since we assume the first 2 are the bits we want.
    // Note: The order is revered, but that not important right now.
    public static byte[] convertIntBinary(int number) {
        boolean[] converted = new boolean[8];
        byte[] convertedBinary = new byte[8];
        for (int i = 6; i >= 0; i--) {
            converted[i] = (number & (1 << i)) != 0;
        }
        int k = 0;
        for(int i = converted.length - 1; i >= 0; i--) {
            if( !converted[i] ) {
                convertedBinary[k] = 0;
                k++;
            }else {
                convertedBinary[k] = 1;
                k++;
            }
        }
        // Remove all leading zeros keep last 2
        byte[] removeZero = {convertedBinary[6], convertedBinary[7]};
        return removeZero;
    }

    public static int searchMatrix(byte[][] matrix, int row, int col) {
        int number = matrix[row][col];
        return number;
    }

    public static void p(byte[] a) {
        for(byte i: a) {
            System.out.print(i);
        }
        System.out.println("");
    }

    public static void m(String n){
        System.out.println(n);
    }

    public static byte[] Encrypt(byte[] rawkey, byte[] plaintext) {
        byte[] bit = null;
        Keys k = new Keys(rawkey);
        byte[] ip = ip(plaintext);

        for(int i = 1; i <= rounds; i ++) {

            byte[] leftSide = getLeft(ip);
            byte[] rightSide = getRight(ip);
            byte[] p4 = functionK(rightSide, i, k);
            byte[] xor = XOR(leftSide, p4);
            if(i == rounds){
                ip = combineBytes(xor, rightSide);
                break;
            }
            ip = combineBytes(rightSide, xor); // This causes the swap
        }
        bit = invertTable(ip);
        return bit;
    }

    public static byte[] XOR (byte[] left, byte[] right) {
        byte[] xor = new byte[4];
        for(int i = 0; i < left.length; i ++) {
            xor[i] = (byte) (left[i] ^ right[i]);
        }
        return xor;
    }

    public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext) {
        byte[] bit = {};

        return bit;
    }


    // Initial Permutation
    // 8 bit plaintext
    // swap with associated ip values
    public static byte[] ip(byte[] plaintext) {
        byte[] permkey = {2,6,3,1,4,8,5,7}; // 8 byte permutation defined in Doc
        byte[] permuated = new byte[plaintext.length]; // Assign new array

        HashMap<Integer, Byte> hash = new HashMap<Integer, Byte>(); // Map index with corresponding new index
        for (int i = 0; i < permkey.length; i ++) {
            hash.put(i, permkey[i]); // Key, Value
        }
        for (Entry<Integer, Byte> e : hash.entrySet()) {
            permuated[e.getKey()] = plaintext[e.getValue() - 1];
        }
        return permuated;
    }

    // This will be needed toward the end of the algo
    public static byte[] invertTable(byte[] table) {
        byte[] iptable = {4,1,3,5,7,2,8,6}; // 8 byte permutation defined in Doc
        byte[] invertedTable = new byte[table.length];
        HashMap<Integer, Byte> hash = new HashMap<Integer, Byte>();

        for(int i = 0 ; i < iptable.length; i ++) {
            hash.put(i, iptable[i]); // Key, Value
        }
        for(Entry<Integer, Byte> e: hash.entrySet()) {
            invertedTable[e.getKey()] = table[e.getValue() - 1];
        }
        return invertedTable;
    }


    // Expands from 4 bit input to 8.
    public static byte[] extendPermuate(byte[] right) {
        byte[] permkey = {4,1,2,3,2,3,4,1}; // 8 byte permutation defined in Doc
        byte[] permuated = new byte[right.length * 2]; // Assign new array

        HashMap<Integer, Byte> hash = new HashMap<Integer, Byte>(); // Map index with corresponding new index
        for (int i = 0; i < permkey.length; i ++) {
            hash.put(i, permkey[i]); // Key, Value
        }
        for (Entry<Integer, Byte> e : hash.entrySet()) {
            permuated[e.getKey()] = right[e.getValue() - 1];
        }
        return permuated;

    }
    public static byte[] p4(byte[] left, byte[] right) {
        byte[] combined = combineBytes(left, right);
        byte[] permkey = {2,4,3,1}; // 8 byte permutation defined in Doc
        byte[] permuated = new byte[4]; // Assign new array

        HashMap<Integer, Byte> hash = new HashMap<Integer, Byte>(); // Map index with corresponding new index
        for (int i = 0; i < permkey.length; i ++) {
            hash.put(i, permkey[i]); // Key, Value
        }
        for (Entry<Integer, Byte> e : hash.entrySet()) {
            permuated[e.getKey()] = combined[e.getValue() - 1];
        }
        return permuated;
    }


    public static byte[] functionK(byte[] right, int count, Keys keys) {
        byte[] functionResult = null;
        byte[] extended = extendPermuate(right);
        // For triple SDES: Here we might need to make a loop to accommodate 3,5,n amount of rounds.
        key1 = keys.getKey1(count);

        for(int i = 0; i < key1.length; i ++) {
            extended[i] = (byte) (extended[i] ^ key1[i]);
        }

        byte[] rights1 = getRight(extended);
        byte[] lefts0 = getLeft(extended);

        functionResult = getP4(lefts0, rights1);
        return functionResult;
    }

    public static byte[] getRight(byte[] full) {
        byte[] right = new byte[full.length / 2];
        int pos = (full.length / 2);
        for(int i = 0 ; i < pos; i++) {
            right[i] =  full[ pos + i];
        }
        return right;
    }

    public static byte[] getLeft(byte[] full) {
        byte[] left = new byte[full.length / 2];
        for(int i = 0 ; i < full.length / 2; i ++) {
            left[i] = full[i];
        }
        return left;
    }

    public static void fillMatrix() {

        s0[0][0] = 1; s0[1][0] = 3; s0[2][0] = 0; s0[3][0] = 3;
        s0[0][1] = 0; s0[1][1] = 2; s0[2][1] = 2; s0[3][1] = 1;
        s0[0][2] = 3; s0[1][2] = 1; s0[2][2] = 1; s0[3][2] = 3;
        s0[0][3] = 2; s0[1][3] = 0; s0[2][3] = 3; s0[3][3] = 2;

        s1[0][0] = 0; s1[1][0] = 2; s1[2][0] = 3; s1[3][0] = 2;
        s1[0][1] = 1; s1[1][1] = 0; s1[2][1] = 0; s1[3][1] = 1;
        s1[0][2] = 2; s1[1][2] = 1; s1[2][2] = 1; s1[3][2] = 0;
        s1[0][3] = 3; s1[1][3] = 3; s1[2][3] = 0; s1[3][3] = 3;


    }
}
