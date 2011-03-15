package me.FallingDownLib.CassandraConnection.connectors;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.prettyprint.cassandra.service.CassandraClient;
import me.prettyprint.cassandra.service.CassandraClientPool;
import me.prettyprint.cassandra.service.CassandraClientPoolFactory;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class Connector {

    private static final int CASSANDRA_PORT = 9160;
    private static final String CASSANDRA_KEYSPACE = "FallingDown";
    private static final String CASSANDRA_HOST = "localhost";
    private CassandraClientPool pool;
    private CassandraClient client;

    /**
     *
     * @return a client borrowed. Do not forget to release it afterwards
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    private CassandraClient getClient() throws IllegalStateException,
            PoolExhaustedException, Exception{
        pool= CassandraClientPoolFactory.getInstance().get();
        return pool.borrowClient(CASSANDRA_HOST, CASSANDRA_PORT);
    }

    /**
     *
     * @param client
     * @return a keyspace
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     */

    public Keyspace getKeyspace() throws IllegalArgumentException,
            TException, IllegalStateException, PoolExhaustedException, NotFoundException, Exception{
        client=getClient();
        return client.getKeyspace(CASSANDRA_KEYSPACE);
    }

    /**
     * Release the client borrowed
     * @param connector
     * @throws Exception
     */

    public static void releaseClient(Connector connector) {

        if (connector != null) {
            try {
                connector.pool.releaseClient(connector.client);
            } catch (Exception ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
