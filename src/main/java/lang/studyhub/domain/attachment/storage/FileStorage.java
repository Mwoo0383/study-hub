package lang.studyhub.domain.attachment.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    StoredFile store(MultipartFile file, String storedName) throws Exception;
    record StoredFile(String uri, long size, String contentType) {}
}
