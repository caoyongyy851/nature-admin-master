package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.CCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.dto.CardDetail;
import com.ruoyi.nature.dto.CardUserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帖子Mapper接口
 *
 * @author ruoyi
 * @date 2021-09-06
 */
public interface CCardMapper extends BaseMapper<CCard> {

    ArrayList<CardUserDto> getCardList(int i, Integer pageSize);

    ArrayList<CardUserDto> getCardByUserList(int i, Integer pageSize, String uid);

    Integer countLike(String cid, String id);

    void insertLike(String cid, String id);

    void deleteLike(String cid, String id);

    CardDetail getDetailById(String id);

    List<String> getLikes(String id);

    ArrayList<CardUserDto> getCardGuanzhuList(int i, Integer pageSize, String uid);

    Map getCountByUid(String id);

    Integer getFollowsCountByUid(String id);

    ArrayList<CardUserDto> getCardListByUid(String uid, int i, Integer pageSize);

    List<CardUserDto> getHotCard(String id, int i, Integer pageSize);

    List<CardUserDto> getNewCard(String id, int i, Integer pageSize);

    List<CardUserDto> getCardByUid(String id, int i, Integer pageSize);
}
