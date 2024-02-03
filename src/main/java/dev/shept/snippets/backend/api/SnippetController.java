package dev.shept.snippets.backend.api;

import dev.shept.snippets.backend.data.entity.Snippet;
import dev.shept.snippets.backend.data.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/snippet")
public class SnippetController {

    private final SnippetService snippetService;

    @Autowired
    public SnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @PostMapping("/create")
    public ResponseEntity<Snippet> createSnippet(@RequestBody Snippet snippet) {
        return snippetService.createSnippet(snippet);
    }

    @GetMapping("/get/{uuid}")
    public Optional<Snippet> getSnippet(@PathVariable String uuid) {
        return snippetService.getSnippet(uuid);
    }

}
