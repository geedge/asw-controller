package net.geedge.asw.common.util;

import java.io.File;

public class Constants {

	/**
	 * AES 加密密钥
	 */
	public static final byte[] AES_KEY = T.HexUtil.decodeHex("bde5430614b21baf1c53bd6f616d1a39");

	/**
	 * 临时目录
	 */
	public static final String TEMP_PATH = System.getProperty("user.dir") + File.separator + "tmp";


	
}
