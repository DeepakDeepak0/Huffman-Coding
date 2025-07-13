package com.deepak.fileCompressor.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class HuffmanService {

    // Node class representing a character and its frequency in the Huffman tree
    static class Node implements Comparable<Node> {
        byte data;
        int freq;
        Node left, right;

        Node(byte data, int freq) {
            this.data = data;
            this.freq = freq;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.freq, other.freq);
        }
    }

    private Map<Byte, String> huffmanCodes = new HashMap<>();

    public byte[] compress(byte[] inputBytes) throws IOException {
        if (inputBytes == null || inputBytes.length == 0)
            return "Empty file provided.".getBytes();

        // Step 1: Build frequency map from actual input
        Map<Byte, Integer> freqMap = buildFrequencyMap(inputBytes);

        // Edge case: only one unique character
        if (freqMap.size() == 1) {
            byte uniqueByte = freqMap.keySet().iterator().next();
            char ch = (char) uniqueByte;
            String display = Character.isISOControl(ch) ? "[" + uniqueByte + "]" : "'" + ch + "'";
            return ("== ONLY ONE CHARACTER IN INPUT ==\n" +
                    "Char: " + display + "\n" +
                    "Repeated: " + inputBytes.length + " times").getBytes();
        }

        // Step 2: Build Huffman tree
        Node root = buildHuffmanTree(freqMap);

        // Step 3: Generate Huffman codes
        huffmanCodes.clear();
        generateCodes(root, "", huffmanCodes);

        // Step 4: Encode input bytes into bit string
        StringBuilder encodedBits = new StringBuilder();
        for (byte b : inputBytes) {
            encodedBits.append(huffmanCodes.get(b));
        }

        // Step 5: Build readable output string
        StringBuilder output = new StringBuilder();

        output.append("== HUFFMAN CODES ==\n");
        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            byte key = entry.getKey();
            char ch = (char) key;
            String display = Character.isISOControl(ch) ? "[" + key + "]" : "'" + ch + "'";
            output.append(display).append(" : ").append(entry.getValue()).append("\n");
        }

        output.append("\n== ENCODED BIT STRING ==\n");
        output.append(encodedBits);

        return output.toString().getBytes();
    }

    private Map<Byte, Integer> buildFrequencyMap(byte[] data) {
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : data) {
            freq.put(b, freq.getOrDefault(b, 0) + 1);
        }
        return freq;
    }

    private Node buildHuffmanTree(Map<Byte, Integer> freqMap) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node merged = new Node((byte) 0, left.freq + right.freq);
            merged.left = left;
            merged.right = right;
            pq.offer(merged);
        }

        return pq.poll();
    }

    private void generateCodes(Node node, String code, Map<Byte, String> map) {
        if (node == null) return;

        if (node.isLeaf()) {
            map.put(node.data, code.length() > 0 ? code : "0"); // handle single-character file
        }

        generateCodes(node.left, code + "0", map);
        generateCodes(node.right, code + "1", map);
    }
}