package com.example.kkikki_be_server.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_analysis")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_analysis_id")
    private Long id;

    @Column(name = "anchor_menu_id", nullable = false)
    private Long anchorMenuId;

    @Column(name = "analyzed_review_count", nullable = false)
    private int analyzedReviewCount;

    @Column(name = "pairing_intent", length = 20)
    private String pairingIntent;

    @Column(name = "recommendable", nullable = false)
    private boolean recommendable;

    @Lob
    @Column(name = "summary")
    private String summary;

    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt;

    @Builder
    public ReviewAnalysis(Long anchorMenuId, int analyzedReviewCount, String pairingIntent,
                          boolean recommendable, String summary, LocalDateTime analyzedAt) {
        this.anchorMenuId = anchorMenuId;
        this.analyzedReviewCount = analyzedReviewCount;
        this.pairingIntent = pairingIntent;
        this.recommendable = recommendable;
        this.summary = summary;
        this.analyzedAt = analyzedAt;
    }
}
