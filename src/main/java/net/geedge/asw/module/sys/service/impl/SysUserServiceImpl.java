package net.geedge.asw.module.sys.service.impl;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.geedge.asw.common.util.T;
import net.geedge.asw.module.sys.dao.SysUserDao;
import net.geedge.asw.module.sys.entity.SysUserEntity;
import net.geedge.asw.module.sys.entity.SysUserRoleEntity;
import net.geedge.asw.module.sys.service.ISysUserService;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements ISysUserService {

	@Autowired
	private SysUserRoleServiceImpl userRoleService;

	@Override
	@Transactional
	public void saveOrUpdateUser(SysUserEntity entity) {
		if (T.StrUtil.isNotBlank(entity.getId())) {
			userRoleService.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id", entity.getId()));
		}
		// 保存 user
		this.saveOrUpdate(entity);
		// 保存 user role关系
		List<SysUserRoleEntity> urList = T.ListUtil.list(false);
		Stream.of(entity.getRoleIds()).forEach(roleId -> {
			urList.add(SysUserRoleEntity.builder().roleId(roleId).userId(entity.getId()).build());
		});
		userRoleService.saveBatch(urList);
	}

}
