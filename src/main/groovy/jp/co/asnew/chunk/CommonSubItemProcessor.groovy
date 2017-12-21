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
@Component('commonSubItemProcessor')
class CommonSubItemProcessor implements ItemProcessor<Object, Object> {

	Logger log = LoggerFactory.getLogger(CommonSubItemProcessor.class)
	
	@Override
	public Object process(Object item) throws Exception {
		
		log.info("${getClass().getSimpleName()} Execute. item: ${item}")

		List nextItems = []

		[*1..5].each {
			KieServices ks = KieServices.Factory.get()
			KieContainer kContainer = ks.getKieClasspathContainer()
			KieSession kSession = kContainer.newKieSession("ksession-rules-sub")
	
			Report report = new Report()
			report.volume = item
			UnitCost unitCost = new UnitCost()
	
			kSession.insert(report)
			kSession.insert(unitCost)
			
			kSession.getAgenda().getAgendaGroup("CommonSubRulesA").setFocus()
			
			kSession.fireAllRules()
	
			kSession.dispose()
	
			if (unitCost.cost) {
				nextItems << unitCost
			}
		}
		nextItems
		
	}
	
}
