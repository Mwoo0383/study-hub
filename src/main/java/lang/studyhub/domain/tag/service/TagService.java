package lang.studyhub.domain.tag.service;

import lang.studyhub.domain.tag.dto.TagDto.*;
import lang.studyhub.domain.tag.entity.Tag;
import lang.studyhub.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository repo;

    public Response create(CreateRequest req) {
        repo.findByName(req.name()).ifPresent(t -> {
            throw new IllegalArgumentException("이미 존재하는 태그");
        });
        Tag tag = Tag.builder().name(req.name()).build();
        repo.save(tag);
        return new Response(tag.getId(), tag.getName());
    }

    @Transactional(readOnly = true)
    public List<Response> list() {
        return repo.findAll().stream().map(t -> new Response(t.getId(), t.getName())).toList();
    }
}
