package net.geedge.asw.module.sys.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.geedge.asw.module.sys.dao.SysConfigDao;
import net.geedge.asw.module.sys.entity.SysConfigEntity;
import net.geedge.asw.module.sys.service.ISysConfigService;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfigEntity> implements ISysConfigService {

}
