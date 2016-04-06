/*******************************************************************************
 * Copyright (c) 2016 Apcera Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package io.nats.connector.redis;

import io.nats.connector.Connector;
import io.nats.connector.plugins.redis.RedisPubSubPlugin;

/**
 * A Utility class to launch the Redis Connector from the command line.
 */
public class RedisPubSubConnector {

    /**
     * Usage.
     */
    static void usage() {
        System.out.printf("java %s\n", RedisPubSubConnector.class.getCanonicalName());
        System.out.printf("    -configURL <URL of the Redis Connector Configuration>\n" +
                          "    -debug\n");
        System.exit(-1);
    }

    static void parseArgs(String[] args)
    {
        if(args == null)
            return;

        if (args.length == 0)
            return;

        if(args.length < 2) {
            RedisPubSubConnector.usage();
        }

        for (int i = 0; i < args.length; i++)
        {
            if("-configURL".equalsIgnoreCase(args[i]))
            {
                i++;
                if (i >= args.length) {
                    usage();
                }
                System.setProperty(RedisPubSubPlugin.CONFIG_URL, args[i]);
            }
            else if ("-debug".equalsIgnoreCase(args[i]))
            {
                System.setProperty("org.slf4j.simpleLogger.log.io.nats.connector.plugins.redis.RedisPubSubPlugin", "trace");
            }
            else
            {
                RedisPubSubConnector.usage();
            }
        }
    }

    /***
     * Entry point to launch the NATS Redis Connector
     * @param args - arguments.  See usage for more information.
     */
    public static void main(String[] args)
    {
        try
        {
            parseArgs(args);

            System.setProperty(Connector.PLUGIN_CLASS, RedisPubSubPlugin.class.getName());
            new Connector(null).run();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
