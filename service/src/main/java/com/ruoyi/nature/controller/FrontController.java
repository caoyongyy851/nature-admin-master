package com.ruoyi.nature.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.web.service.WxLoginService;
import com.ruoyi.framework.web.service.WxUser;
import com.ruoyi.nature.config.WxMaConfiguration;
import com.ruoyi.nature.config.WxMineProperties;
import com.ruoyi.nature.domain.*;
import com.ruoyi.nature.dto.*;
import com.ruoyi.nature.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/8/14 15:18
 * @Description:
 */
@RequestMapping("nature/front")
@RestController
@Api(tags = "前端控制器")
@Slf4j
public class FrontController {
    @Autowired
    private WxLoginService wxLoginService;
    @Autowired
    private ICUserService icUserService;
    @Autowired
    private ICActivityService icActivityService;
    @Autowired
    private ICCommentService icCommentService;
    @Autowired
    private ICACommentService icaCommentService;
    @Autowired
    private ICCardService icCardService;
    @Autowired
    private ICPlaceService icPlaceService;
    @Autowired
    private ICTopicService icTopicService;
    @Autowired
    private ICOrderService icOrderService;
    @Autowired
    private ICRefundAuditService icRefundAuditService;
    @Autowired
    private ICCompanyService icCompanyService;
    @Autowired
    private ICPlaceConsultService icPlaceConsultService;
    @Autowired
    private ICTopicCoverService icTopicCoverService;
    @Autowired
    private WxMineProperties wxMineProperties;
    @Autowired
    private ICActOrderService icActOrderService;
    @Autowired
    private RedisCache redisCache;


