package lab7.server.network;

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
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.BiConsumer;

public class DTLSTransport {

    private final DTLSConnector connector;

    public DTLSTransport(int port) throws Exception {
        DtlsConfig.register();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream("server.jks"), "password".toCharArray());

        PrivateKey privateKey = (PrivateKey) keyStore.getKey("server", "password".toCharArray());
        Certificate[] certChain = keyStore.getCertificateChain("server");

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("server-trust.jks"), "password".toCharArray());

        List<X509Certificate> trustedCerts = new ArrayList<>();
        Enumeration<String> aliases = trustStore.aliases();
        while (aliases.hasMoreElements()) {
            String a = aliases.nextElement();
            if (trustStore.isCertificateEntry(a)) {
                trustedCerts.add((X509Certificate) trustStore.getCertificate(a));
            }
        }

        Configuration config = Configuration.getStandard();
        DtlsConnectorConfig.Builder builder = DtlsConnectorConfig.builder(config);
        builder.setAddress(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), port));

        builder.setCertificateIdentityProvider(
                new SingleCertificateProvider(privateKey, certChain, CertificateType.X_509)
        );

        builder.setAdvancedCertificateVerifier(
                StaticNewAdvancedCertificateVerifier.builder()
                        .setTrustedCertificates(trustedCerts.toArray(new X509Certificate[0]))
                        .build()
        );

        this.connector = new DTLSConnector(builder.build());
    }

    public void start(BiConsumer<byte[], InetSocketAddress> handler) throws IOException {
        connector.setRawDataReceiver(raw -> handler.accept(
                    raw.getBytes(),
                    raw.getInetSocketAddress()));
        connector.start();
    }

    public void send(byte[] data, InetSocketAddress addr) {
        AddressEndpointContext ctx = new AddressEndpointContext(addr);
        connector.send(RawData.outbound(data, ctx, null, false));
    }
}