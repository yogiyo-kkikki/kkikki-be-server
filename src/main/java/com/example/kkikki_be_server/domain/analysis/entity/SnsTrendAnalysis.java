package com.example.kkikki_be_server.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sns_trend_analysis")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnsTrendAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sns_trend_analysis_id")
    private Long id;

    @Column(name = "keyword", length = 100, nullable = false)
    private String keyword;

    @Column(name = "trend_score", precision = 5, scale = 2)
    private BigDecimal trendScore;

    @Column(name = "context_type", length = 40)
    private String contextType;

    @Column(name = "purchase_intent", length = 20)
    private String purchaseIntent;

    @Column(name = "recommendable", nullable = false)
    private boolean recommendable;

    @Lob
    @Column(name = "summary")
    private String summary;

    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt;

    @Builder
    public SnsTrendAnalysis(String keyword, BigDecimal trendScore, String contextType,
                            String purchaseIntent, boolean recommendable, String summary,
                            LocalDateTime analyzedAt) {
        this.keyword = keyword;
        this.trendScore = trendScore;
        this.contextType = contextType;
        this.purchaseIntent = purchaseIntent;
        this.recommendable = recommendable;
        this.summary = summary;
        this.analyzedAt = analyzedAt;
    }
}
