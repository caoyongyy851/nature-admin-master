package com.ruoyi.nature.service;

import com.ruoyi.nature.domain.CPlace;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.dto.CardDetail;
import com.ruoyi.nature.dto.PlaceDetail;
import com.ruoyi.nature.dto.PlaceDto;
import com.ruoyi.nature.dto.ScheForm;

import java.util.ArrayList;
import java.util.List;

/**
 * 场地Service接口
 *
 * @author ruoyi
 * @date 2021-09-22
 */
public interface ICPlaceService extends IService<CPlace> {

    List<CPlace> getPositionList();

    ArrayList<PlaceDto> getReferPlaceList(Integer pageNum, Integer pageSize);

    PlaceDetail getDetailById(String id);

    Integer countCollect(String pid, String id);

    void insertCollect(String pid, String id);

    void deleteCollect(String pid, String id);

    ArrayList<PlaceDto> getPlaceByCategoryId(String categoryId);

    Integer countLike(String pid, String id);

    void insertLike(String pid, String id);

    void deleteLike(String pid, String id);

    void saveSche(ScheForm form);
}
