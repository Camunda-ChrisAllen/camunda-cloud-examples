package io.camunda.getstarted.potpourri.java;

import io.camunda.zeebe.client.ZeebeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Create an instance of the Potpourri Process.  This pure Java class is executable directly.
 *
 * @author chris
 */
public class CreateProcessInstance {

    static Logger log = LoggerFactory.getLogger(CreateProcessInstance.class);

    public static void main(String[] args) {

        log.info("+++ start process using MyZeebeUtil...");
        Map<String, Object> variables = new HashMap<>();
        variables.put("potpourriId", UUID.randomUUID().toString());

        try (ZeebeClient client = MyZeebeUtil.getClient()) {
            MyZeebeUtil.startProcess(client, "potpourri-process", variables);
        } // client auto-closes when out of scope.


    }

}
