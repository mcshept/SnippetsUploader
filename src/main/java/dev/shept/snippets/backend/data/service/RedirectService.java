package dev.shept.snippets.backend.data.service;

import dev.shept.snippets.backend.data.entity.Snippet;
import dev.shept.snippets.backend.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Service
public class RedirectService {

    private final SnippetRepository snippetRepository;

    @Autowired
    public RedirectService(@Qualifier("snippetRepository") SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    public RedirectView redirect(String uuid) {
        Optional<Snippet> snippet = snippetRepository.findByUuid(uuid);
        if (snippet.isPresent()) {
            return new RedirectView("http://localhost:8080/dashboard/view/" + snippet.get().getUuid());
        } else {
            return new RedirectView("http://localhost:8080/");
        }
    }

}
