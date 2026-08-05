package com.example.kkikki_be_server.domain.combination.entity;

import com.example.kkikki_be_server.domain.common.BaseCreatedEntity;
import com.example.kkikki_be_server.domain.recommendation.entity.SourceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Combination extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comb_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 20, nullable = false)
    private SourceType sourceType;

    @Column(name = "sns_trend_analysis_id")
    private Long snsTrendAnalysisId;

    @Column(name = "review_analysis_id")
    private Long reviewAnalysisId;

    @Column(name = "comb_doc_id", length = 24, nullable = false)
    private String combDocId;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Builder
    public Combination(SourceType sourceType, Long snsTrendAnalysisId, Long reviewAnalysisId,
                String combDocId, boolean active) {
        this.sourceType = sourceType;
        this.snsTrendAnalysisId = snsTrendAnalysisId;
        this.reviewAnalysisId = reviewAnalysisId;
        this.combDocId = combDocId;
        this.active = active;
    }

    public void deactivate() {
        this.active = false;
    }
}
