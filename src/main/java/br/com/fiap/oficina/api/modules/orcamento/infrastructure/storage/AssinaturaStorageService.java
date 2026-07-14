package br.com.fiap.oficina.api.modules.orcamento.infrastructure.storage;

public interface AssinaturaStorageService {

    String store(byte[] content, String originalFilename);

    String storeBase64(String base64Content, String filenamePrefix);

    byte[] baixar(String filename);
}
