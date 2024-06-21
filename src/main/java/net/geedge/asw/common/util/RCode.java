package net.geedge.asw.common.util;

import java.text.MessageFormat;

public enum RCode {

	/**
	 * 10**** : 系统认证 或 通用错误提示 20**** : screen module
	 */

	ERROR(999, "error"), // 通用错误/未知错误

	SYS_USER_PWD_ERROR(100001, "username or password error"), // 用户名 或密码错误
	SYS_DUPLICATE_RECORD(100002, "duplicate record"), // 数据库中已存在该记录
	SYS_NO_AUTH(100003, "Permission denied"), // 没有权限
	ID_CANNOT_EMPTY(100004, "id cannot be empty"), // ID 不能为空
	NAME_CANNOT_EMPTY(100005, "name cannot be empty"), // name 不能为空
	PARAM_CANNOT_EMPTY(100006, "parameter cannot be empty"), // parameter 不能为空
	USER_NO_LOGIN(100007, "user not login"), // 用户未登录
	SYS_RECORD_NOT_FOUND(100008, "record not found"),// 未找到记录


	SCREEN_ID_CANNOT_EMPTY(200001, "id cannot be empty"),

	SCREE_DATASOURCE_DEFAULT_CANNOT_BE_DELETE(300001,"The default data source cannot be deleted."),
	SCREE_DATASOURCE_REPEAT(300002,"Screen datasource name duplicate"),

	/**
	 * import
	 */
	EXCELFILE_TYPE_ERROR(400001, "The type can only be xlsx, json, csv"),
	EXCELFILE_PARSE_ERROR(400002, "Import file resolution failed"),
	EXCELFILE_HEADER_TEMPLATE_ERROR(400003,"The header row of the import template is inconsistent with the system template"),
	EXCELFILE_HEADER_LANGUAGE_ERROR(400004, "Language must be en, zh or ru"),
	EXCELFILE_IMPORT_FILE_ISNULL(400005, "Import file is null"),
	EXCELFILE_HEADER_LANGUAGE_ISNULL(400006, "Language can not be empty"),
	EXCELFILE_IMPORT_ERROR(400007, "File import error"),
	EXCELFILE_SCHEDULE_TASK_IS_NULL(400008, "Schedule task can not be empty"),
	EXCELFILE_SCHEDULE_CRON_IS_NULL(400009, "Schedule cron can not be empty"),
	EXCELFILE_SCHEDULE_ENABLE_IS_NULL(400010, "Schedule enable can not be empty"),
	EXCELFILE_SCHEDULE_SCRIPT_IS_NULL(400011, "Schedule script can not be empty"),


	SUCCESS(200, "success"); // 成功

	private RCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private Integer code;
	private String msg;
	private Object[] param;

	public RCode setParam(Object... param) {
		this.param = param;
		return this;
	}

	public Object[] getParam() {
		return param;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return MessageFormat.format(msg, param);
	}
}
