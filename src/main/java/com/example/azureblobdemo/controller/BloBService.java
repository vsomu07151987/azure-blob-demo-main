package com.example.azureblobdemo.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

@Service
public class BloBService {

    public String printAllFiles(String accountName, String accountKey) {
	StringBuffer result = new StringBuffer();

	/*
	 * Use your Storage account's name and key to create a credential object; this
	 * is used to access your account.
	 */
	StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

	/*
	 * From the Azure portal, get your Storage account blob service URL endpoint.
	 * The URL typically looks like this:
	 */
	String endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net", accountName);

	/*
	 * Create a BlobServiceClient object that wraps the service endpoint, credential
	 * and a request pipeline.
	 */
	BlobServiceClient storageClient = new BlobServiceClientBuilder().endpoint(endpoint).credential(credential)
		.buildClient();

	
	/*
	 * List the containers' name under the Azure storage account.
	 */
	storageClient.listBlobContainers().forEach(containerItem -> {
	    System.out.println("Container name: " + containerItem.getName());
	    result.append("\"Container name:" + containerItem.getName()+" Files within container are ");
	    storageClient.getBlobContainerClient(containerItem.getName()).listBlobs().forEach(name ->{
		System.out.print("File "+name.getName());
		result.append(name.getName()).append("\n");
	    });
	   
	});
	return result.toString();
    }
}
