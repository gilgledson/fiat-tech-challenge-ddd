package br.com.fiap.oficina.api.modules.orcamento.infrastructure.storage;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Armazena as assinaturas de aceite de orçamento no Azure Blob Storage.
 * Antes eram salvas no disco local do container (uploads/assinaturas/), que é
 * efêmero: some a cada restart/redeploy do pod e não é compartilhado entre
 * réplicas do HPA. O container de blobs já é provisionado via Terraform
 * (infra/storage.tf) — este serviço só faz upload/download.
 */
@ApplicationScoped
public class AzureBlobStorageService {

    private final BlobContainerClient containerClient;

    public AzureBlobStorageService(
            @ConfigProperty(name = "oficina.storage.azure.connection-string") String connectionString,
            @ConfigProperty(name = "oficina.storage.azure.container") String containerName) {
        this.containerClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient()
                .getBlobContainerClient(containerName);
    }

    public String store(byte[] content, String originalFilename) {
        String extension = originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".png";
        String blobName = UUID.randomUUID() + extension;

        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.upload(new ByteArrayInputStream(content), content.length, true);

        return blobName;
    }

    public String storeBase64(String base64Content, String filenamePrefix) {
        String pureBase64 = base64Content.contains(",")
                ? base64Content.split(",")[1]
                : base64Content;

        byte[] decodedBytes = Base64.getDecoder().decode(pureBase64);
        return store(decodedBytes, filenamePrefix + ".png");
    }

    public byte[] baixar(String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        if (!blobClient.exists()) {
            return null;
        }
        return blobClient.downloadContent().toBytes();
    }
}
