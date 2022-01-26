package io.camunda.getstarted.potpourri.java;

import io.camunda.zeebe.client.ZeebeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Send a message to a process instance.  This pure Java class is executable directly.
 *
 * @author chris
 */
public class SendMessage {

    static Logger log = LoggerFactory.getLogger(SendMessage.class);

    public static void main(String[] args) {

        log.info("+++ send message with help from MyZeebeUtil...");

        try (ZeebeClient client = MyZeebeUtil.getClient()){
            // !!! fill in correlationKey before sending !!!
            MyZeebeUtil.correlateMessage(
                    client,
                    "ingredients-arrived",
                    "correlation-key-goes-here");
        } // client auto-closes when out of scope.

    }
}