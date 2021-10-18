package com.company;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class Keys {

    public static byte[] permten = new byte[10];
    public static byte[] key =  new byte[8]; // For now len 8.


    public byte[] left = new byte[10];
    public byte[] right = new byte[10];


    public Keys(byte[] rawkey) {
        Keys.permten = tenpermutation(rawkey);
        Keys.key = rawkey;
    }

    public void leftRotate(byte[] arr, int d, int n) {
        // create temp array of size d
        byte[]  temp = new byte[d];


        // copy first d element in array temp
        for (int i = 0; i < d; i++)
            temp[i] = arr[i];

        // move the rest element to index
        // zero to N-d
        for (int i = d; i < n; i++) {
            arr[i - d] = arr[i];
        }

        // copy the temp array element
        // in original array
        for (int i = 0; i < d; i++) {
            arr[i + n - d] = temp[i];
        }

    }

    // DES key 2
    public byte[] getKey2() {

        byte[] a = splitArray(permten, true); // left
        byte[] b = splitArray(permten, false); // right

        byte[] shifta = shiftLeft(a); // 1st shift before k1, LS-1 -> Product
        byte[] shiftb = shiftLeft(b); // 1st shift before k1, LS-1 -> Product

        byte[] doubleshifta = shiftLeft(shiftLeft(shifta)); // LS-2 Level
        byte[] doubleshiftb = shiftLeft(shiftLeft(shiftb)); // LS-2 Level
        System.out.println("Key 2");
        return eightpermutation(combineBytes(doubleshifta, doubleshiftb));

    }

    // DES key 1
    public byte[] getKey1(int counter) {
        //byte[] permten = tenpermutation(original);

        left = splitArray(permten, true); // left
        right = splitArray(permten, false); // right
        System.out.println("Left side before rotation.");
        p(left);
        System.out.println("Right side before rotation.");
        p(right);

        leftRotate(left,counter,left.length);
        leftRotate(right,counter, right.length);

        System.out.println("Rotation Left: ");
        p(left);
        System.out.println("Rotation Right: ");
        p(right);

        byte[] keyGenerated = eightpermutation(combineBytes(left,right));
        System.out.println("Key" + counter);
        p(keyGenerated);
        return keyGenerated;
    }
    // Prints out btye[] arrays for testing
    public static void p(byte[] arr){
        for(Byte e: arr){
            System.out.print(e);
        }
        System.out.println("");
    }


    public static byte[] tenpermutation(byte[] rawkey) {
        byte[] permkey = {3,5,2,7,4,10,1,9,8,6}; // 10 byte permutation defined in Doc
        byte[] permuated = new byte[10]; // Assign new array

        HashMap<Integer, Byte> hash = new HashMap<Integer, Byte>(); // Map index with corresponding new index
        for (int i = 0; i < permkey.length; i ++) {
            hash.put(i, permkey[i]); // Key, Value
        }
        for (Entry<Integer, Byte> e : hash.entrySet()) {
            permuated[e.getKey()] = rawkey[e.getValue() - 1];
        }
        return permuated;
    }


    public static byte[] eightpermutation(byte[] rawkey) {
        byte[] permkey = {6,3,7,4,8,5,10,9}; // 8 byte permutation defined in Doc
        byte[] permuated = new byte[8]; // Assign new array

        HashMap<Integer, Byte> hash = new HashMap<Integer, Byte>(); // Map index with corresponding new index
        for (int i = 0; i < permkey.length; i ++) {
            hash.put(i, permkey[i]); // Key, Value
        }
        for (Entry<Integer, Byte> e : hash.entrySet()) {
            permuated[e.getKey()] = rawkey[e.getValue() - 1];
        }
        return permuated;
    }


    public static byte[] shiftLeftNtimes(byte[] arr, int n) {
        // Assume n < 0
        byte[] temp = new byte[arr.length];
        for(int i = 0 ; i < n; i ++) {
            temp = shiftLeft(arr);
        }
        return temp;

    }

    // Returns new array shifted once to the left.
    public static byte[] shiftLeft(byte[] arr) {
        byte[] newArray = new byte[arr.length];
        byte firstIndex = arr[0];
        for(int i = 0; i < arr.length - 1; i ++) {
            newArray[i] = arr[i + 1]; // Shifting adding at index 0 -> value of index 1
        }
        newArray[arr.length - 1] = firstIndex; // Add first value to the end of the array.
        return newArray;
    }


    // Need to call this twice and keep track of left hand value.
// boolean lower: True-> Lower half
// boolean lower: False-> Upper half
    public static byte[] splitArray(byte[] rawkey, boolean lower) {
        if(lower) {
            // Lower
            return Arrays.copyOfRange(rawkey, 0, (rawkey.length/2) );
        }else {
            return Arrays.copyOfRange(rawkey, (rawkey.length/2), rawkey.length );
        }

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

}