    @ApiOperation(value = "评论")
    @PostMapping("writeComment")
    public R writeComment(@RequestBody CComment cComment, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser) && StringUtils.isNotNull(cComment)) {
            CActivity cActivity = icActivityService.getById(cComment.getCid());
            cActivity.setComments(cActivity.getComments() + 1);
            icActivityService.updateById(cActivity);
            cComment.setUid(cUser.getId());
            icCommentService.save(cComment);
            icUserService.addPoints(cUser.getId(), 1);
            return R.success("评论成功");
        }
        return R.error("评论出错");
    }


    @ApiOperation(value = "取消收藏")
    @GetMapping("activityCollectCancel/{aid}")
    public R activityCollectCancel(@PathVariable String aid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CActivity cActivity = icActivityService.getById(aid);
            if (StringUtils.isNotNull(cActivity)) {
                Integer col = icActivityService.delCollections(aid, cUser.getId());
                if (col == 1) {
                    cActivity.setCollections(cActivity.getCollections() - 1);
                    icActivityService.updateById(cActivity);
                    return R.success("取消收藏成功");
                }
            }
        }
        return R.error("取消收藏出错");
    }

    @ApiOperation(value = "喜欢活动")
    @GetMapping("activityAppreciate/{aid}")
    public R activityAppreciate(@PathVariable String aid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CActivity cActivity = icActivityService.getById(aid);
            if (StringUtils.isNotNull(cActivity)) {

                Integer count = icActivityService.countAppreciate(aid, cUser.getId());
                if (count == 0) {
//                    //删除点踩数
//                    Integer cai = icActivityService.delACai(aid, cUser.getId());
//                    if (cai == 1) {
//                        cActivity.setUnlikes(cActivity.getUnlikes() - 1);
//                    }
                    cActivity.setLikes(cActivity.getLikes() + 1);
                    icActivityService.updateById(cActivity);
                    icActivityService.insertAZan(aid, cUser.getId());
                    return R.success("喜欢成功");
                }
            }
        }
        return R.error("点赞出错");
    }

    @ApiOperation(value = "取消喜欢")
    @GetMapping("activityAppreciateCancel/{aid}")
    public R activityAppreciateCancel(@PathVariable String aid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CActivity cActivity = icActivityService.getById(aid);
            if (StringUtils.isNotNull(cActivity)) {
                Integer zan = icActivityService.delAZan(aid, cUser.getId());
                if (zan == 1) {
                    cActivity.setLikes(cActivity.getLikes() - 1);
                    icActivityService.updateById(cActivity);
                    return R.success("取消喜欢成功");
                }
            }
        }
        return R.error("取消喜欢出错");
    }

    @ApiOperation(value = "点踩活动")
    @GetMapping("activityTread/{aid}")
    public R activityTread(@PathVariable String aid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CActivity cActivity = icActivityService.getById(aid);
            if (StringUtils.isNotNull(cActivity)) {
                Integer count = icActivityService.countTread(aid, cUser.getId());
                if (count == 0) {
                    //删除点赞数
                    Integer zan = icActivityService.delAZan(aid, cUser.getId());
                    if (zan == 1) {
                        cActivity.setLikes(cActivity.getLikes() - 1);
                    }
                    cActivity.setUnlikes(cActivity.getUnlikes() + 1);
                    icActivityService.updateById(cActivity);
                    icActivityService.insertACai(aid, cUser.getId());
                    return R.success("点踩成功");
                }
            }
        }
        return R.error("点踩出错");
    }

    @ApiOperation(value = "取消点踩活动")
    @GetMapping("activityTreadCancel/{aid}")
    public R activityTreadCancel(@PathVariable String aid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CActivity cActivity = icActivityService.getById(aid);
            if (StringUtils.isNotNull(cActivity)) {
                Integer cai = icActivityService.delACai(aid, cUser.getId());
                if (cai == 1) {
                    cActivity.setUnlikes(cActivity.getUnlikes() - 1);
                    icActivityService.updateById(cActivity);
                    return R.success("取消点踩成功");
                }
            }
        }
        return R.error("取消点踩出错");
    }


    @ApiOperation(value = "点踩评论")
    @GetMapping("commentTread/{cid}")
    public R commentTread(@PathVariable String cid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CComment cComment = icCommentService.getById(cid);
            if (StringUtils.isNotNull(cComment)) {

                Integer count = icCommentService.countCCai(cid, cUser.getId());
                if (count == 0) {
                    //删除点赞数
                    Integer zan = icCommentService.delCZan(cid, cUser.getId());
                    if (zan == 1) {
                        cComment.setLikes(cComment.getLikes() - 1);
                    }
                    icCommentService.updateById(cComment);
                    icCommentService.insertCCai(cid, cUser.getId());
                    return R.success("点踩成功");
                }
            }
        }
        return R.error("点踩出错");
    }

    @ApiOperation(value = "取消点踩评论")
    @GetMapping("commentTreadCancel/{cid}")
    public R commentTreadCancel(@PathVariable String cid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CComment cComment = icCommentService.getById(cid);
            if (StringUtils.isNotNull(cComment)) {
                Integer cai = icCommentService.delCCai(cid, cUser.getId());
                if (cai == 1) {
                    icCommentService.updateById(cComment);
                    return R.success("取消点踩成功");
                }
            }
        }
        return R.error("取消点踩出错");
    }


    /**
     ******************************* 自然玩主 ********************************
     */
    /**
     * 获取帖子列表（不用）
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getCardList/{pageNum}/{pageSize}")
//    @Cacheable(value = "getCardList", key = "'pageNew_'+#pageNum+'pageSize_'+#pageSize", cacheManager = "redisExpire")
    public R getCardList(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        ArrayList<CardUserDto> cardList = icCardService.getCardList(pageNum, pageSize, cUser.getId());
        cardList.forEach(e -> {
            if (StringUtils.isNotNull(e.getImgs())) {
                ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImgs().split(",")));
                e.setImgList(imgList);
                e.setImgs(null);
            }
        });
        return R.success("getCardList", cardList);
    }

    /**
     * 获取当前用户关注的帖子列表
     *
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("getCardGuanzhuList/{pageNum}/{pageSize}")
    public R getCardGuanzhuList(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        ArrayList<CardUserDto> cardList = icCardService.getCardGuanzhuList(pageNum, pageSize, cUser.getId());
        if (StringUtils.isNotNull(cUser)) {
            cardList.forEach(e -> {
                if (StringUtils.isNotNull(e.getImgs())) {
                    ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImgs().split(",")));
                    e.setImgList(imgList);
                    e.setImgs(null);
                }
            });
            return R.success("getCardList", cardList);
        }
        return R.error("查询出错");
    }


    /**
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "用户新增帖子")
    @PostMapping("createCard")
    public R createCard(@RequestBody CCard card, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
//            鉴定敏感图文
            try {
                checkImgAndText(card.getImgs(), card.getTitle() + card.getDetail());
            } catch (WxErrorException e) {
                log.error("【用户ID】：" + cUser.getId());
                log.error("【违规内容】：" + e.getMessage());
                return R.error("严禁上传违规内容，您的违规操作已被记录！");
            }
            try {
                checkVideo(card.getVids());
            } catch (WxErrorException e) {
                log.error("【用户ID】：" + cUser.getId());
                log.error("【违规内容】：" + e.getMessage());
                return R.error("严禁上传违规内容，您的违规操作已被记录！");
            }
            card.setUid(cUser.getId());
            icCardService.save(card);
            icUserService.addPoints(cUser.getId(), 20);
            List<CUser> list = icUserService.list();
            list.forEach(e -> {
                Map<String, Object> cardMap = redisCache.getCacheMap("sign:card:uid:" + e.getId());
                cardMap.put(card.getId(), 1);
                log.info("cardMap : {}", JSONUtil.toJsonStr(cardMap));
                redisCache.setCacheMap("sign:card:uid:" + e.getId(), cardMap);

                Map<String, Object> topicMap = redisCache.getCacheMap("sign:topic:uid:" + e.getId());
                topicMap.put(card.getTopicId(), 1);
                log.info("topicMap : {}", JSONUtil.toJsonStr(topicMap));
                redisCache.setCacheMap("sign:topic:uid:" + e.getId(), topicMap);
            });
            return R.success("新增成功");
        }
        return R.error("新增出错");
    }

    /**
     * 帖子图片或视频上传
     */
    @PostMapping("/cardFileImg")
    public R cardFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String cardFile = FileUploadUtils.upload(RuoYiConfig.getCardPath(), file);
            R r = R.success();
            r.put("imgUrl", cardFile);
            return r;
        }
        return R.error("上传视频异常");
    }

    /**
     * 帖子图片或视频上传
     */
    @PostMapping("/cardFileVid")
    public R cardFileVid(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String cardFile = FileUploadUtils.uploadVideo(RuoYiConfig.getCardPath(), file);
            R r = R.success();
            r.put("vidUrl", cardFile);
            return r;
        }
        return R.error("上传视频异常");
    }

    /**
     * 话题图片上传
     */
    @PostMapping("/topicFileImg")
    public R topicFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String cardFile = FileUploadUtils.upload(RuoYiConfig.getTopicPath(), file);
            R r = R.success();
            r.put("imgUrl", cardFile);
            return r;
        }
        return R.error("上传视频异常");
    }

    /**
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "用户新增活动")
    @PostMapping("createActivity")
    public R createActivity(@RequestBody CActivity activity, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            //            鉴定敏感图文
//            try {
//                checkImgAndText(activity.getImg(), activity.getTitle() + activity.getLabel() + activity.getLabel());
//                checkImgAndText(activity.getQrcodeUrl(), null);
//            } catch (WxErrorException e) {
//                log.error("【用户ID】：" + cUser.getId());
//                log.error("【违规内容】：" + e.getMessage());
//                return R.error("严禁上传违规内容，您的违规操作已被记录！");
//            }
            activity.setPbid(cUser.getId());
            if (StringUtils.isNull(activity.getPayType())){
                return R.error("请选择付款方式");
            }
            if (activity.getPayType() == 0)
            {
                activity.setQrcodeUrl(null);
                activity.setPrice(null);
            }else if (activity.getPayType() == 1)
            {
                if (activity.getPrice().compareTo(BigDecimal.ZERO) <= 0)
                {
                    return R.error("请输入正确金额");
                }
                activity.setQrcodeUrl(null);
            }else if (activity.getPayType() == 2 ){
                activity.setPrice(null);
            }
            icActivityService.save(activity);
            icUserService.addPoints(cUser.getId(), 20);
            return R.success("新增成功");
        }
        return R.error("新增出错");
    }

    /**
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "用户修改活动")
    @PostMapping("modifyActivity")
    public R modifyActivity(@RequestBody CActivity activity, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            activity.setPbid(cUser.getId());
            icActivityService.updateById(activity);
            return R.success("修改成功");
        }
        return R.error("修改出错");
    }


    /**
     * 帖子图片或视频上传
     */
    @PostMapping("/activityFile")
    public R activityFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
