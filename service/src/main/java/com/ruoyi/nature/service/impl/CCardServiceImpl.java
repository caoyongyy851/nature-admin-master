package com.ruoyi.nature.service.impl;

import com.ruoyi.nature.dto.CardDetail;
import com.ruoyi.nature.dto.CardStatusDto;
import com.ruoyi.nature.dto.CardUserDto;
import com.ruoyi.nature.mapper.CUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.mapper.CCardMapper;
import com.ruoyi.nature.domain.CCard;
import com.ruoyi.nature.service.ICCardService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帖子Service业务层处理
 *
 * @author ruoyi
 * @date 2021-09-06
 */
@Service
public class CCardServiceImpl extends ServiceImpl<CCardMapper, CCard> implements ICCardService {

    @Autowired
    private CUserMapper cUserMapper;

    @Override
    public ArrayList<CardUserDto> getCardList(Integer pageNum, Integer pageSize) {
        return baseMapper.getCardList((pageNum - 1) * pageSize, pageSize);
    }

    @Override
    public ArrayList<CardUserDto> getCardList(Integer pageNum, Integer pageSize, String uid) {
        return baseMapper.getCardByUserList((pageNum - 1) * pageSize, pageSize, uid);
    }

    @Override
    public Integer countLike(String cid, String id) {
        return baseMapper.countLike(cid, id);
    }

    @Override
    public void insertLike(String cid, String id) {
        baseMapper.insertLike(cid, id);
    }

    @Override
    public void deleteLike(String cid, String id) {
        baseMapper.deleteLike(cid, id);
    }

    @Override
    public CardDetail getDetailById(String id) {
        return baseMapper.getDetailById(id);
    }

    @Override
    public List<String> getLikes(String id) {
        return baseMapper.getLikes(id);
    }

    @Override
    public CardStatusDto cardStatus(String id, String uid) {
        CCard cCard = getById(id);
        CardStatusDto cardStatusDto = new CardStatusDto();
        Integer isLike = baseMapper.countLike(id, uid);
        Integer isFollow = cUserMapper.countFollow(cCard.getUid(), uid);
        cardStatusDto.setIsFollow(isFollow > 0 ? true : false);
        cardStatusDto.setIsLike(isLike > 0 ? true : false);
        return cardStatusDto;
    }

    @Override
    public ArrayList<CardUserDto> getCardGuanzhuList(Integer pageNum, Integer pageSize, String uid) {
        return baseMapper.getCardGuanzhuList((pageNum - 1) * pageSize, pageSize, uid);
    }

    @Override
    public Map getCountByUid(String id) {
        return baseMapper.getCountByUid(id);
    }

    @Override
    public Integer getFollowsCountByUid(String id) {
        return baseMapper.getFollowsCountByUid(id);
    }

    @Override
    public ArrayList<CardUserDto> getCardListByUid(String uid, Integer pageNum, Integer pageSize) {
        return baseMapper.getCardListByUid(uid, (pageNum-1) * pageSize, pageSize);
    }

    @Override
    public List<CardUserDto> getHotCard(String id, Integer pageNum, Integer pageSize) {
        return baseMapper.getHotCard(id, (pageNum-1) * pageSize, pageSize);
    }

    @Override
    public List<CardUserDto> getNewCard(String id, Integer pageNum, Integer pageSize) {
        return baseMapper.getNewCard(id, (pageNum-1) * pageSize, pageSize);
    }

    @Override
    public List<CardUserDto> getCardByUid(String id, Integer pageNum, Integer pageSize) {
        return baseMapper.getCardByUid(id, (pageNum-1) * pageSize, pageSize);
    }
}

