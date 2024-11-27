package com.bravos2k5.azureblobsimple;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobProperties;

import java.io.File;
import java.io.InputStream;

public class AzureBlobService {

    private final BlobServiceClient client;

    public AzureBlobService(BlobServiceClient client) {
        this.client = client;
    }

    public BlobContainerClient getContainerClient(String containerName) {
        return client.getBlobContainerClient(containerName);
    }

    public String uploadAndGetUrl(String filePath, String name, String container) {
        BlobContainerClient blobContainerClient = getContainerClient(container);
        BlobClient client = blobContainerClient.getBlobClient(name);
        client.uploadFromFile(filePath,true);
        return client.getBlobUrl();
    }

    public String uploadAndGetUrl(InputStream inputStream, String name, String container) {
        BlobContainerClient blobContainerClient = getContainerClient(container);
        BlobClient client = blobContainerClient.getBlobClient(name);
        client.upload(inputStream, true);
        return client.getBlobUrl();
    }

    public boolean deleteBlob(String name, String container) {
        BlobContainerClient blobContainerClient = getContainerClient(container);
        BlobClient client = blobContainerClient.getBlobClient(name);
        return client.deleteIfExists();
    }

    public void downloadBlob(String name, String container, String destination) {
        BlobContainerClient blobContainerClient = getContainerClient(container);
        BlobClient client = blobContainerClient.getBlobClient(name);
        if(client.exists()) {
            new File(destination).getParentFile().mkdirs();
            client.downloadToFile(destination,true);
        }
    }

    public void renameBlob(String oldName, String newName, String container) {
        BlobContainerClient blobContainerClient = getContainerClient(container);
        BlobClient oldClient = blobContainerClient.getBlobClient(oldName);
        if(oldClient.exists()) {
            BlobClient newClient = blobContainerClient.getBlobClient(newName);
            newClient.copyFromUrl(oldClient.getBlobUrl());
            oldClient.delete();
        }
    }

    public boolean exist(String name, String container) {
        BlobContainerClient blobContainerClient = getContainerClient(container);
        BlobClient client = blobContainerClient.getBlobClient(name);
        return client.exists();
    }

    public void deleteContainer(String container) {
        BlobContainerClient blobContainerClient = getContainerClient(container);
        blobContainerClient.delete();
    }

    public void createContainer(String container) {
        client.createBlobContainerIfNotExists(container);
    }

    public BlobProperties getBlobProperties(String name, String container) {
        BlobContainerClient blobContainerClient = getContainerClient(container);
        BlobClient client = blobContainerClient.getBlobClient(name);
        if (client.exists()) {
            return client.getProperties();
        }
        return null;
    }

}
