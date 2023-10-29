package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);

        ArrayList<Double> doubles = new ArrayList<Double>();
        ArrayList<Character> chars = new ArrayList<Character>();

        Double total = 0.0;

        while(StdIn.hasNextChar()){
            total += 1.0;
            char current = StdIn.readChar();

            if(chars.contains(current)){
                int ind = chars.indexOf(current);
                double curDouble = doubles.get(ind);
                doubles.set(ind, curDouble+1);
            }else{
                chars.add(current);
                doubles.add(1.0);
            }
        }

        if(chars.size() == 1){
            int index = chars.get(0);
            
            if(index != 127){
                chars.add((char)(index+1));
                doubles.add(0.0);
            }else{
                chars.add((char)0);
            }
        }

        sortedCharFreqList = new ArrayList<CharFreq>();
        for(int i = 0; i < chars.size(); i++){
            double a = doubles.get(i)/total;
            sortedCharFreqList.add(new CharFreq(chars.get(i),a));
        }
        Collections.sort(sortedCharFreqList);
	/* Your code goes here */
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {
        Queue<TreeNode> s = new Queue<TreeNode>();
        Queue<TreeNode> t = new Queue<TreeNode>();

        for(int i = 0; i < sortedCharFreqList.size(); i++){
            TreeNode temp = new TreeNode(sortedCharFreqList.get(i), null, null);
            s.enqueue(temp);
        }

        while(s.size() != 0 || t.size() != 1){
            TreeNode left = new TreeNode();
            TreeNode right = new TreeNode();

            if(t.size() == 0){
                left = s.dequeue();
                right = s.dequeue();

                double total = getProb(left)+getProb(right);
                CharFreq a = new CharFreq(null, total);

                TreeNode first = new TreeNode(a, left, right);
                t.enqueue(first);
            }else{
                for(int i = 0; i < 2; i++){
                    if(i == 0){
                        if(s.size() == 0){
                            left = t.dequeue();
                            right = t.dequeue();
                            i = 69;
                        }else{
                            double sp = getProb(s.peek());
                            double tp = getProb(t.peek());
                            
                            if(sp <= tp){
                                left = s.dequeue();
                            }else{
                                left = t.dequeue();
                            }
                        }
                    }else{
                        if(s.size() == 0){
                            right = t.dequeue();
                            i = 69;
                        }else if(t.size() == 0){
                            right = s.dequeue();
                            i = 69;
                        }else{
                            double sp = getProb(s.peek());
                            double tp = getProb(t.peek());

                            if(sp <= tp){
                                right = s.dequeue();
                            }else{
                                right = t.dequeue();
                            }
                        }
                    }
                }

                double total = getProb(left)+getProb(right);
                CharFreq a = new CharFreq(null, total);

                TreeNode temp = new TreeNode(a, left, right);
                t.enqueue(temp);
            }
        }
        huffmanRoot = t.dequeue();
	/* Your code goes here */
    }
    
    private double getProb(TreeNode node){
        return node.getData().getProbOcc();
    }
    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        encodings = new String[128];
        encoder(huffmanRoot, "");
	/* Your code goes here */
    }

    private void encoder(TreeNode a, String b){
        if(a.getData().getCharacter() != null){
            encodings[a.getData().getCharacter()] = b; 
        }

        if(a.getLeft() != null){
            encoder(a.getLeft(), b+"0");
        }

        if(a.getRight() != null){
            encoder(a.getRight(),b+"1");
        }
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String a = "";
        while(StdIn.hasNextChar()){
            a += encodings[StdIn.readChar()];
        }
        writeBitString(encodedFile, a);
	/* Your code goes here */
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        String a = readBitString(encodedFile);
        String b = "";
        TreeNode temp = huffmanRoot;
        int bet = a.length();
        
        for(int i = 0; i < bet; i++){
            if(a.substring(0,1).equals("0")){
                temp = temp.getLeft();

                if(temp.getData().getCharacter()!= null){
                    b+=temp.getData().getCharacter();
                    temp = huffmanRoot;
                }
            }else if(a.substring(0,1).equals("1")){
                temp = temp.getRight();

                if(temp.getData().getCharacter()!= null){
                    b+=temp.getData().getCharacter();
                    temp = huffmanRoot;
                }
            }
            a = a.substring(1);
        }

        StdOut.print(b);
	/* Your code goes here */
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
