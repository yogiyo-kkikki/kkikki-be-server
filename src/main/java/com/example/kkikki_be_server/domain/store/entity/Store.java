package com.example.kkikki_be_server.domain.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "store_name", length = 100, nullable = false)
    private String storeName;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "is_open", nullable = false)
    private boolean open = true;

    @Builder
    public Store(String storeName, String category, String address, BigDecimal latitude,
                 BigDecimal longitude, BigDecimal rating, int deliveryFee, boolean open) {
        this.storeName = storeName;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.deliveryFee = deliveryFee;
        this.open = open;
    }
}
