package dev.shept.snippets.backend.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "snippets")
public class Snippet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String uuid;
    private byte[] content;

}
