package dev.shept.snippets.backend.repository;

import dev.shept.snippets.backend.data.entity.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("snippetRepository")
public interface SnippetRepository extends JpaRepository<Snippet, Long> {

    Optional<Snippet> findByUuid(String uuid);

}
