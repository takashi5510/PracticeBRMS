package jp.co.asnew.chunk

import org.slf4j.Logger
import java.util.List
import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@StepScope
@Component('sampleItemWriter')
class SampleItemWriter implements ItemWriter<Object> {

	Logger log = LoggerFactory.getLogger(SampleItemWriter.class)
	
	@Override
	public void write(List<? extends Object> items) throws Exception {
		items.each { item ->
			item.each { i ->
				log.info("SampleItemWriter Execute. billingDate: ${i.billingDate}, deliveryVendorID: ${i.deliveryVendorID}, conditionID: ${i.masterDeliveryCostConditionID}, scheduleID: ${i.scheduleID}, vendorReportID: ${i.vendorReportID}, volume: ${i.volume}, unitCost: ${i.unitCost}")
			}
		}
	}
	
}
