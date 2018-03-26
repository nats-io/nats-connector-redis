// Copyright 2016-2018 The NATS Authors
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
