package com.deepak.fileCompressor.controller;


import com.deepak.fileCompressor.service.HuffmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*") // allow frontend access from different port
public class FileController {

    @Autowired
    private HuffmanService huffmanService;

    @PostMapping("/compress")
    public ResponseEntity<byte[]> compressFile(@RequestParam("file") MultipartFile file) {
        try {
            byte[] inputBytes = file.getBytes();
            byte[] compressedBytes = huffmanService.compress(inputBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("compressed.txt")
                    .build());

            return new ResponseEntity<>(compressedBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Compression failed: " + e.getMessage()).getBytes());
        }
    }
}