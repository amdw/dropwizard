package com.example.logging;

import io.dropwizard.Configuration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * An example command which does nothing, but inherits from ConfiguredCommand so we get its default teardown behaviour
 */
public class ExampleCommand extends ConfiguredCommand<Configuration> {
    ExampleCommand() {
        super("example", "an example command");
    }

    @Override
    protected void run(Bootstrap<Configuration> bootstrap, Namespace namespace, Configuration configuration) throws Exception {
        System.out.println("Running " + ExampleCommand.class.getSimpleName());
    }
}
