package jp.co.asnew

import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
class PracticeBrmsApplication {

	static void main(String[] args) {
//		SpringApplication.run PracticeBrmsApplication, args
		SpringApplication app = new SpringApplication(PracticeBrmsApplication.class)
		app.setWebEnvironment(false)
		ConfigurableApplicationContext ctx= app.run(args)

		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class)

		def result = new JobParametersBuilder()

		if (args.size() > 1) {
			args[1].split(',').each {
				result.addParameter(it.split('=')[0], new JobParameter(it.split('=')[1]))
			}
		}
		JobParameters jobParameters = result.toJobParameters()

		jobLauncher.run(ctx.getBean(args[0],  Job.class), jobParameters)
	}
}
