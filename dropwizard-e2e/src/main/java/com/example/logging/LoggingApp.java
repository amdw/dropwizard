package com.example.logging;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LoggingApp extends Application<Configuration> {
    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addCommand(new ExampleCommand());
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        // Just to get rid of the warning about lack of health checks
        environment.healthChecks().register("health", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                return Result.healthy();
            }
        });
    }
}
