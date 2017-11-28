package jp.co.asnew.chunk

import org.slf4j.Logger
import java.util.List
import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@StepScope
@Component('commonItemWriter')
class CommonItemWriter implements ItemWriter<Object> {

	Logger log = LoggerFactory.getLogger(CommonItemWriter.class)
	
	@Override
	public void write(List<? extends Object> items) throws Exception {
		items.each { item ->
			item.each { i ->
				log.info("commonItemWriter Execute. item.volume: ${i.volume}, item.cost: ${i.cost}")
			}
		}
	}
	
}
