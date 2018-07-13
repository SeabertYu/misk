package misk.client

import misk.config.Config
import misk.security.ssl.CertStoreConfig
import misk.security.ssl.TrustStoreConfig
import java.time.Duration

data class HttpClientsConfig(
  private val defaultConnectTimeout: Duration? = null,
  private val defaultWriteTimeout: Duration? = null,
  private val defaultReadTimeout: Duration? = null,
  private val ssl: HttpClientSSLConfig? = null,
  private val endpoints: Map<String, HttpClientEndpointConfig> = mapOf()
) : Config {
  /** @return The [HttpClientEndpointConfig] for the given client, populated with defaults as needed */
  operator fun get(clientName: String): HttpClientEndpointConfig {
    // TODO(mmihic): Cache, proxy, etc
    val endpointConfig = endpoints[clientName] ?: throw IllegalArgumentException(
        "no client configuration for endpoint $clientName")

    return HttpClientEndpointConfig(
        endpointConfig.url,
        connectTimeout = endpointConfig.connectTimeout ?: defaultConnectTimeout,
        writeTimeout = endpointConfig.writeTimeout ?: defaultWriteTimeout,
        readTimeout = endpointConfig.readTimeout ?: defaultReadTimeout,
        ssl = endpointConfig.ssl ?: ssl
    )
  }
}

data class HttpClientSSLConfig(
  val cert_store: CertStoreConfig?,
  val trust_store: TrustStoreConfig
)

data class HttpClientEndpointConfig(
  val url: String,
  val connectTimeout: Duration? = null,
  val writeTimeout: Duration? = null,
  val readTimeout: Duration? = null,
  val ssl: HttpClientSSLConfig? = null
)

