package com.ruoyi.nature.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.domain.CPlace;
import com.ruoyi.nature.domain.CSche;
import com.ruoyi.nature.dto.PlaceDetail;
import com.ruoyi.nature.dto.PlaceDto;
import com.ruoyi.nature.dto.ScheForm;
import com.ruoyi.nature.mapper.CPlaceMapper;
import com.ruoyi.nature.service.ICPlaceService;
import com.ruoyi.nature.service.ICScheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 场地Service业务层处理
 *
 * @author ruoyi
 * @date 2021-09-22
 */
@Service
public class CPlaceServiceImpl extends ServiceImpl<CPlaceMapper, CPlace> implements ICPlaceService {

    @Autowired
    private ICScheService icScheService;

    @Override
    public List<CPlace> getPositionList() {
        return baseMapper.getPositionList();
    }

    @Override
    public ArrayList<PlaceDto> getReferPlaceList(Integer pageNum, Integer pageSize) {
        return baseMapper.getReferPlaceList((pageNum - 1) * pageSize, pageSize);
    }

    @Override
    public PlaceDetail getDetailById(String id) {
        return baseMapper.getDetailById(id);
    }

    @Override
    public Integer countCollect(String pid, String id) {
        return baseMapper.countCollect(pid, id);
    }

    @Override
    public void insertCollect(String pid, String id) {
        baseMapper.insertCollect(pid, id);
    }

    @Override
    public void deleteCollect(String pid, String id) {
        baseMapper.deleteCollect(pid, id);
    }

    @Override
    public ArrayList<PlaceDto> getPlaceByCategoryId(String categoryId) {
        return baseMapper.getPlaceByCategoryId(categoryId);
    }

    @Override
    public Integer countLike(String pid, String id) {
        return baseMapper.countLike(pid, id);
    }

    @Override
    public void insertLike(String pid, String id) {
        baseMapper.insertLike(pid, id);
    }

    @Override
    public void deleteLike(String pid, String id) {
        baseMapper.deleteLike(pid, id);
    }
    @Transactional
    @Override
    public void saveSche(ScheForm form) {
        QueryWrapper<CSche> wrapper = new QueryWrapper<>();
        wrapper.eq("place_id", form.getId());
        form.getDomains().forEach(e -> {
            e.setYtd(DateUtil.formatDate(DateUtil.parse(e.getYtd())));
            e.setPlaceId(form.getId());
        });
        icScheService.remove(wrapper);

        icScheService.saveBatch(form.getDomains());
    }
}
