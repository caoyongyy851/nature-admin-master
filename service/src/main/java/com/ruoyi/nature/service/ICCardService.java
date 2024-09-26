package com.ruoyi.nature.service;

import com.ruoyi.nature.domain.CCard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.dto.CardDetail;
import com.ruoyi.nature.dto.CardStatusDto;
import com.ruoyi.nature.dto.CardUserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帖子Service接口
 *
 * @author ruoyi
 * @date 2021-09-06
 */
public interface ICCardService extends IService<CCard> {

    ArrayList<CardUserDto> getCardList(Integer pageNum, Integer pageSize);

    ArrayList<CardUserDto> getCardList(Integer pageNum, Integer pageSize, String uid);

    Integer countLike(String cid, String id);

    void insertLike(String cid, String id);

    void deleteLike(String cid, String id);

    CardDetail getDetailById(String id);

    List<String> getLikes(String id);

    CardStatusDto cardStatus(String id, String uid);

    ArrayList<CardUserDto> getCardGuanzhuList(Integer pageNum, Integer pageSize, String uid);

    Map getCountByUid(String id);

    Integer getFollowsCountByUid(String id);

    ArrayList<CardUserDto> getCardListByUid(String uid, Integer pageNum, Integer pageSize);

    List<CardUserDto> getHotCard(String id, Integer pageNum, Integer pageSize);

    List<CardUserDto> getNewCard(String id, Integer pageNum, Integer pageSize);

    List<CardUserDto> getCardByUid(String id, Integer pageNum, Integer pageSize);
}
