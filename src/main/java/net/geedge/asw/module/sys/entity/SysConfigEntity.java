package net.geedge.asw.module.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;
import net.geedge.asw.common.util.T;

@Data
@Builder
@TableName("sys_config")
public class SysConfigEntity {
	@TableId(type = IdType.INPUT)
	private String paramKey;
	private String paramValue;
	private Integer status;
	private String remark;

	public enum KeyEnum {
		SCREEN_PUBLISH_NUM("screen_publish_num");

		private String key;

		KeyEnum(String key) {
			this.key = key;
		}

		public String getKey() {
			return this.key;
		}
	}
	public static void main(String[] args) {
		System.out.println(SysConfigEntity.KeyEnum.SCREEN_PUBLISH_NUM.getKey());
	}
	
	public Integer getParamValueAsInt() {
		return T.NumberUtil.parseInt(this.paramValue);
	}
	
	public Long getParamValueAsLong() {
		return T.NumberUtil.parseLong(this.paramValue);
	}
}
