package lang.studyhub.domain.attachment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lang.studyhub.domain.attachment.dto.AttachmentDto.*;
import lang.studyhub.domain.attachment.entity.Attachment;
import lang.studyhub.domain.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
@Tag(name="Attachment", description="첨부파일 업로드/다운로드")
public class AttachmentController {

    private final AttachmentService service;

    @PostMapping(path="/posts/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadToPost(@PathVariable Long postId,
                                 @RequestParam Long uploaderId,
                                 @RequestPart("file") MultipartFile file) throws Exception {
        return service.uploadToPost(postId, uploaderId, file);
    }

    @GetMapping("/{id}/download")
    public void download(@PathVariable Long id, HttpServletResponse resp) throws Exception {
        Attachment a = service.getEntity(id);
        UrlResource resource = new UrlResource(a.getStorageUri());

        resp.setContentType(a.getContentType());
        String encoded = URLEncoder.encode(a.getOriginalName(), StandardCharsets.UTF_8);
        resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded);
        resource.getInputStream().transferTo(resp.getOutputStream());
        resp.flushBuffer();
    }
}
