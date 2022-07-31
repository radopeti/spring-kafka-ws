package com.radopeti.actionmonitor.component;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class KafkaHealthIndicator implements HealthIndicator {

    private final KafkaAdmin admin;

    private final AdminClient adminClient;

    public KafkaHealthIndicator(KafkaAdmin admin) {
        this.admin = admin;
        this.adminClient = AdminClient.create(admin.getConfigurationProperties());
    }

    /**
     * Return an indication of health.
     *
     * @return the health for
     */
    @Override
    public Health health() {
        final DescribeClusterOptions describeClusterOptions = new DescribeClusterOptions().timeoutMs(1000);
        final DescribeClusterResult describeCluster = adminClient.describeCluster(describeClusterOptions);
        try {
            final String clusterId = describeCluster.clusterId().get();
            final int nodeCount = describeCluster.nodes().get().size();
            return Health.up()
                    .withDetail("clusterId", clusterId)
                    .withDetail("nodeCount", nodeCount)
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            return Health.down()
                    .withException(e)
                    .build();
        }
    }
}
