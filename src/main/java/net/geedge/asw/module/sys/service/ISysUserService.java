package net.geedge.asw.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.geedge.asw.module.sys.entity.SysUserEntity;

public interface ISysUserService extends IService<SysUserEntity>{

	public void saveOrUpdateUser(SysUserEntity entity);
}
