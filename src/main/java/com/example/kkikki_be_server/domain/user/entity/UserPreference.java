package com.example.kkikki_be_server.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_preference")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_preference_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "preference_tag", length = 50, nullable = false)
    private String preferenceTag;

    @Column(name = "preference_score", precision = 5, scale = 4, nullable = false)
    private BigDecimal preferenceScore;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public UserPreference(Long userId, String preferenceTag, BigDecimal preferenceScore,
                          LocalDateTime updatedAt) {
        this.userId = userId;
        this.preferenceTag = preferenceTag;
        this.preferenceScore = preferenceScore;
        this.updatedAt = updatedAt;
    }
}
