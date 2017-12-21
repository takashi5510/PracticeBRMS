package jp.co.asnew.chunk

import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

import jp.co.asnew.entity.*

import org.kie.api.KieServices
import org.kie.api.runtime.KieContainer
import org.kie.api.runtime.KieSession

@StepScope
@Component('sampleEntryFeeItemProcessor')
class SampleEntryFeeItemProcessor implements ItemProcessor<Object, Object> {

	Logger log = LoggerFactory.getLogger(SampleEntryFeeItemProcessor.class)
	
	@Override
	public Object process(Object item) throws Exception {
		
		log.info("${getClass().getSimpleName()} Execute. item: ${item}")

		List nextItems = []

		// Master取得
		List masterRecords = []

		MasterUnitCost masterRecord1 = new MasterUnitCost()
		masterRecord1.deliveryVendorID = 3458
		masterRecord1.masterDeliveryCostConditionID = 1
		masterRecord1.ruleName = "1-1"
		masterRecord1.conditionStartDate = '2017-01-01'
		masterRecord1.conditionEndDate = '2017-01-31'
		masterRecord1.masterDeliveryCostID = 1
		masterRecord1.unitCost = 2.0
		masterRecord1.costStartDate = '2017-01-01'
		masterRecord1.costEndDate = '2017-01-31'

		MasterUnitCost masterRecord2 = new MasterUnitCost()
		masterRecord2.deliveryVendorID = 3458
		masterRecord2.masterDeliveryCostConditionID = 2
		masterRecord2.ruleName = "1-2"
		masterRecord2.conditionStartDate = '2017-01-01'
		masterRecord2.conditionEndDate = '2017-01-31'
		masterRecord2.masterDeliveryCostID = 2
		masterRecord2.unitCost = 1.0
		masterRecord2.costStartDate = '2017-01-01'
		masterRecord2.costEndDate = '2017-01-31'

		masterRecords << masterRecord1
		masterRecords << masterRecord2
		
		// Report取得
		List entryReportList = []
		
		
		[*1..100000].each { it ->
			EntryReport entryReport = new EntryReport()
			entryReport.entryID = it
			entryReport.entryDate = '2017-01-10'
			entryReport.entryPatternID = 1
			entryReport.scheduleID = it + 10000
			entryReport.volume = 10
			entryReport.entryProcessingID = 1
			entryReport.cashOnDelivery = null
			if(it%2 == 0) {
				entryReport.isProduct = 1
			} else {
				entryReport.isProduct = 0
			}
			entryReportList << entryReport
		}
		[*1..50000].each { it ->
			EntryReport entryReport = new EntryReport()
			entryReport.entryID = it
			entryReport.entryDate = '2017-01-10'
			entryReport.entryPatternID = 2
			entryReport.scheduleID = it + 20000
			entryReport.volume = 10
			entryReport.entryProcessingID = 1
			entryReport.cashOnDelivery = null
			if(it%2 == 0) {
				entryReport.isProduct = 1
			} else {
				entryReport.isProduct = 0
			}
			entryReportList << entryReport
		}

		entryReportList.each { tmpEntryReport ->
			masterRecords.each { tmpMasterRecord ->
				KieServices ks = KieServices.Factory.get()
				KieContainer kContainer = ks.getKieClasspathContainer()
				KieSession kSession = kContainer.newKieSession("ksession-rules-entry")

				DeliveryCost deliveryCost = new DeliveryCost()
				deliveryCost.billingDate = "2017-01-31"

				kSession.insert(tmpEntryReport)
				kSession.insert(tmpMasterRecord)
				kSession.insert(deliveryCost)
				
				kSession.getAgenda().getAgendaGroup("EntryFee").setFocus()	
				kSession.fireAllRules()
		
				kSession.dispose()
		
				if (deliveryCost && deliveryCost.unitCost && deliveryCost.unitCost > 0) {
					nextItems << deliveryCost
				}
			}
		}
		
		nextItems

	}
	
}
