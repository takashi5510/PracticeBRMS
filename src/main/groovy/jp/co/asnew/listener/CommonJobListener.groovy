package jp.co.asnew.listener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component('commonJobListener')
class CommonJobListener implements JobExecutionListener {
	
	Logger log = LoggerFactory.getLogger(CommonJobListener.class)

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("${jobExecution.getJobInstance().getJobName()} Start.")
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("${jobExecution.getJobInstance().getJobName()} Done, status: ${jobExecution.getStatus()}")
	}

}