package net.geedge.asw.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.client.RestTemplate;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.service.MagicResourceStorage;
import org.ssssssss.magicapi.modules.http.HttpModule;
import org.ssssssss.magicapi.task.model.TaskInfo;
import org.ssssssss.magicapi.task.service.TaskInfoMagicResourceStorage;
import org.ssssssss.magicapi.task.service.TaskMagicDynamicRegistry;
import org.ssssssss.magicapi.task.starter.MagicTaskConfig;
import org.ssssssss.magicapi.utils.ScriptManager;
import org.ssssssss.script.MagicScriptContext;

@Configuration
public class MagicApiConfig {
	@Autowired
	private MagicTaskConfig config;

	@Bean
	public HttpModule magicHttpModule(RestTemplate restTemplate) {
		return new HttpModule(restTemplate);
	}

	@Bean
	public TaskMagicDynamicRegistry taskMagicDynamicRegistry(
			TaskInfoMagicResourceStorage taskInfoMagicResourceStorage) {
		MagicTaskConfig.Shutdown shutdown = config.getShutdown();
		ThreadPoolTaskScheduler poolTaskScheduler = null;
		if (config.isEnable()) {
			poolTaskScheduler = new ThreadPoolTaskScheduler();
			poolTaskScheduler.setPoolSize(config.getPool().getSize());
			poolTaskScheduler.setWaitForTasksToCompleteOnShutdown(shutdown.isAwaitTermination());
			if (shutdown.getAwaitTerminationPeriod() != null) {
				poolTaskScheduler.setAwaitTerminationSeconds((int) shutdown.getAwaitTerminationPeriod().getSeconds());
			}
			poolTaskScheduler.setThreadNamePrefix(config.getThreadNamePrefix());
			poolTaskScheduler.initialize();
		}
		return new CustomTaskMagicDynamicRegistry(taskInfoMagicResourceStorage, poolTaskScheduler, config.isLog());
	}

	/**
	 * 自定义任务动态注册，context注入taskinfo对象
	 */
	private class CustomTaskMagicDynamicRegistry extends TaskMagicDynamicRegistry {
		private final TaskScheduler taskScheduler;
		private static final Logger logger = LoggerFactory.getLogger(CustomTaskMagicDynamicRegistry.class);
		private final boolean showLog;

		public CustomTaskMagicDynamicRegistry(MagicResourceStorage<TaskInfo> magicResourceStorage,
				TaskScheduler taskScheduler, boolean showLog) {
			super(magicResourceStorage, taskScheduler, showLog);
			this.taskScheduler = taskScheduler;
			this.showLog = showLog;
		}

		@Override
		protected boolean register(MappingNode<TaskInfo> mappingNode) {
			TaskInfo entity = mappingNode.getEntity();
			if (taskScheduler != null) {
				String scriptName = MagicConfiguration.getMagicResourceService().getScriptName(entity);
				try {
					CronTrigger trigger = new CronTrigger(entity.getCron());
					CronTask cronTask = new CronTask(() -> {
						if (entity.isEnabled()) {
							try {
								if (showLog) {
									logger.info("定时任务:[{}]开始执行", scriptName);
								}
								MagicScriptContext magicScriptContext = new MagicScriptContext();
								magicScriptContext.setScriptName(scriptName);
								magicScriptContext.set("taskinfo", entity);
								ScriptManager.executeScript(entity.getScript(), magicScriptContext);
							} catch (Exception e) {
								logger.error("scriptName:{}", scriptName);
								logger.error("定时任务执行出错", e);
							} finally {
								if (showLog) {
									logger.info("定时任务:[{}]执行完毕", scriptName);
								}
							}
						}
					}, trigger);
					mappingNode.setMappingData(taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger()));
				} catch (Exception e) {
					logger.error("定时任务:[{}]注册失败", scriptName, e);
				}
				logger.debug("注册定时任务:[{},{}]", MagicConfiguration.getMagicResourceService().getScriptName(entity),
						entity.getCron());
			}
			return true;
		}
	}

}
