package lab8.client.network;

import lab8.common.dto.Chunk;
import lab8.common.dto.Request;
import lab8.common.dto.Response;
import lab8.common.network.ChunkBuffer;
import lab8.common.network.ChunkUtils;
import org.eclipse.californium.elements.AddressEndpointContext;
import org.eclipse.californium.elements.RawData;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConfig;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.CertificateType;
import org.eclipse.californium.scandium.dtls.x509.SingleCertificateProvider;
import org.eclipse.californium.scandium.dtls.x509.StaticNewAdvancedCertificateVerifier;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static lab8.common.utils.Serializer.deserialize;
import static lab8.common.utils.Serializer.serialize;

public class DTLSClient {

    private final InetSocketAddress serverAddress;
    private final DTLSConnector connector;
    private final ChunkBuffer buffer = new ChunkBuffer();
    private volatile CompletableFuture<Response> pendingResponse;

    public DTLSClient(String host, int port) throws Exception {
        DtlsConfig.register();
        this.serverAddress = new InetSocketAddress(InetAddress.getByName(host), port);

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream("client.jks"), "password".toCharArray());
        PrivateKey privateKey = (PrivateKey) keyStore.getKey("client", "password".toCharArray());
        Certificate[] certChain = keyStore.getCertificateChain("client");

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("client-trust.jks"), "password".toCharArray());
        List<X509Certificate> trustedCerts = new ArrayList<>();
        Enumeration<String> aliases = trustStore.aliases();

        while (aliases.hasMoreElements()) {
            String a = aliases.nextElement();
            if (trustStore.isCertificateEntry(a))
                trustedCerts.add((X509Certificate) trustStore.getCertificate(a));
        }

        Configuration config = Configuration.getStandard();
        DtlsConnectorConfig.Builder builder = DtlsConnectorConfig.builder(config);
        builder.setAddress(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 0));

        builder.setCertificateIdentityProvider(
                new SingleCertificateProvider(privateKey, certChain, CertificateType.X_509)
        );
        builder.setAdvancedCertificateVerifier(
                StaticNewAdvancedCertificateVerifier.builder()
                        .setTrustedCertificates(trustedCerts.toArray(new X509Certificate[0]))
                        .build()
        );

        this.connector = new DTLSConnector(builder.build());

        this.connector.setRawDataReceiver(raw -> {
            try {
                Chunk chunk = deserialize(raw.getBytes());
                buffer.add(chunk);
                if (!buffer.isComplete(chunk.getId())) return;

                List<Chunk> full = buffer.take(chunk.getId());
                byte[] responseData = ChunkUtils.assemble(full);
                Response response = deserialize(responseData);

                if (pendingResponse != null)
                    pendingResponse.complete(response);

            } catch (Exception e) {
                if (pendingResponse != null)
                    pendingResponse.completeExceptionally(e);
            }
        });

        this.connector.start();
    }

    public Response sendRequest(Request request) {
        try {
            pendingResponse = new CompletableFuture<>();
            byte[] data = serialize(request);
            List<Chunk> chunks = ChunkUtils.split(data, UUID.randomUUID());
            for (Chunk c : chunks) {
                connector.send(RawData.outbound(
                        serialize(c),
                        new AddressEndpointContext(serverAddress),
                        null, false
                ));
            }

            Response response = pendingResponse.get(10, TimeUnit.SECONDS);
            return response;

        } catch (TimeoutException e) {
            return new Response(Collections.emptyList(), "Сервер не ответил (таймаут)");
        } catch (Exception e) {
            return new Response(Collections.emptyList(), "Ошибка: " + e.getMessage());
        } finally {
            pendingResponse = null;
        }
    }
}
