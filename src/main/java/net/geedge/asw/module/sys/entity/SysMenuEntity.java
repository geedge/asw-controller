package net.geedge.asw.module.sys.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("sys_menu")
public class SysMenuEntity {

	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	private String name;
	private String i18n;
	private String pid;
	private String type;
	private String perms;
	private String route;
	private String icon;
	private Integer order;
	private Integer state;
	private Long createTimestamp;
	@TableField(exist = false)
	private List<SysMenuEntity> children;
}
