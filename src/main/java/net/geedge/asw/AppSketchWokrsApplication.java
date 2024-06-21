package net.geedge.asw;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.geedge.asw.common.util.Constants;
import net.geedge.asw.common.util.T;

@SpringBootApplication(proxyBeanMethods = false)
@EnableTransactionManagement
@PropertySource(value = {"file:./config/asw.properties", "classpath:./config/asw.properties"}, encoding = "utf-8", ignoreResourceNotFound = true)
public class AppSketchWokrsApplication {

	public static void main(String[] args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("jasypt.encryptor.password", T.HexUtil.encodeHexStr(Constants.AES_KEY));
		SpringApplication.run(AppSketchWokrsApplication.class, args);
	}

}
