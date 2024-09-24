package com.viaas.docker.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.viaas.certification.api.util.JwtUtil;
import com.viaas.docker.common.Constants;
import com.viaas.docker.common.Result;
import com.viaas.docker.entity.*;
import com.viaas.docker.entity.dto.AddContainer;
import com.viaas.docker.entity.dto.AddOrder;
import com.viaas.docker.execption.CustomException;
import com.viaas.docker.mapper.*;
import com.viaas.docker.service.OrderService;
import com.viaas.docker.task.TaskThreadPool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * 订单接口
 */
@Tag(name = "订单接口")
@RestController
@RequestMapping("/ibs/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    PacketMapper packetMapper;

    @Autowired
    HardwareMapper hardwareMapper;

    @Autowired
    TaskThreadPool taskThreadPool;

    @Autowired
    ImageMapper imageMapper;

    @Autowired
    WalletMapper walletMapper;

    @Autowired
    ContainerMapper containerMapper;

    @Autowired
    private JwtUtil jwtUtil;

    //是否开启支付功能
    boolean enablePay = false;

    /**
     * 创建订单
     *
     * @param token
     * @return
     */
    @Operation(summary = "创建订单接口")
    @Transactional
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @RequestParam("id") int packetId,
                                     @RequestBody AddContainer addContainer) throws ParseException {
        Packet packet = packetMapper.selectById(packetId);
        Hardware hard = hardwareMapper.selectById(packet.getHardwareId());
        if(packet == null || hard == null) {
            return Result.error(Constants.NULL);
        }

        //容器重命检测
        String name = jwtUtil.extractUserId(token) + "-" + addContainer.getContainerName();
        boolean isRepeat = containerMapper.exists(new QueryWrapper<Container>().eq("name_c", name));
        if(isRepeat) {
            return Result.error(Constants.CONTAINER_REPEAT_NAME);
        }

        //镜像检测
        String imageName = addContainer.getImageName();
        boolean exist = imageMapper.exists(new QueryWrapper<Image>().eq("name", imageName));
        if(!exist) {
            return Result.error(Constants.IMAGE_NOT_EXIST);
        }

        //发送消息
        AddOrder addOrder = new AddOrder(packetId, jwtUtil.extractUserId(token), addContainer, 100);
        Order result = orderService.sendMessage(addOrder);

        return Result.success(Constants.CODE_200.getCode(), "success", result);
    }

    /**
     * 支付功能
     * @return
     */
    @Operation(summary = "支付订单")
    @Transactional
    @PutMapping("/pay/{id}")
    public Result payOrder(@PathVariable("id") int orderId,
                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Order order = orderService.getById(orderId);
        Long userId = jwtUtil.extractUserId(token);

        if(enablePay) { //开启支付功能
            Wallet wallet = walletMapper.selectOne(new QueryWrapper<Wallet>().eq("user_id", userId));
            if(wallet.getBalance() < order.getMoney()) {
                throw new CustomException(Constants.PAY_NOT_ENOUGH_MONEY);
            }
            wallet.setBalance(wallet.getBalance() - order.getMoney());
            walletMapper.updateById(wallet);
        }

        if(orderService.paied(order)) {
            return Result.success(Constants.CODE_200.getCode(), "支付成功!", null);
        }

        return Result.error(Constants.PAY_FAIL);
    }

    /**
     * 获取用户的订单
     * @param token
     * @return
     */
    @Operation(summary = "获取用户的订单")
    @GetMapping("/get/{page}/{pageSize}")
    public Result getOrders(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                            @PathVariable(value = "page") Integer page,
                            @PathVariable(value = "pageSize") Integer pageSize) {
        return Result.success(Constants.CODE_200, orderService.getAllOrderByUser(jwtUtil.extractUserId(token)));
    }
}
