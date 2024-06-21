package net.geedge.asw.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.geedge.asw.module.sys.entity.SysRoleEntity;

public interface ISysRoleService extends IService<SysRoleEntity>{

	public void saveOrUpdateRole(SysRoleEntity entity);
}
