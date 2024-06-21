package net.geedge.asw.module.sys.service.impl;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.geedge.asw.common.util.T;
import net.geedge.asw.module.sys.dao.SysRoleDao;
import net.geedge.asw.module.sys.entity.SysRoleEntity;
import net.geedge.asw.module.sys.entity.SysRoleMenuEntity;
import net.geedge.asw.module.sys.service.ISysRoleMenuService;
import net.geedge.asw.module.sys.service.ISysRoleService;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements ISysRoleService {

	@Autowired
	private ISysRoleMenuService roleMenuService;

	@Override
	@Transactional
	public void saveOrUpdateRole(SysRoleEntity entity) {
		// 删除 sys_role_menu 关联数据
		if (T.StrUtil.isBlank(entity.getId())) {
			roleMenuService.remove(new QueryWrapper<SysRoleMenuEntity>().eq("role_id", entity.getId()));
		}
		// 保存 role
		this.saveOrUpdate(entity);
		// 保存 role 和 menu关联关系
		List<SysRoleMenuEntity> rmList = T.ListUtil.list(false);
		Stream.of(entity.getMenuIds()).forEach(menuId -> {
			rmList.add(SysRoleMenuEntity.builder().roleId(entity.getId()).menuId(menuId).build());
		});
		roleMenuService.saveBatch(rmList);
	}

}
