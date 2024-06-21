package net.geedge.asw.module.sys.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.log.Log;
import net.geedge.asw.common.util.ASWException;
import net.geedge.asw.common.util.Constants;
import net.geedge.asw.common.util.RCode;
import net.geedge.asw.common.util.T;
import net.geedge.asw.module.sys.dao.SysRoleDao;
import net.geedge.asw.module.sys.dao.SysUserDao;
import net.geedge.asw.module.sys.entity.SysMenuEntity;
import net.geedge.asw.module.sys.entity.SysRoleEntity;
import net.geedge.asw.module.sys.entity.SysUserEntity;
import net.geedge.asw.module.sys.service.ISysAuthService;

@Service
public class SysAuthServiceImpl implements ISysAuthService {

	private static final Log log = Log.get();

	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysRoleDao roleDao;

	@Override
	public SysUserEntity login(String userName, String pwd) {
		SysUserEntity userEntity = userDao
				.selectOne(new QueryWrapper<SysUserEntity>().lambda().eq(SysUserEntity::getUserName, userName));
		if (T.ObjectUtil.isNull(userEntity)
				|| !T.StrUtil.equals(userEntity.getPwd(), T.AesUtil.encrypt(pwd, Constants.AES_KEY))) {
			log.warn("user login error, username: {}", userName);
			throw ASWException.builder().rcode(RCode.SYS_USER_PWD_ERROR).build();
		}
		StpUtil.login(userEntity.getId());
		log.info("user login success, userName: {}", userName);
		return userEntity;
	}

	@Override
	public void logout() {
		StpUtil.logout();
	}

	/**
	 * 获取登录用户权限
	 */
	@Override
	public Map<String, Object> userPermissions() {
		Map<String, Object> result = T.MapUtil.newHashMap();
		String userId = StpUtil.getLoginIdAsString();
		List<SysRoleEntity> roleList = roleDao.findRoleByUserId(userId);
		result.put("roles", roleList);
		// 组织 menu数据
		List<SysMenuEntity> menuList = roleDao.findMenuByUserId(userId);
		List<String> buttonList = menuList.stream().filter(menu -> T.StrUtil.equalsIgnoreCase(menu.getType(), "button"))
				.map(menu -> menu.getName()).collect(Collectors.toList());
		result.put("buttons", buttonList);
		//生成 menu tree结构
		Map<String, List<SysMenuEntity>> groupMap = menuList.stream()
				.filter(menu -> T.StrUtil.equalsIgnoreCase(menu.getType(), "menu"))
				.collect(Collectors.groupingBy(SysMenuEntity::getPid));
		menuList.forEach(menu -> {
			menu.setChildren(groupMap.get(menu.getId()));
		});
		List<SysMenuEntity> collect = menuList.stream().filter(menu -> T.StrUtil.isBlank(menu.getPid()))
				.collect(Collectors.toList());
		result.put("menus", collect);
		
		return result;
	}
}
