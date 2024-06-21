package net.geedge.asw.module.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("sys_file")
public class FileEntity {

	/**
	 * 以文件md5作为id
	 */
	@TableId(type = IdType.NONE)
	private String id;
	private String name;
	private String suffix;
	private String contentType;
	private Integer size;
	private String path;
	private String remark;
	private Long createTimestamp;
	@TableField(exist = false)
	private String uri;
}
