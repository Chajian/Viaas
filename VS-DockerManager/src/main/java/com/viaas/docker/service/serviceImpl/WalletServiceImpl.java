package com.viaas.docker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viaas.docker.common.Constants;
import com.viaas.docker.entity.Wallet;
import com.viaas.docker.execption.CustomException;
import com.viaas.docker.mapper.WalletMapper;
import com.viaas.docker.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements WalletService {
    @Override
    @Transactional
    public Wallet pay(double payment,int userId) {
        Wallet wallet = getOne(new QueryWrapper<Wallet>().eq("user_id",userId));
        if(wallet.getBalance()<payment)
            throw new CustomException(Constants.PAY_NOT_ENOUGH_MONEY);
        wallet.setBalance(wallet.getBalance()-payment);
        //update in db
        updateById(wallet);
        return wallet;
    }

    @Override
    @Transactional
    public Wallet topUp(double top,int userId) {
        Wallet wallet = getOne(new QueryWrapper<Wallet>().eq("user_id",userId));
        wallet.setBalance(wallet.getBalance()+top);
        updateById(wallet);
        return wallet;
    }
}
