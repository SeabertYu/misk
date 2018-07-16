package misk.web.interceptors

import misk.MiskModule
import misk.asAction
import misk.metrics.Metrics
import misk.testing.MiskTest
import misk.testing.MiskTestModule
import misk.web.Get
import misk.web.NetworkChain
import misk.web.NetworkInterceptor
import misk.web.Request
import misk.web.Response
import misk.web.actions.WebAction
import misk.web.actions.asNetworkChain
import okhttp3.HttpUrl
import okio.Buffer
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jetty.http.HttpMethod
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MiskTest
class MetricsInterceptorTest {
  @MiskTestModule
  val module = MiskModule()

  @Inject internal lateinit var metricsInterceptorFactory: MetricsInterceptor.Factory
  @Inject internal lateinit var testAction: TestAction
  @Inject internal lateinit var metrics: Metrics

  @BeforeEach
  fun sendRequests() {
    assertThat(invoke(200).statusCode).isEqualTo(200)
    assertThat(invoke(200).statusCode).isEqualTo(200)
    assertThat(invoke(202).statusCode).isEqualTo(202)
    assertThat(invoke(404).statusCode).isEqualTo(404)
    assertThat(invoke(403).statusCode).isEqualTo(403)
    assertThat(invoke(403).statusCode).isEqualTo(403)
  }

  @Test
  fun requests() {
    assertThat(metrics.counters["web.TestAction.requests"]!!.count).isEqualTo(6)
  }

  @Test
  fun responseCodes() {
    assertThat(metrics.counters["web.TestAction.responses.2xx"]!!.count).isEqualTo(3)
    assertThat(metrics.counters["web.TestAction.responses.200"]!!.count).isEqualTo(2)
    assertThat(metrics.counters["web.TestAction.responses.202"]!!.count).isEqualTo(1)
    assertThat(metrics.counters["web.TestAction.responses.4xx"]!!.count).isEqualTo(3)
    assertThat(metrics.counters["web.TestAction.responses.404"]!!.count).isEqualTo(1)
    assertThat(metrics.counters["web.TestAction.responses.403"]!!.count).isEqualTo(2)
  }

  @Test
  fun timing() {
    assertThat(metrics.timers["web.TestAction.timing"]!!.count).isEqualTo(6)
  }

  fun invoke(desiredStatusCode: Int): Response<String> {
    val request = Request(
        HttpUrl.parse("http://foo.bar/")!!,
        HttpMethod.GET,
        body = Buffer()
    )
    val metricsInterceptor = metricsInterceptorFactory.create(TestAction::call.asAction())!!
    val chain = testAction.asNetworkChain(TestAction::call, request, metricsInterceptor,
        TerminalInterceptor(desiredStatusCode))

    @Suppress("UNCHECKED_CAST")
    return chain.proceed(chain.request) as Response<String>
  }

  internal class TestAction : WebAction {
    @Get("/call/{result}")
    fun call(desiredStatusCode: Int): Response<String> {
      return Response("foo", statusCode = desiredStatusCode)
    }
  }

  internal class TerminalInterceptor(val status: Int) : NetworkInterceptor {
    override fun intercept(chain: NetworkChain): Response<*> = Response("foo", statusCode = status)
  }
}
