package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "starRequest")
public class StarRequest {

    @Id
    @Column(name = "member_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String bankCode;
    private String accountNumber;
    private String accountHolder;
    private int amount;


}
