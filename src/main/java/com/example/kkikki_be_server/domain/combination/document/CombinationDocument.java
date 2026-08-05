package com.example.kkikki_be_server.domain.combination.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

// MongoDB의 "combinations" 라는 컬렉션(테이블)에 저장됨
@Document(collection = "combinations")
@Getter
public class CombinationDocument {

    @Id
    private String id; // 💡 MongoDB가 자동 생성해주는 ObjectId (ex: "64c9f1a...")

    @Field("combination_name")
    private String combinationName; // 예: "치킨 + 콜라 조합"

    @Field("recommendation_reason")
    private String recommendationReason; // LLM이 써준 이유: "SNS에서 급상승 중인..."

    @Field("confidence_score")
    private double confidenceScore; // 매칭 점수: 0.8732

    // 💡 MongoDB는 NoSQL이라 객체 안에 객체를 자유롭게 넣을 수 있습니다!
    @Field("main_menu")
    private SimpleMenu mainMenu;

    @Field("paired_menu")
    private SimpleMenu pairedMenu;

    @Field("analyzed_at")
    private LocalDateTime analyzedAt;

    @Builder
    public CombinationDocument(String combinationName, String recommendationReason,
                               double confidenceScore, SimpleMenu mainMenu,
                               SimpleMenu pairedMenu, LocalDateTime analyzedAt) {
        this.combinationName = combinationName;
        this.recommendationReason = recommendationReason;
        this.confidenceScore = confidenceScore;
        this.mainMenu = mainMenu;
        this.pairedMenu = pairedMenu;
        this.analyzedAt = analyzedAt;
    }

    // 내부에서 사용할 심플한 메뉴 객체 (MySQL의 Menu 데이터를 요약해서 저장)
    @Getter
    @Builder
    public static class SimpleMenu {
        private Long menuId; // MySQL의 Menu PK
        private String menuName;
        private String imageUrl;
    }
}