package net.geedge.asw.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("sys_user_role")
public class SysUserRoleEntity {

	private String userId;
	private String roleId;
}
