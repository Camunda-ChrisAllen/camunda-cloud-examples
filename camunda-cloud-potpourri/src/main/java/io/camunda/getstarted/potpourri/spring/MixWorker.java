package io.camunda.getstarted.potpourri.spring;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Zeebe worker for the "Mix ingredients and smell" service task in the Potpourri process.
 *
 * @author chris
 */
@Component
@EnableZeebeClient
public class MixWorker {

    Logger log = LoggerFactory.getLogger(MixWorker.class);

    @ZeebeWorker(type = "mix-ingredients")
    public void mixIngredients(final JobClient client, final ActivatedJob job) {
        log.info("...mixing ingredients....");

        HashMap<String, Object> varmap = (HashMap) job.getVariablesAsMap();
        final String ingredient2 = (String) varmap.get("ingredient2");

        if ("licorice".equalsIgnoreCase(ingredient2)) {
            varmap.put("smellGood", false);
        } else {
            varmap.put("smellGood", true);
        }
        client.newCompleteCommand(job.getKey())
                .variables(varmap)
                .send()
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Completed job successful with result:" + result);
                    } else {
                        log.error("Failed to complete job", exception);
                    }
                });
    }

}