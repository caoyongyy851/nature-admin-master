package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.CPlace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.dto.PlaceDetail;
import com.ruoyi.nature.dto.PlaceDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 场地Mapper接口
 *
 * @author ruoyi
 * @date 2021-09-22
 */
public interface CPlaceMapper extends BaseMapper<CPlace> {

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
}
