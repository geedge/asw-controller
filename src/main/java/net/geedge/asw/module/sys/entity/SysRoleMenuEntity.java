package net.geedge.asw.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("sys_role_menu")
public class SysRoleMenuEntity {

	private String roleId;
	private String menuId;
}
