package com.example.kkikki_be_server.domain.recommendation.entity;

import com.example.kkikki_be_server.global.common.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "recommendation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "comb_id", nullable = false)
    private Long combId;

    @Column(name = "main_menu_id", nullable = false)
    private Long mainMenuId;

    @Column(name = "paired_menu_id", nullable = false)
    private Long pairedMenuId;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_type", length = 20, nullable = false)
    private RecommendationType recommendationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "placement_type", length = 30, nullable = false)
    private PlacementType placementType;

    @Column(name = "match_score", precision = 5, scale = 4)
    private BigDecimal matchScore;

    @Column(name = "recommendation_message", length = 500)
    private String recommendationMessage;

    @Builder
    public Recommendation(Long userId, Long combId, Long mainMenuId, Long pairedMenuId,
                          RecommendationType recommendationType, PlacementType placementType,
                          BigDecimal matchScore, String recommendationMessage) {
        this.userId = userId;
        this.combId = combId;
        this.mainMenuId = mainMenuId;
        this.pairedMenuId = pairedMenuId;
        this.recommendationType = recommendationType;
        this.placementType = placementType;
        this.matchScore = matchScore;
        this.recommendationMessage = recommendationMessage;
    }
}
