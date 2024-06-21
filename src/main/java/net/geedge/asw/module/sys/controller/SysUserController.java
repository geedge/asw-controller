package net.geedge.asw.module.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.hutool.log.Log;
import net.geedge.asw.common.util.ASWException;
import net.geedge.asw.common.util.Constants;
import net.geedge.asw.common.util.R;
import net.geedge.asw.common.util.RCode;
import net.geedge.asw.common.util.T;
import net.geedge.asw.module.sys.entity.SysRoleEntity;
import net.geedge.asw.module.sys.entity.SysUserEntity;
import net.geedge.asw.module.sys.service.ISysRoleService;
import net.geedge.asw.module.sys.service.ISysUserService;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

	private static final Log log = Log.get();
	@Autowired
	private ISysUserService userService;
	@Autowired
	private ISysRoleService roleService;
	
	@GetMapping("/{id}")
	public R detail(@PathVariable("id") String id) {
		SysUserEntity entity = userService.getById(id);
		entity.setPwd(null);
		List<SysRoleEntity> roleList = roleService.listByIds(T.ListUtil.of(entity.getRoleIds()));
		entity.setRoles(roleList);
		return R.ok().putData("record", entity);
	}

	@GetMapping
	public R list(String name, @RequestParam(defaultValue = "1") Integer current,
			@RequestParam(defaultValue = "20") Integer size, @RequestParam(defaultValue = "name") String orderBy) {
		QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<SysUserEntity>();
		// 不查询 pwd 列
		queryWrapper.select(SysUserEntity.class, entity -> !entity.getColumn().equals("pwd"));
		if (T.StrUtil.isNotBlank(name)) {
			queryWrapper.and(wrapper -> wrapper.like("name", name).or().like("user_name", name));
		}
		Page<SysUserEntity> page = Page.of(current, size);
		page.addOrder(T.PageUtil.decodeOrderByStr(orderBy));
		page = userService.page(page, queryWrapper);
		return R.ok(page);
	}

	@PostMapping
	public R add(@RequestBody SysUserEntity entity) {
		T.VerifyUtil.is(entity).notNull().and(entity.getName()).notEmpty(RCode.NAME_CANNOT_EMPTY)
				.and(entity.getUserName()).notEmpty().and(entity.getPwd()).notEmpty();
		SysUserEntity one = userService.getOne(
				new QueryWrapper<SysUserEntity>().lambda().eq(SysUserEntity::getUserName, entity.getUserName()));
		if (T.ObjectUtil.isNotNull(one)) {
			throw ASWException.builder().rcode(RCode.SYS_DUPLICATE_RECORD).build();
		}
		// 密码加密
		entity.setPwd(T.AesUtil.encrypt(entity.getPwd(), Constants.AES_KEY));
		entity.setCreateTimestamp(T.DateUtil.current());
		userService.saveOrUpdateUser(entity);
		return R.ok().putData("id", entity.getId());
	}

	@PutMapping
	public R update(@RequestBody SysUserEntity entity) {
		T.VerifyUtil.is(entity).notNull().and(entity.getId()).notEmpty(RCode.ID_CANNOT_EMPTY);
		if (T.StrUtil.isNotBlank(entity.getPwd())) {
			// 密码加密
			entity.setPwd(T.AesUtil.encrypt(entity.getPwd(), Constants.AES_KEY));
		} else {
			entity.setPwd(null);
		}
		entity.setUserName(null);// username 不允许修改
		entity.setCreateTimestamp(null);
		userService.saveOrUpdateUser(entity);
		return R.ok().putData("id", entity.getId());
	}

	@DeleteMapping
	public R delete(String[] ids) {
		T.VerifyUtil.is(ids).notEmpty();
		userService.removeBatchByIds(T.ListUtil.of(ids));
		log.info("delete user, ids: {}", T.ArrayUtil.toString(ids));
		return R.ok();
	}

}
