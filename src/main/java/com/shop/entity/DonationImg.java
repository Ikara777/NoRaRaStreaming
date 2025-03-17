package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "donation_img")
@Getter
@Setter
@NoArgsConstructor
public class DonationImg {

    @Id
    @Column(name = "donation_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imgName; //이미지 이름
    private String oriImgName; // 오리지널 이름이란 변수설정 할 때 바뀔 수 있는 이름을 보존하기 위해


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @Column(length = 65535, columnDefinition = "TEXT") // 길이를 충분히 늘림
    private String imgUrl;
    private String repImgYn; // 대표이미지 - 0번째 이미지를 대표이미지로 정함



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    private Donation donation;
    

    public void updateDonationImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
        // 원래는 업데이트 명령을 내려야 하는데 '변경 감지' 때문에 자동으로 변경 가능
    }


}
