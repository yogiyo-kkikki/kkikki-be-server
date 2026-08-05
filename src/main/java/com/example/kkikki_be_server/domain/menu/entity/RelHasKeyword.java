package com.example.kkikki_be_server.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rel_has_keyword")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RelHasKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "has_keyword_id")
    private Long id;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Column(name = "weight", nullable = false)
    private float weight;

    @Builder
    public RelHasKeyword(Long menuId, Long keywordId, float weight) {
        this.menuId = menuId;
        this.keywordId = keywordId;
        this.weight = weight;
    }
}
