package com.viaas.docker.controller;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.viaas.docker.common.Constants;
import com.viaas.docker.common.Result;
import com.viaas.docker.entity.Image;
import com.viaas.docker.entity.dto.ImagesParam;
import com.viaas.docker.entity.dto.PullImages;
import com.viaas.docker.entity.vo.ImageVo;
import com.viaas.docker.execption.CustomException;
import com.viaas.docker.service.ImageService;
import com.viaas.docker.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "镜像接口")
@RestController
@RequestMapping("/ibs/api/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    /**
     * 获取镜像列表
     * author chen
     * @return 镜像列表
     */
    @Operation(summary = "获取镜像列表接口")
    @PostMapping
    public Result getImages(@RequestBody(required = false) ImagesParam imagesParam,
                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if(imagesParam.isCenter()) {
            Map<String, Image> imageMap = imageService.getImagesByDatabase(imagesParam, JwtUtil.getUserId(token))
                    .stream()
                    .filter(image -> image.getName() != null)
                    .collect(Collectors.toMap(Image::getName, image -> image, (existingValue, newValue) -> existingValue));

            List<Image> images2 = imageService.dockerObjectToImage(imageService.getImages(imagesParam, JwtUtil.getUserId(token)));
            List<ImageVo> imageVos = new ArrayList<>();
            for(Image image : images2){
                ImageVo imageVo = new ImageVo();
                imageVo.toImageVo(image);
                imageVo.setInstall(imageMap.get(imageVo.getName()) != null);
                imageVos.add(imageVo);
            }
            return Result.success(Constants.CODE_200, imageVos);
        } else {
            List<Image> images = imageService.dockerObjectToImage(imageService.getImages(imagesParam, JwtUtil.getUserId(token)));
            List<ImageVo> imageVos = new ArrayList<>();
            for(Image image : images){
                ImageVo imageVo = new ImageVo();
                imageVo.toImageVo(image);
                imageVo.setNewest(true);
                imageVos.add(imageVo);
            }
            return Result.success(Constants.CODE_200, imageVos);
        }
    }

    /** 拉取镜像
     * author chen
     * @return code 200 msg success
     */
    @Operation(summary = "拉取镜像接口")
    @PostMapping("/pull")
    public Result pull(@RequestBody PullImages pullImages){
        String fullName = pullImages.getName() + pullImages.getTag();
        //检测数据库中是否存在iamge
        Image image = imageService.getImageByName(fullName);
        if(image != null)
            throw new CustomException(Constants.IMAGE_ALEARY_EXIST);
        //检测image仓库中是否存在image
        InspectImageResponse inspectImageResponse = (InspectImageResponse) imageService.getImageObjectByName(fullName);
        if(inspectImageResponse != null) {
            image = new Image();
            image.setName(fullName);
            image.setTag(pullImages.getTag());
            image.setAuthor(inspectImageResponse.getAuthor());
            image.setSize(inspectImageResponse.getSize());
            imageService.save(image);
            throw new CustomException(Constants.IMAGE_ALEARY_EXIST);
        }

        return imageService.pull(pullImages.getName(), pullImages.getTag());
    }

    /**
     * build镜像通过Dockerfile文件
     * @param imageName
     * @return
     */
    @Operation(summary = "通过Dockerfile构建镜像")
    @PostMapping("/build")
    public Result build(String imageName, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        String account = JwtUtil.getUserAccount(token);
        return imageService.build(imageName, account);
    }
}
