package net.geedge.asw.module.sys.controller;

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
import net.geedge.asw.common.util.R;
import net.geedge.asw.common.util.RCode;
import net.geedge.asw.common.util.T;
import net.geedge.asw.module.sys.entity.SysRoleEntity;
import net.geedge.asw.module.sys.service.ISysRoleService;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

	private static final Log log = Log.get();
	@Autowired
	private ISysRoleService roleService;

	@GetMapping("/{id}")
	public R detail(@PathVariable("id") String id) {
		SysRoleEntity entity = roleService.getById(id);
		return R.ok().putData("record", entity);
	}

	@GetMapping
	public R list(String ids, String name, @RequestParam(defaultValue = "1") Integer current,
			@RequestParam(defaultValue = "20") Integer size, @RequestParam(defaultValue = "name") String orderBy) {
		QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<SysRoleEntity>();
		queryWrapper.like(T.StrUtil.isNotBlank(name), "name", name).in(T.StrUtil.isNotBlank(ids), "id", ids.split(","));
		Page<SysRoleEntity> page = Page.of(current, size);
		page.addOrder(T.PageUtil.decodeOrderByStr(orderBy));
		page = roleService.page(page, queryWrapper);
		return R.ok(page);
	}

	@PostMapping
	public R add(@RequestBody SysRoleEntity entity) {
		T.VerifyUtil.is(entity).notNull().and(entity.getName()).notEmpty(RCode.NAME_CANNOT_EMPTY);
		SysRoleEntity one = roleService.getOne(new QueryWrapper<SysRoleEntity>().lambda().eq(SysRoleEntity::getName, entity.getName()));
		if (T.ObjectUtil.isNotNull(one)) {
			throw ASWException.builder().rcode(RCode.SYS_DUPLICATE_RECORD).build();
		}
		entity.setCreateTimestamp(T.DateUtil.current());
		roleService.saveOrUpdateRole(entity);
		return R.ok().putData("id", entity.getId());
	}

	@PutMapping
	public R update(@RequestBody SysRoleEntity entity) {
		T.VerifyUtil.is(entity).notNull().and(entity.getId()).notEmpty(RCode.ID_CANNOT_EMPTY);
		entity.setCreateTimestamp(null);
		roleService.saveOrUpdateRole(entity);
		return R.ok().putData("id", entity.getId());
	}

	@DeleteMapping
	public R delete(String[] ids) {
		T.VerifyUtil.is(ids).notEmpty();
		roleService.removeBatchByIds(T.ListUtil.of(ids));
		log.info("delete Role, ids: {}", T.ArrayUtil.toString(ids));
		return R.ok();
	}

}