//            CUser cUser = getCUserByRequest(ServletUtils.getRequest());
            String cardFile = FileUploadUtils.upload(RuoYiConfig.getActivityPath(), file);
            R r = R.success();
            r.put("imgUrl", cardFile);
            return r;
        }
        return R.error("上传图片/视频异常");
    }


    /**
     * @param cid
     * @param request
     * @return
     */
    @ApiOperation(value = "喜欢/取消喜欢")
    @GetMapping("cardToLike/{cid}")
    public R cardToLike(@PathVariable String cid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CCard cCard = icCardService.getById(cid);
            if (StringUtils.isNotNull(cCard)) {
                Integer count = icCardService.countLike(cid, cUser.getId());
                if (count == 0) {
                    cCard.setLikes(cCard.getLikes() + 1);
                    icCardService.updateById(cCard);
                    icCardService.insertLike(cid, cUser.getId());
                    return R.success("喜欢成功");
                } else {
                    cCard.setLikes(cCard.getLikes() - 1);
                    icCardService.updateById(cCard);
                    icCardService.deleteLike(cid, cUser.getId());
                    return R.success("取消喜欢成功");
                }
            }
        }
        return R.error("喜欢出错");
    }

    @ApiOperation(value = "评论")
    @PostMapping("toComment")
    public R toComment(@RequestBody CComment cComment, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser) && StringUtils.isNotNull(cComment)) {

            try {
                checkImgAndText(null, cComment.getContent());
            } catch (WxErrorException e) {
                log.error("【用户ID】：" + cUser.getId());
                log.error("【违规内容】：" + e.getMessage());
                return R.error("严禁上传违规内容，您的违规操作已被记录！");
            }

            CCard cCard = icCardService.getById(cComment.getCid());
            cCard.setComments(cCard.getComments() + 1);
            icCardService.updateById(cCard);
            cComment.setUid(cUser.getId());
            icCommentService.save(cComment);
            icUserService.addPoints(cUser.getId(), 1);
            return R.success("评论成功");
        }
        return R.error("评论出错");
    }

    @ApiOperation(value = "关注/取消关注")
    @GetMapping("userToFollow/{followUid}")
    public R userToFollow(@PathVariable String followUid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CUser followUser = icUserService.getById(followUid);
            if (StringUtils.isNotNull(followUser)) {
                Integer count = icUserService.countFollow(followUid, cUser.getId());
                if (count == 0) {
                    icUserService.insertFollow(followUid, cUser.getId());
                    return R.success("关注成功");
                } else {
                    icUserService.deleteFollow(followUid, cUser.getId());
                    return R.success("取消关注成功");
                }
            }
        }
        return R.error("关注出错");
    }

    @ApiOperation(value = "关注/取消关注")
    @GetMapping("topicToFollow/{topicId}")
    public R topicToFollow(@PathVariable String topicId, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CTopic cTopic = icTopicService.getById(topicId);
            if (StringUtils.isNotNull(cTopic)) {
                Integer count = icTopicService.countFollow(cTopic.getId(), cUser.getId());
                if (count == 0) {
                    icTopicService.insertFollow(cTopic.getId(), cUser.getId());
                    return R.success("关注成功");
                } else {
                    icTopicService.deleteFollow(cTopic.getId(), cUser.getId());
                    return R.success("取消关注成功");
                }
            }
        }
        return R.error("关注出错");
    }

    @ApiOperation(value = "点赞评论")
    @GetMapping("commentAppreciate/{cid}")
    public R commentAppreciate(@PathVariable String cid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CComment cComment = icCommentService.getById(cid);
            if (StringUtils.isNotNull(cComment)) {
                Integer count = icCommentService.countCZan(cid, cUser.getId());
                if (count == 0) {
                    //删除点踩数
//                    Integer cai = icCommentService.delCCai(cid, cUser.getId());
                    cComment.setLikes(cComment.getLikes() + 1);
                    icCommentService.updateById(cComment);
                    icCommentService.insertCZan(cid, cUser.getId());
                    return R.success("点赞成功");
                }
            }
        }
        return R.error("点赞出错");
    }

    @ApiOperation(value = "取消点赞")
    @GetMapping("commentAppreciateCancel/{cid}")
    public R commentAppreciateCancel(@PathVariable String cid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CComment cComment = icCommentService.getById(cid);
            if (StringUtils.isNotNull(cComment)) {
                Integer zan = icCommentService.delCZan(cid, cUser.getId());
                if (zan == 1) {
                    cComment.setLikes(cComment.getLikes() - 1);
                    icCommentService.updateById(cComment);
                    return R.success("取消点赞成功");
                }
            }
        }
        return R.error("取消点赞出错");
    }

    @ApiOperation(value = "获取用户当前帖子状态")
    @GetMapping("getCardUserStatus/{id}")
    public R getCardUserStatus(@PathVariable String id, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {

            CardStatusDto cardStatus = icCardService.cardStatus(id, cUser.getId());
            List<Boolean> commentStatus = icCommentService.commentStatus(id, cUser.getId());

            Map<String, Object> statusMap = new HashMap<>(2);
            statusMap.put("cardStatus", cardStatus);
            statusMap.put("commentStatus", commentStatus);
            return R.success("获取成功", statusMap);
        }
        return R.error("获取当前用户帖子状态失败");
    }

    @ApiOperation(value = "获取用户当前话题状态")
    @GetMapping("getTopicUserStatus/{id}")
    public R getTopicUserStatus(@PathVariable String id, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            TopicStatusDto topicStatus = icTopicService.topicStatus(id, cUser.getId());
            Map<String, Object> statusMap = new HashMap<>(1);
            statusMap.put("topicStatus", topicStatus);
            return R.success("获取成功", statusMap);
        }
        return R.error("获取当前用户帖子状态失败");
    }

    @GetMapping("getAllCommentsByCid/{cid}")
    @ApiOperation(value = "根据帖子id查询所有评论")
    public R getAllCommentsByCid(@PathVariable String cid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            List<CommentDetail> commentDetailList = icCommentService.getAllCommentsByCid(cid, cUser.getId());
            return R.success("获取成功", commentDetailList);
        }
        return R.error("查询出错");
    }

    @ApiOperation(value = "参加/取消参加")
    @PostMapping("joinToActive")
    @Transactional
    public R joinToActive(@RequestBody JoinDto joinDto, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CActivity cActivity = icActivityService.getById(joinDto.getAid());
            if (StringUtils.isNotNull(cActivity)) {
                if (new Date().getTime() >= (cActivity.getTime().getTime() + 86400000 - 1)) {
                    return R.error("当前活动已截止");
                }
                CUser creater = icUserService.getById(cActivity.getPbid());
                Integer count = icActivityService.countJoin(joinDto.getAid(), cUser.getId());
                if (count == 0) {
                    if (cActivity.getPerLimit() != null && cActivity.getPerLimit() + joinDto.getPerson() <= cActivity.getPerson()) {
                        Integer joinPerson = cActivity.getPerLimit() + joinDto.getPerson();
                        cActivity.setPerLimit(joinPerson);
                        icActivityService.updateById(cActivity);
                        icActivityService.insertJoin(joinDto.getAid(), cUser.getId(), joinDto.getPerson(), joinDto.getRemark());
                        if (joinPerson >= cActivity.getPerson()) {
                            log.info("发信息给发布者");
                            List<WxMaSubscribeMessage.Data> dataList = new ArrayList<>();
                            dataList.add(new WxMaSubscribeMessage.Data() {{
                                setName("thing1");
                                setValue(cActivity.getTitle());
                            }});
                            dataList.add(new WxMaSubscribeMessage.Data() {{
                                setName("number3");
                                setValue(cActivity.getPerson().toString());
                            }});
                            dataList.add(new WxMaSubscribeMessage.Data() {{
                                setName("thing4");
                                setValue("当前活动已成团");
                            }});
                            try {
                                sendMsg(dataList, wxMineProperties.getTempId1(), creater.getOpenid());
                            } catch (WxErrorException e) {
                                e.printStackTrace();
                            }
                        } else {
                            log.info("未发信息");
                        }
                        return R.success("参加成功");
                    } else {
                        return R.error("当前活动剩余名额不足");
                    }
                } else {
                    cActivity.setPerLimit(cActivity.getPerLimit() - joinDto.getPerson());
                    icActivityService.updateById(cActivity);
                    icActivityService.deleteJoin(joinDto.getAid(), cUser.getId());
                    return R.success("取消参加成功");
                }
            }
        }
        return R.error("关注出错");
    }

    @ApiOperation(value = "获取活动当前用户状态")
    @GetMapping("getActivityUserStatus/{aid}")
    public R getActivityUserStatus(@PathVariable String aid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CActivity cActivity = icActivityService.getById(aid);
            if (StringUtils.isNotNull(cActivity)) {
                HashMap<String, Object> map = new HashMap<>();
                Integer appreciate = icActivityService.countAppreciate(aid, cUser.getId());
                QueryWrapper<CActOrder> wr = new QueryWrapper<CActOrder>();
                wr.eq("act_id", cActivity.getId()).eq("user_id", cUser.getId()).in("status", 1,2,5);
                CActOrder cActOrder = icActOrderService.getOne(wr);
//                JoinDto joinDto = icActivityService.getJoin(aid, cUser.getId());
                QueryWrapper<CAComment> wrapper = new QueryWrapper<>();
                wrapper.eq("aid", aid).orderByDesc("id");
                List<CAComment> cComments = icaCommentService.list(wrapper);

                List<Integer> commentAppreciate = new ArrayList<>();
                for (CAComment cComment : cComments) {
                    String cid = cComment.getId();
                    Integer cZan = icaCommentService.countCZan(cid, cUser.getId());
                    commentAppreciate.add(cZan);
                }
                map.put("appreciate", appreciate);
                map.put("join", cActOrder);
                map.put("commentAppreciate", commentAppreciate);

                return R.success("获取成功", map);
            }
        }
        return R.error("获取失败");
    }

    @ApiOperation(value = "活动评论")
    @PostMapping("writeAComment")
    public R writeAComment(@RequestBody CAComment caComment, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser) && StringUtils.isNotNull(caComment)) {
            try {
                checkImgAndText(null, caComment.getContent());
            } catch (WxErrorException e) {
                log.error("【用户ID】：" + cUser.getId());
                log.error("【违规内容】：" + e.getMessage());
                return R.error("严禁上传违规内容，您的违规操作已被记录！");
            }
            CActivity cActivity = icActivityService.getById(caComment.getAid());
            cActivity.setComments(cActivity.getComments() + 1);
            icActivityService.updateById(cActivity);
            caComment.setUid(cUser.getId());
            icaCommentService.save(caComment);
            icUserService.addPoints(cUser.getId(), 1);
            return R.success("评论成功");
        }
        return R.error("评论出错");
    }

    @ApiOperation(value = "活动评论点赞")
    @GetMapping("acommentAppreciate/{cid}")
    public R acommentAppreciate(@PathVariable String cid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CAComment caComment = icaCommentService.getById(cid);
            if (StringUtils.isNotNull(caComment)) {
                Integer count = icaCommentService.countCZan(cid, cUser.getId());
                if (count == 0) {
                    //删除点踩数
//                    Integer cai = icaCommentService.delCCai(aid, cUser.getId());
                    caComment.setLikes(caComment.getLikes() + 1);
                    icaCommentService.updateById(caComment);
                    icaCommentService.insertCZan(cid, cUser.getId());
                    return R.success("点赞成功");
                }
            }
        }
        return R.error("点赞出错");
    }

    @ApiOperation(value = "活动评论取消点赞")
    @GetMapping("acommentAppreciateCancel/{cid}")
    public R acommentAppreciateCancel(@PathVariable String cid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CAComment caComment = icaCommentService.getById(cid);
            if (StringUtils.isNotNull(caComment)) {
                Integer zan = icaCommentService.delCZan(cid, cUser.getId());
                if (zan == 1) {
                    caComment.setLikes(caComment.getLikes() - 1);
                    icaCommentService.updateById(caComment);
                    return R.success("取消点赞成功");
                }
            }
        }
        return R.error("取消点赞出错");
    }

    @GetMapping("getAllCommentsByAid/{aid}")
    @ApiOperation(value = "根据帖子id查询所有评论")
    public R getAllCommentsByAid(@PathVariable String aid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            List<CommentDetail> commentDetailList = icaCommentService.getAllCommentsByAid(aid, cUser.getId());
            return R.success("获取成功", commentDetailList);
        }
        return R.error("查询出错");
    }

    /**
     * @param pid
     * @param request
     * @return
     */
    @ApiOperation(value = "收藏/取消收藏")
    @GetMapping("placeToCollect/{pid}")
    public R placeToCollect(@PathVariable String pid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CPlace cPlace = icPlaceService.getById(pid);
            if (StringUtils.isNotNull(cPlace)) {
                Integer count = icPlaceService.countCollect(pid, cUser.getId());
                if (count == 0) {
                    cPlace.setCollects(cPlace.getCollects() + 1);
                    icPlaceService.updateById(cPlace);
                    icPlaceService.insertCollect(pid, cUser.getId());
                    return R.success("收藏成功");
                } else {
                    cPlace.setCollects(cPlace.getCollects() - 1);
                    icPlaceService.updateById(cPlace);
                    icPlaceService.deleteCollect(pid, cUser.getId());
                    return R.success("取消收藏成功");
                }
            }
        }
        return R.error("收藏出错");
    }

    @ApiOperation(value = "获取个人信息")
    @GetMapping("getSelfDetail/{uid}")
    @Cacheable(value = "getSelfDetail", key = "'uid_'+#uid", cacheManager = "redisExpire")
    public R getSelfDetail(@PathVariable String uid) {
        if (StringUtils.isNotNull(uid)) {
            CUser cUser = icUserService.getById(uid);
            BigDecimal ratio = icUserService.getRatioById(cUser.getId());
            Map cradMap = icUserService.getCountByUid(cUser.getId());
            Integer follows = icCardService.getFollowsCountByUid(cUser.getId());
            Integer critics = icUserService.getContextCountByUid(cUser.getId());
            List<String> letterList = redisCache.getCacheList("msg:uid:" + cUser.getId());
            Integer newLetter = 0;
            if (letterList != null && letterList.size() > 0) {
                for (String json : letterList) {
                    LetterWriterDto letterWriterDto = JSONUtil.toBean(json, LetterWriterDto.class);
                    if (!letterWriterDto.getRead()) {
                        newLetter++;
                    }
                }
            }
            SelfDetail selfDetail = new SelfDetail();
            selfDetail.setCashs(cUser.getCashs() == null ? new BigDecimal("0") : cUser.getCashs());
            selfDetail.setPoints(cUser.getPoints());
            selfDetail.setRatio(ratio == null ? 0 : ratio.intValue());
            selfDetail.setCallers(cradMap.get("callers") == null ? 0 : Integer.valueOf(cradMap.get("callers").toString()));
            selfDetail.setFans(cradMap.get("fans") == null ? 0 : Integer.valueOf(cradMap.get("fans").toString()));
            selfDetail.setCritics(critics);
            selfDetail.setFollows(follows);
            selfDetail.setNewLetters(newLetter);
            return R.success("查询成功", selfDetail);
        }
        return R.error("查询出错");
    }

    @ApiOperation(value = "获取个人信息")
    @PostMapping("saveUserInfo")
    public R saveUserInfo(@RequestBody CUser cUser, HttpServletRequest request) {
        CUser user = getCUserByRequest(request);
        if (user != null) {
            cUser.setId(user.getId());
            icUserService.saveOrUpdate(cUser);
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    /**
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "用户新增话题")
    @PostMapping("createTopic")
    public R createTopic(@RequestBody CTopic topic, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            //            鉴定敏感图文
            try {
                checkImgAndText(topic.getCover(), topic.getTitle() + topic.getDetail());
                checkImgAndText(topic.getImage(), null);
            } catch (WxErrorException e) {
                log.error("【用户ID】：" + cUser.getId());
                log.error("【违规内容】：" + e.getMessage());
                return R.error("严禁上传违规内容，您的违规操作已被记录！");
            }
            topic.setUid(cUser.getId());
            icTopicService.save(topic);
            icUserService.addPoints(cUser.getId(), 20);
            List<CUser> list = icUserService.list();
            list.forEach(e -> {
                Map<String, Object> topicMap = redisCache.getCacheMap("sign:topic:uid:" + e.getId());
                topicMap.put(topic.getId(), 1);
                log.info("topicMap1 : {}", JSONUtil.toJsonStr(topicMap));
                redisCache.setCacheMap("sign:topic:uid:" + e.getId(), topicMap);

                QueryWrapper<CTopicCover> wrapper = new QueryWrapper();
                wrapper.eq("topic_type", topic.getType());
                CTopicCover cTopicCover = icTopicCoverService.getOne(wrapper);
                Map<String, Object> coverMap = redisCache.getCacheMap("sign:cover:uid:" + e.getId());
                coverMap.put(cTopicCover.getId().toString(), 1);
                log.info("coverMap : {}", JSONUtil.toJsonStr(coverMap));
                redisCache.setCacheMap("sign:cover:uid:" + e.getId(), coverMap);
            });
            return R.success("新增成功");
        }
        return R.error("新增出错");
    }

    /**
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "用户修改话题")
    @PostMapping("modifyTopic")
    public R modifyTopic(@RequestBody CTopic topic, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            topic.setUid(cUser.getId());
            icTopicService.updateById(topic);
            return R.success("修改成功");
        }
        return R.error("修改出错");
    }

    @ApiOperation(value = "获取用户话题")
    @GetMapping("getTopicByUid/{pageNum}/{pageSize}")
    public R getTopicByUid(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<TopicUserDto> topics = icTopicService.getTopicByUid(cUser.getId(), pageNum, pageSize);
            return R.success("查询成功", topics);
        }
        return R.error("查询失败");
    }

    @ApiOperation(value = "删除话题")
    @PostMapping("deleteTopic")
    public R deleteTopic(@RequestBody CTopic topic, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            boolean b = icTopicService.removeById(topic.getId());
            if (b) {
                return R.success("删除成功");
            }
        }
        return R.error("删除出错");
    }

    @ApiOperation(value = "删除线下活动")
    @PostMapping("deleteActivity")
    public R deleteActivity(@RequestBody CActivity activity, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            CActivity cActivity = icActivityService.getById(activity.getId());
            if (cActivity.getPerLimit() > 0 && new Date().getTime() < (cActivity.getTime().getTime() + 86400000 - 1)) {
                return R.error("有人参与了活动，不能删除哦");
            }
            boolean b = icActivityService.removeById(activity.getId());
            if (b) {
                return R.success("删除成功");
            }
        }
        return R.error("删除出错");
    }


    @ApiOperation(value = "获取用户话帖子")
    @GetMapping("getCardByUid/{pageNum}/{pageSize}")
    public R getCardByUid(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<CardUserDto> cards = icCardService.getCardByUid(cUser.getId(), pageNum, pageSize);
            cards.forEach(e -> {
                e.setImgs(e.getImgs().split(",")[0]);
            });
            return R.success("查询成功", cards);
        }
        return R.error("查询失败");
    }

    @ApiOperation(value = "删除帖子")
    @PostMapping("deleteCard")
    public R deleteCard(@RequestBody CCard card, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            boolean b = icCardService.removeById(card.getId());
            if (b) {
                return R.success("删除成功");
            }
        }
        return R.error("删除出错");
    }


    /**
     * @param pid
     * @param request
     * @return
     */
    @ApiOperation(value = "玩场喜欢/取消喜欢")
    @GetMapping("placeToLike/{pid}")
    public R placeToLike(@PathVariable String pid, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser)) {
            CPlace cPlace = icPlaceService.getById(pid);
            if (StringUtils.isNotNull(cPlace)) {
                Integer count = icPlaceService.countLike(pid, cUser.getId());
                if (count == 0) {
                    cPlace.setLikes(cPlace.getLikes() + 1);
                    icPlaceService.updateById(cPlace);
                    icPlaceService.insertLike(pid, cUser.getId());
                    return R.success("喜欢成功");
                } else {
                    cPlace.setLikes(cPlace.getLikes() - 1);
                    icPlaceService.updateById(cPlace);
                    icPlaceService.deleteLike(pid, cUser.getId());
                    return R.success("取消喜欢成功");
                }
            }
        }
        return R.error("喜欢出错");
    }

    @ApiOperation(value = "获取用户订单列表")
    @GetMapping("getOrderByUid/{pageNum}/{pageSize}")
    public R getOrderByUid(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<OrderUserDto> orders = icOrderService.getOrderByUid(cUser.getId(), pageNum, pageSize);
            return R.success("查询成功", orders);
        }
        return R.error("查询失败");
    }

    @ApiOperation(value = "退款申请")
    @PostMapping("refundAudit")
    public R refundAudit(@RequestBody CRefundAudit refundAudit, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {

            if (StringUtils.isEmpty(refundAudit.getOrderId())) {
                return R.error("订单id为空");
            }
            if (StringUtils.isEmpty(refundAudit.getOrderNo())) {
                return R.error("订单编号为空");
            }
            COrder cOrder = icOrderService.getById(refundAudit.getOrderId());
            if (cOrder.getStatus() != 1) {
                return R.error("当前订单不是已支付状态");
            }

            refundAudit.setUserId(cUser.getId());
            refundAudit.setRefundTime(new Date());
            refundAudit.setRefundNo(OrderNoUtil.getD(null));
            boolean save = icRefundAuditService.save(refundAudit);
            if (save) {
                // 退款中
                cOrder.setStatus(3);
                icOrderService.updateById(cOrder);
                return R.success("退款申请成功");
            }
        }
        return R.error("退款申请出错");
    }

    /**
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "用户申请公司入驻")
    @PostMapping("createCompany")
    public R createCompany(@RequestBody CCompany company, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            company.setUserId(cUser.getId());
            icCompanyService.save(company);
            return R.success("新增成功");
        }
        return R.error("新增出错");
    }

    /**
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "获取公司信息")
    @GetMapping("getCompanyByUid")
    public R getCompanyByUid(HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            QueryWrapper<CCompany> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", cUser.getId());
            CCompany company = icCompanyService.getOne(wrapper);
            return R.success(company);
        }
        return R.error("查询出错");
    }

    @ApiOperation(value = "获取用户线下活动")
    @GetMapping("getActivityByUid/{pageNum}/{pageSize}")
    public R getActivityByUid(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<ActivityDetail> activitys = icActivityService.getActivityByUid(cUser.getId(), pageNum, pageSize);
            activitys.forEach(e -> {
                ArrayList<String> imgs = new ArrayList<>(Arrays.asList(e.getImg().split(",")));
                e.setImg(imgs.get(0));
                if (new Date().getTime() >= (e.getTime().getTime() + 86400000 - 1)) {
                    e.setActivityStatus(2);
                } else {
                    e.setActivityStatus(1);
                }
            });
            return R.success("查询成功", activitys);
        }
        return R.error("查询失败");
    }


    /**
     * 检测视频
     *
     * @param video
     * @return
     */
    private Boolean checkVideo(String video) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(wxMineProperties.getAppId());
        if (StringUtils.isNotEmpty(video)) {
            wxService.getSecCheckService().mediaCheckAsync(wxMineProperties.getBaseUrl() + video, 1);
        }
        return true;
    }

    @ApiOperation(value = "玩场咨询")
    @PostMapping("placeConsult")
    public R placeConsult(@RequestBody CPlaceConsult placeConsult, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (StringUtils.isNotNull(cUser) && StringUtils.isNotNull(placeConsult)) {
            placeConsult.setUserId(cUser.getId());
            icPlaceConsultService.save(placeConsult);
            icUserService.addPoints(cUser.getId(), 1);
            return R.success("咨询成功");
        }
        return R.error("咨询出错");
    }

    /**
     * 查询话题轮播图
     *
     * @return
     */
    @GetMapping("getTopicCover")
    public R getTopicCover(HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<TopicCover> topicList = icTopicCoverService.getCover();
            Map<String, Object> coverMap = redisCache.getCacheMap("sign:cover:uid:" + cUser.getId());
            log.info("query coverMap: {}", JSONUtil.toJsonStr(coverMap));
            topicList.forEach(e -> {
                Integer sign = (Integer) coverMap.get(e.getId().toString());
                e.setSign(sign == null ? 0 : sign);
            });
            return R.success("查询成功", topicList);
        }
        return R.error("查询失败");
    }

    @GetMapping("getTopicDetailById/{id}")
    public R getTopicDetailById(@PathVariable String id, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            CTopic cTopic = icTopicService.getById(id);
            if (StringUtils.isNotNull(cTopic)) {
                QueryWrapper<CTopicCover> wrapper = new QueryWrapper();
                wrapper.eq("topic_type", cTopic.getType());
                CTopicCover cTopicCover = icTopicCoverService.getOne(wrapper);
                Map<String, Object> coverMap = redisCache.getCacheMap("sign:cover:uid:" + cUser.getId());
                log.info("set coverMap: {}", JSONUtil.toJsonStr(coverMap));
                log.info("set cover: {}", coverMap.get(cTopicCover.getId().toString()));
                coverMap.put(cTopicCover.getId().toString(), 0);
                redisCache.setCacheMap("sign:cover:uid:" + cUser.getId(), coverMap);

                Map<String, Object> topicMap = redisCache.getCacheMap("sign:topic:uid:" + cUser.getId());
                topicMap.put(cTopic.getId(), 0);
                redisCache.setCacheMap("sign:topic:uid:" + cUser.getId(), topicMap);


                cTopic.setViews(cTopic.getViews() + 1);
                icTopicService.updateById(cTopic);
                TopicDetail topicDetail = icTopicService.getDetailById(id);

                if (StringUtils.isNotEmpty(topicDetail.getImage())) {
                    ArrayList<String> imgList = new ArrayList<>(Arrays.asList(topicDetail.getImage().split(",")));
                    topicDetail.setImageList(imgList);
                }
                return R.success("获取成功", topicDetail);
            }
        }
        return R.error("查询失败");
    }

    /**
     * 获取话题列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getTopicList/{search}/{type}/{pageNum}/{pageSize}")
    public R getTopicList(
                          @PathVariable Integer search,
                          @PathVariable Integer type,
                          @PathVariable Integer pageNum,
                          @PathVariable Integer pageSize,
                          HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            Map<String, Object> topicMap = redisCache.getCacheMap("sign:topic:uid:" + cUser.getId());
            ArrayList<TopicUserDto> topicList = null;
            // search 0：最新 1：精选 2：关注
            if (search == 0) {
                topicList = icTopicService.getSiftTopicList(type,pageNum, pageSize);
            } else if (search == 1){
                topicList = icTopicService.getTopicList(type, pageNum, pageSize);
            } else if (search == 2){
                 topicList = icTopicService.getFollowTopicList(cUser.getId(), pageNum, pageSize);
            }
            if (topicList != null && topicList.size() > 0){
                topicList.forEach(e -> {
                    Integer sign = (Integer) topicMap.get(e.getId());
                    e.setSign(sign == null ? 0 : sign);
                });
            }
            return R.success("getTopicList", topicList);
        }
        return R.error("查询失败");
    }

    /**
     * 根据话题查询热门帖子
     *
     * @param id
     * @return
     */
    @GetMapping("getHotCardByTopicId/{id}/{pageNum}/{pageSize}")
    public R getHotCardByTopicId(@PathVariable String id, @PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<CardUserDto> cardUserDtos = icCardService.getHotCard(id, pageNum, pageSize);
            Map<String, Object> cardMap = redisCache.getCacheMap("sign:card:uid:" + cUser.getId());

            if (cardUserDtos != null) {
                cardUserDtos.forEach(e -> {
                    if (StringUtils.isNotNull(e.getImgs())) {
                        ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImgs().split(",")));
                        e.setImgList(imgList);
                        e.setImgs(null);
                    }
                    if (StringUtils.isNotNull(e.getVids())) {
                        ArrayList<String> vidList = new ArrayList<>(Arrays.asList(e.getVids().split(",")));
                        e.setVidList(vidList);
                        e.setVids(null);
                    }
                    Integer sign = (Integer) cardMap.get(e.getId());
                    e.setSn(sign == null ? 0 : sign);
                });
            }
            return R.success("获取成功", cardUserDtos);
        }
        return R.error("获取失败");
    }

    /**
     * 根据话题查询最新帖子
     *
     * @param id
     * @return
     */
    @GetMapping("getNewCardByTopicId/{id}/{pageNum}/{pageSize}")
    public R getNewCardByTopicId(@PathVariable String id, @PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<CardUserDto> cardUserDtos = icCardService.getNewCard(id, pageNum, pageSize);
            Map<String, Object> cardMap = redisCache.getCacheMap("sign:card:uid:" + cUser.getId());

            if (cardUserDtos != null) {
                cardUserDtos.forEach(e -> {
                    if (StringUtils.isNotNull(e.getImgs())) {
                        ArrayList<String> imgList = new ArrayList<>(Arrays.asList(e.getImgs().split(",")));
                        e.setImgList(imgList);
                        e.setImgs(null);
                    }
                    if (StringUtils.isNotNull(e.getVids())) {
                        ArrayList<String> vidList = new ArrayList<>(Arrays.asList(e.getVids().split(",")));
                        e.setVidList(vidList);
                        e.setVids(null);
                    }
                    Integer sign = (Integer) cardMap.get(e.getId());
                    e.setSn(sign == null ? 0 : sign);
                });
            }
            return R.success("获取成功", cardUserDtos);
        }
        return R.error("获取失败");
    }


    @GetMapping("getCardDetailById/{id}")
    public R getCardDetailById(@PathVariable String id, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            CCard cCard = icCardService.getById(id);
            if (StringUtils.isNotNull(cCard)) {

                Map<String, Object> topicMap = redisCache.getCacheMap("sign:topic:uid:" + cCard.getTopicId());
                topicMap.put(cCard.getTopicId(), 0);
                redisCache.setCacheMap("sign:topic:uid:" + cUser.getId(), topicMap);

                Map<String, Object> cardMap = redisCache.getCacheMap("sign:card:uid:" + cCard.getId());
                cardMap.put(cCard.getId(), 0);
                redisCache.setCacheMap("sign:card:uid:" + cUser.getId(), cardMap);


                cCard.setViews(cCard.getViews() + 1);
                icCardService.updateById(cCard);
                CardDetail cardDetail = icCardService.getDetailById(id);
                List<String> avatarList = icCardService.getLikes(id);
                cardDetail.setAvatarLikes(avatarList);
                return R.success("获取成功", cardDetail);
            }
        }
        return R.error("查询失败");
    }

    /**
     * 查询活动户列表
     *
     * @return
     */
    @GetMapping("getActivityUserList/{id}")
    public R getActivityUserList(@PathVariable String id) {
        List<UserDetail> activityUserList = icActOrderService.getActUserList(id);
//        List<UserDetail> activityUserList = icActivityService.getActivityUserList(id);
        return R.success("查询成功", activityUserList);
    }

    /**
     * 获取获取所有评论列表
     *
     * @return
     */
    @GetMapping("getContextByUser/{pageNum}/{pageSize}")
    public R getContextByUser(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<ContextDetail> contexts = icUserService.getContextByUid(pageNum, pageSize, cUser.getId());
            return R.success(contexts);
        }
        return R.error("获取失败");
    }

    /**
     * 查询话题用户列表
     *
     * @return
     */
    @GetMapping("getFollower")
    public R getTopicUserList(HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        List<UserDetail> follower = icUserService.getFollower(cUser.getId());
        return R.success("查询成功", follower);
    }

    /**
     * 查询话题用户列表
     *
     * @return
     */
    @GetMapping("getFanser")
    public R getFanser(HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        List<UserDetail> follower = icUserService.getFanser(cUser.getId());
        return R.success("查询成功", follower);
    }

    /**
     * 查询话题用户列表
     *
     * @return
     */
    @GetMapping("getCeller")
    public R getCeller(HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        Map count = icUserService.getCountByUid(cUser.getId());
        List<UserDetail> follower = icUserService.getCeller(Integer.valueOf(count.get("callers").toString()));
        return R.success("查询成功", follower);
    }

    /**
     * 查询话题用户列表
     *
     * @return
     */
    @PostMapping("changeUserAvatar")
    public R changeUserAvatar(@RequestBody CUser u, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            CUser user = icUserService.getById(cUser.getId());
            user.setAvatar(u.getAvatar());
            icUserService.updateById(user);
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }


    /**
     * 获取关注的话题列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("getFollowTopicList/{pageNum}/{pageSize}")
    public R getFollowTopicList(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            ArrayList<TopicUserDto> topicList = icTopicService.getFollowTopicList(cUser.getId(), pageNum, pageSize);
            return R.success("getFollowTopicList", topicList);
        }
        return R.error("查询失败");
    }

    /**
     * 写私信
     *
     * @return
     */
    @PostMapping("writerLetter")
    public R writeLetter(@RequestBody LetterWriterDto letterWriterDto, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            letterWriterDto.setUid(cUser.getId());
            letterWriterDto.setTime(new Date());
            letterWriterDto.setAvatar(cUser.getAvatar());
            letterWriterDto.setNickname(cUser.getNickname());
            letterWriterDto.setRead(Boolean.FALSE);
            String letterJson = JSONUtil.toJsonStr(letterWriterDto);
            List<String> letterList = new ArrayList<>();
            letterList.add(letterJson);
            redisCache.setCacheList("msg:uid:" + letterWriterDto.getToId(), letterList);
            return R.success("发送成功");
        }
        return R.error("发送失败");
    }


    @GetMapping("setLetter")
    public R setLetter(HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<String> letterList = redisCache.getCacheList("msg:uid:" + cUser.getId());
            if (letterList != null && letterList.size() > 0) {
                List<String> toDo = new ArrayList<>();
                for (String json : letterList) {
                    //将未读信息设为已读
                    LetterWriterDto letterWriterDto = JSONUtil.toBean(json, LetterWriterDto.class);
                    letterWriterDto.setRead(Boolean.TRUE);
                    toDo.add(JSONUtil.toJsonStr(letterWriterDto));
                    log.info("letter json: {}", JSONUtil.toJsonStr(letterWriterDto));
                }
                boolean b = redisCache.deleteObject("msg:uid:" + cUser.getId());
                if (b){
                    redisCache.setCacheList("msg:uid:" + cUser.getId(), toDo);
                }
                return R.success("获取成功", null);
            }
        }
        return R.error("获取失败");
    }

    @GetMapping("getLetterList")
    public R getLetterList(HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<String> letterList = redisCache.getCacheList("msg:uid:" + cUser.getId());
            if (letterList != null && letterList.size() > 0) {
                List<LetterWriterDto> letterWriterDtos = new ArrayList<>();
                for (String json : letterList) {
                    LetterWriterDto letterWriterDto = JSONUtil.toBean(json, LetterWriterDto.class);
                    letterWriterDtos.add(letterWriterDto);
                }
                List<LetterWriterDto> collect = letterWriterDtos.stream().sorted(Comparator.comparing(LetterWriterDto::getTime).reversed()).collect(Collectors.toList());
                return R.success("获取成功", collect);
            }
            return R.success("获取成功", null);
        }
        return R.error("获取失败");
    }
    @GetMapping("getFollowStatus/{fid}")
    public R getFollowStatus(@PathVariable String fid, HttpServletRequest request){
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            Boolean isFollow = icUserService.followStatus(cUser.getId(), fid);
            return R.success("成功",isFollow);

        }
        return R.error("获取失败");
    }

    @GetMapping("getMyJoin")
    public R getMyJoin(HttpServletRequest request){
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            List<UserDetail> orderList = icActOrderService.getMyActOrderList(cUser.getId());
            return R.success("成功",orderList);

        }
        return R.error("获取失败");
    }

    /***
     * 鉴定图文
     * @param img
     * @param text
     * @return
     * @throws WxErrorException
     */
    private Boolean checkImgAndText(String img, String text) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(wxMineProperties.getAppId());

        if (StringUtils.isNotEmpty(img)) {
            ArrayList<String> imgList = new ArrayList<>(Arrays.asList(img.split(",")));
            for (String image : imgList) {
                wxService.getSecCheckService().checkImage(wxMineProperties.getBaseUrl() + image);
            }
        }
        if (StringUtils.isNotEmpty(text)) {
            wxService.getSecCheckService().checkMessage(text);
        }
        return true;
    }

    /**
     * 发消息
     *
     * @param dataList
     * @param tempId
     * @param openId
     * @throws WxErrorException
     */
    private void sendMsg(List<WxMaSubscribeMessage.Data> dataList, String tempId, String openId) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(wxMineProperties.getAppId());
        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setData(dataList);
        wxMaSubscribeMessage.setTemplateId(tempId);
        wxMaSubscribeMessage.setToUser(openId);
        wxService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
    }

    /**
     * 获取二维码
     */
    private byte[] getQrcode(String path, String scene) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(wxMineProperties.getAppId());
        WxMaCodeLineColor wxMaCodeLineColor = new WxMaCodeLineColor("138", "250", "178");
        byte[] wxaCodeUnlimitBytes = wxService.getQrcodeService().createWxaCodeUnlimitBytes(scene, path, 400, false, wxMaCodeLineColor, true);
        return wxaCodeUnlimitBytes;
    }

    private CUser getCUserByRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        System.out.println(authorization);
        String token = wxLoginService.getToken(request);
        WxUser wxUser = wxLoginService.getWxUser(token);
        String openid = wxUser.getOpenid();
        QueryWrapper<CUser> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        return icUserService.getOne(wrapper);
    }
}
