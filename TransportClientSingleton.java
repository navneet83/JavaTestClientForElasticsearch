import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import org.elasticsearch.shield.ShieldPlugin;

import java.net.InetAddress;

public class TransportClientSingleton {

    private static Client client;

    public static Client getClient() throws java.net.UnknownHostException {
        if (client == null) {
            client = createTransportClient();
        }
        return client;
    }

    public static void destroy() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    private static Client createTransportClient() throws java.net.UnknownHostException {
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "shield-test")

                // shield username:password
                .put("shield.user", "admin:password")

                // use ssl for transport protocol
                .put("shield.transport.ssl", "true")

                // truststore containing all the certs of the nodes
                .put("shield.ssl.truststore.path", "/path/to/conf/truststore.jks")
                .put("shield.ssl.truststore.password", "password")
                .build();
        return TransportClient.builder()
                // add the Shield plugin to wrap the transport
                .addPlugin(ShieldPlugin.class)
                .settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost.localdomain"), 9300));
    }

    public static void main(String args[]) throws Exception {
        System.out.println("Begin Test");
        GetResponse response = TransportClientSingleton.getClient().prepareGet("testindex", "testtype1", "1").get();
        System.out.println(response.getSource().values().toString());
        TransportClientSingleton.destroy();
    }
}
