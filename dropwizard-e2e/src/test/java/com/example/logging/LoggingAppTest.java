package com.example.logging;

import io.dropwizard.Configuration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

/**
 * Reproduce logging behaviour described in https://github.com/dropwizard/dropwizard/issues/1119
 */
public class LoggingAppTest {
    @ClassRule
    public static final DropwizardAppRule<Configuration> RULE = new DropwizardAppRule<>(LoggingApp.class,
        ResourceHelpers.resourceFilePath("logging/config.yml"));
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAppTest.class);
    private PrintStream originalStdOut;
    private ByteArrayOutputStream stdOutCapture;

    @Before
    public void setup() {
        stdOutCapture = new ByteArrayOutputStream();
        originalStdOut = System.out;
        System.setOut(new PrintStream(stdOutCapture));
    }

    @After
    public void teardown() {
        System.setOut(originalStdOut);
    }

    @Test
    public void testLogging() throws Exception {
        LOGGER.warn("Should see this first one");
        // Trigger the example command - this causes ConfiguredCommand.cleanup() to be invoked at the end
        // which stops the logging factory
        RULE.getApplication().run("example");

        // If you uncomment the below line, to configure the logging factory again, the test passes.
//        RULE.getConfiguration().getLoggingFactory().configure(RULE.getEnvironment().metrics(), RULE.getApplication().getName());

        LOGGER.warn("Should see this second one");

        // Sleep in the hope that the async appender will flush.
        // Unfortunately I don't see an easy way to turn the async behaviour off to make this deterministic :(
        Thread.sleep(2000);

        String logged = new String(stdOutCapture.toByteArray(), Charset.forName("UTF-8"));
        // This assertion passes, as the message was logged before the loggers were stopped.
        assertTrue("First message was not logged", logged.contains("first one"));
        // This assertion does not pass, as the message was logged after the loggers were stopped.
        assertTrue("Second message was not logged", logged.contains("second one"));
    }
}
