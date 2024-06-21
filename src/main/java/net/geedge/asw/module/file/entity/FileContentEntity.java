package net.geedge.asw.module.file.entity;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("sys_file_content")
public class FileContentEntity {

	private String id;
	@TableField(jdbcType = JdbcType.BLOB)
	private byte[] content;
}
