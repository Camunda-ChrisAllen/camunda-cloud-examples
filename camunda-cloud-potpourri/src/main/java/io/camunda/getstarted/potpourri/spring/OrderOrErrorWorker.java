package io.camunda.getstarted.potpourri.spring;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Zeebe worker for the "Order ingredients" service task in the Potpourri process.  It checks for a "throwerror"
 * value for ingredient3.  If so it will trigger the Error Start Event.
 *
 * @author chris
 */
@Component
@EnableZeebeClient
public class OrderOrErrorWorker {

    Logger log = LoggerFactory.getLogger(OrderOrErrorWorker.class);

    @ZeebeWorker(type = "order-ingredients")
    public void getIngredients(final JobClient client, final ActivatedJob job) {
        log.info("...ordering ingredients....");
        final String ingredient1 = (String) job.getVariablesAsMap().get("ingredient1");
        final String ingredient2 = (String) job.getVariablesAsMap().get("ingredient2");
        final String ingredient3 = (String) job.getVariablesAsMap().get("ingredient3");
        log.info("Ordering : ingredients1 = {}, ingredients2 = {}, ingredients3 = {}",
                ingredient1, ingredient2, ingredient3);
        if ("throwerror".equalsIgnoreCase(ingredient3)) {
            client.newThrowErrorCommand(job).errorCode("order-error-code")
                    .send()
                    .whenComplete(
                            (result2, exception2) -> {
                                if (exception2 == null) {
                                    log.info("Send error successful with result:  " + result2);
                                } else {
                                    log.info("Error send failed:  " + exception2);
                                }
                            });
        } else {
            client.newCompleteCommand(job.getKey())
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

}