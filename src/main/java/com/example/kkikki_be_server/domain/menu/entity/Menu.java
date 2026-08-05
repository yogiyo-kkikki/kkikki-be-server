package com.example.kkikki_be_server.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "menu_name", length = 100, nullable = false)
    private String menuName;

    @Column(name = "normalized_menu_name", length = 100, nullable = false)
    private String normalizedMenuName;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @Builder
    public Menu(Long storeId, String menuName, String normalizedMenuName, String category,
                int price, String imageUrl, boolean available) {
        this.storeId = storeId;
        this.menuName = menuName;
        this.normalizedMenuName = normalizedMenuName;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.available = available;
    }
}