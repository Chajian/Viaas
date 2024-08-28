package com.viaas.docker.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viaas.docker.entity.PermissionGroup;
import com.viaas.docker.mapper.PermissionGroupMapper;
import com.viaas.docker.service.PermissionGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sn
 */
@Slf4j
@Service
public class PermissionGroupServiceImpl extends ServiceImpl<PermissionGroupMapper, PermissionGroup> implements PermissionGroupService {
}
