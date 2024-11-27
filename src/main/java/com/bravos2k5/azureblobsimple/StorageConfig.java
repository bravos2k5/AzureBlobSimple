package com.bravos2k5.azureblobsimple;

import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Builder
@Data
public class StorageConfig {

    private String storageName;
    private String accessKey;
    private Duration connectTimeOut;

    public BlobServiceClient getClient() {
        if(storageName == null || accessKey == null) {
            throw new IllegalArgumentException("Cannot connect without storage name or access key");
        }
        BlobServiceClientBuilder builder = new BlobServiceClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net/",storageName))
                .credential(new StorageSharedKeyCredential(storageName,accessKey));
        builder.httpClient(new NettyAsyncHttpClientBuilder()
                .connectTimeout(connectTimeOut)
                .build());
        return builder.buildClient();
    }

}
