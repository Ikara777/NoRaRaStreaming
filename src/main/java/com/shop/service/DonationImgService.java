package com.shop.service;


import com.shop.entity.Donation;
import com.shop.entity.DonationImg;
import com.shop.entity.MemberImg;
import com.shop.repository.DonationImgRepository;
import com.shop.repository.DonationRepository;
import com.shop.repository.MemberImgRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class DonationImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final MemberRepository memberRepository;
    private final DonationRepository donationRepository;
    private final DonationImgRepository donationImgRepository;
    private final FileService fileService;

    public void saveDonationImgList(List<DonationImg> donationImgList,
                                    List<MultipartFile> donationImgFileList) throws Exception {

        for(int i = 0; i < donationImgFileList.size(); i++) {
            MultipartFile donationImgFile = donationImgFileList.get(i);
            DonationImg donationImg = donationImgList.get(i);

            // 이미지 엔티티 업로드
            if(donationImgFile != null && !donationImgFile.isEmpty()) {
                String oriImgName = donationImgFile.getOriginalFilename();
                String imgName = fileService.uploadFile(itemImgLocation,
                        oriImgName,
                        donationImgFile.getBytes());
                String imgUrl = "/images/item/" + imgName;

                donationImg.updateDonationImg(oriImgName, imgName, imgUrl);
            } else {
                donationImg.setOriImgName(null);
                donationImg.setImgName(null);
                donationImg.setImgUrl(null);
            }

            donationImgRepository.save(donationImg);
        }
    }

    public DonationImg saveDonationImg(MultipartFile donationImgFile, Donation donation) throws Exception {
        DonationImg donationImg = new DonationImg();
        donationImg.setDonation(donation);
        donationImg.setMember(donation.getMember());

        if (!donationImgFile.isEmpty()) {
            String oriImgName = donationImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, donationImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            donationImg.updateDonationImg(oriImgName, imgName, imgUrl);
        }

        return donationImgRepository.save(donationImg);
    }


    public void deleteDonationImg(DonationImg donationImg) throws Exception {
        if (donationImg.getImgName() != null) {
            fileService.deleteFile(itemImgLocation + "/" + donationImg.getImgName());
        }
        donationImgRepository.delete(donationImg);
    }
}
