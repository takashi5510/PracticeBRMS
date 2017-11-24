package jp.co.asnew.conf

import java.util.List
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.item.support.CompositeItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import jp.co.asnew.chunk.*
import jp.co.asnew.listener.CommonJobListener

@Configuration
@EnableBatchProcessing
class PracticeBRMSConfig {
	
	@Autowired
	JobBuilderFactory jobBuilderFactory

	@Autowired
	StepBuilderFactory stepBuilderFactory

	@Autowired
	CommonJobListener commonJobListener
	
	@Autowired
	CommonItemReader commonItemReader

	@Autowired
	CommonItemProcessor commonItemProcessor
	
	@Autowired
	CommonItemWriter commonItemWriter
	
	@Bean
	Job simpleJob() {
		jobBuilderFactory.get('simpleJob')
			.listener(commonJobListener)
			.start(simpleChunkStep())
			.build()
	}

	Step simpleChunkStep() {
		stepBuilderFactory.get('simpleChunkStep')
			.<Object, Object> chunk(1)
			.reader(commonItemReader)
			.processor(commonItemProcessor)
			.writer(commonItemWriter)
			.build()
	}
	
}
