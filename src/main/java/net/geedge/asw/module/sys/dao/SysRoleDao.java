package net.geedge.asw.module.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.geedge.asw.module.sys.entity.SysMenuEntity;
import net.geedge.asw.module.sys.entity.SysRoleEntity;

@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

	@Select("select sr.* from sys_role sr left join sys_user_role sur on sr.id = sur.role_id where sur.user_id = #{userId}")
	public List<SysRoleEntity> findRoleByUserId(String userId);

	@Select("select sm.* from sys_menu sm LEFT JOIN sys_role_menu srm on sm.id = srm.menu_id LEFT JOIN sys_user_role sur on srm.role_id = sur.role_id where sur.user_id = #{userId} and sm.state = 1 order by sm.order")
	public List<SysMenuEntity> findMenuByUserId(String userId);
}
