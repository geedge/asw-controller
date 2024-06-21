package net.geedge.asw.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("sys_role")
public class SysRoleEntity {

	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	private String name;
	private String i18n;
	private String remark;
	private Integer buildIn;
	@TableField(exist = false)
	private String[] menuIds;
	private Long createTimestamp;
}
