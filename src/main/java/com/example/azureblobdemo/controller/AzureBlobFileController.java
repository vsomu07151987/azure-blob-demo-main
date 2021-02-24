package com.example.azureblobdemo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.azureblobdemo.service.AzureBlobAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AzureBlobFileController {
    
    @Value("${storage.account.name}")
    String accountName;

    @Value("${storage.account.key}")
    String accountKey;
    
    @Autowired
    AzureBlobAdapter azureAdapter;
    
    @Autowired
    BloBService blobService;
    
    @GetMapping(path = "/list")
    public String listAll() {
	return blobService.printAllFiles(accountName, accountKey);
    }

     

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> uploadFile(@RequestPart(value = "file", required = true) MultipartFile files)  {
        String name = azureAdapter.upload(files);
        Map<String, String> result = new HashMap<>();
        result.put("key", name);
        result.put("status", "File uploaded to blob successfully");
        return result;
    }

    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
        byte[] data = azureAdapter.getFile(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);

    }
}