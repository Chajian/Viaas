package com.viaas.docker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.viaas.docker.entity.Wallet;

/**
 * 钱包服务
 * @author Yanglin
 */
public interface WalletService extends IService<Wallet> {
    /**
     * 支付功能
     * @param payment
     */
    Wallet pay(double payment,int userId);

    /**
     * 充值功能
     * @param top
     */
    Wallet topUp(double top,int userId);
}
