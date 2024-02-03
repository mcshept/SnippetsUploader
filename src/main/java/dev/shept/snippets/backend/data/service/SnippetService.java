package dev.shept.snippets.backend.data.service;

import dev.shept.snippets.backend.data.entity.Snippet;
import dev.shept.snippets.backend.exception.impl.ResourceNotFoundException;
import dev.shept.snippets.backend.repository.SnippetRepository;
import dev.shept.snippets.backend.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SnippetService {

    private final SnippetRepository snippetRepository;

    @Autowired
    public SnippetService(@Qualifier("snippetRepository") SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    public ResponseEntity<Snippet> createSnippet(Snippet snippet) {
        snippet.setUuid(UUIDGenerator.generateUUID());
        return ResponseEntity.ok(snippetRepository.save(snippet));
    }

    public Optional<Snippet> getSnippet(String uuid) {
        return Optional.ofNullable(snippetRepository.findByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException()));
    }

}
