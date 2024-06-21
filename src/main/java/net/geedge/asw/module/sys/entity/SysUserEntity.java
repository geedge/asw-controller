package net.geedge.asw.module.sys.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("sys_user")
public class SysUserEntity {

	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	private String name;
	@TableField("user_name")
	private String userName;
	private String pwd;
	@TableField(exist = false)
	private String[] roleIds;
	@TableField(exist = false)
	private List<SysRoleEntity> roles;
	private Long createTimestamp;
}
