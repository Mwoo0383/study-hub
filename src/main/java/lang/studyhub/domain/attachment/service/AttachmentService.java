package lang.studyhub.domain.attachment.service;

import lang.studyhub.domain.attachment.dto.AttachmentDto.*;
import lang.studyhub.domain.attachment.entity.Attachment;
import lang.studyhub.domain.attachment.repository.AttachmentRepository;
import lang.studyhub.domain.attachment.storage.FileStorage;
import lang.studyhub.domain.post.entity.Post;
import lang.studyhub.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service @RequiredArgsConstructor
@Transactional
public class AttachmentService {

    private final AttachmentRepository attachRepo;
    private final PostRepository postRepo;
    private final FileStorage storage;

    public Response uploadToPost(Long postId, Long uploaderId, MultipartFile file) throws Exception {
        Post post = postRepo.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        String ext = getExt(file.getOriginalFilename());
        String storedName = UUID.randomUUID().toString().replace("-", "") + (ext.isEmpty() ? "" : "." + ext);

        var stored = storage.store(file, storedName);

        Attachment a = Attachment.builder()
                .originalName(file.getOriginalFilename())
                .storedName(storedName)
                .contentType(stored.contentType() == null ? "application/octet-stream" : stored.contentType())
                .fileSize(stored.size())
                .storageUri(stored.uri())
                .uploaderId(uploaderId)
                .build();
        attachRepo.save(a);

        return new Response(a.getId(), a.getOriginalName(), a.getContentType(), a.getFileSize(), a.getStorageUri());
    }

    @Transactional(readOnly = true)
    public Attachment getEntity(Long id) {
        return attachRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("첨부 없음"));
    }

    private String getExt(String name) {
        if (name == null) return "";
        int i = name.lastIndexOf('.');
        return (i < 0) ? "" : name.substring(i + 1);
    }
}
