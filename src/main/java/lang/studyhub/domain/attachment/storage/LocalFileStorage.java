package lang.studyhub.domain.attachment.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Component
public class LocalFileStorage implements FileStorage {

    private final Path root;

    public LocalFileStorage(@Value("${app.upload.dir:uploads}") String dir) throws Exception {
        this.root = Paths.get(dir).toAbsolutePath().normalize();
        Files.createDirectories(root);
    }

    @Override
    public StoredFile store(MultipartFile file, String storedName) throws Exception {
        Path target = root.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        String uri = target.toUri().toString(); // file://...
        return new StoredFile(uri, file.getSize(), file.getContentType());
    }
}
