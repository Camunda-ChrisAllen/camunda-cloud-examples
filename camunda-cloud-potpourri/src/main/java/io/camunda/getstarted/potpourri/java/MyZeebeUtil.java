package io.camunda.getstarted.potpourri.java;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Final class with helpful utility methods.
 *
 * @author chris
 */
public final class MyZeebeUtil {

    static Logger log = LoggerFactory.getLogger(MyZeebeUtil.class);

    /**
     * Use this method to generate a Zeebe client.  Utilizes values available in your application.properties file.
     *
     * @return ZeebeClient
     */
    public static ZeebeClient getClient() {

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";

        log.info("appConfigPath = " + appConfigPath);

        Properties appProps = new Properties();
        ZeebeClient client = null;

        try {
            appProps.load(new FileInputStream(appConfigPath));

            String zeebeAPI = appProps.getProperty("zeebe.client.cloud.cluster-id") + ".bru-2.zeebe.camunda.io";
            String clientId = appProps.getProperty("zeebe.client.cloud.client-id");
            String clientSecret = appProps.getProperty("zeebe.client.cloud.client-secret");
            String oAuthAPI = appProps.getProperty("zeebe.client.cloud.auth-url");

            OAuthCredentialsProvider credentialsProvider =
                    new OAuthCredentialsProviderBuilder()
                            .authorizationServerUrl(oAuthAPI)
                            .audience(zeebeAPI)
                            .clientId(clientId)
                            .clientSecret(clientSecret)
                            .build();

            client = ZeebeClient.newClientBuilder()
                    .gatewayAddress(zeebeAPI)
                    .credentialsProvider(credentialsProvider)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * Start a process in Camunda Cloud.
     *
     * @param client a provided ZeebeClient instance.
     * @param processId The unique identifying ID of the process.
     * @param variables A process instance variables map.
     */
    public static void startProcess(ZeebeClient client, String processId, Map<String, Object> variables) {

        log.info("Starting the process.......NOW!!!");

        final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                .bpmnProcessId(processId)
                .latestVersion()
                .variables(variables)
                .send()
                .join();

        log.info(
                "started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
                event.getProcessDefinitionKey(),
                event.getBpmnProcessId(),
                event.getVersion(),
                event.getProcessInstanceKey());

    }

    /**
     * Correlate a BPMN message in a process instance using a key.
     *
     * @param client a ZeebeClient instance.
     * @param messageName A message name.
     * @param correlationKey A correlation key, as defined in the BPMN model for this message event.
     */
    public static void correlateMessage(ZeebeClient client, String messageName, String correlationKey) {

        log.info("Attempt to correlate message.......NOW!!!");

        client.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey)
                .send()
                .join();
    }

}
