import com.csforever.app.auth.filter.AuthorizationFilter
import com.csforever.app.auth.implement.TokenHandler
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class AuthorizationFilterTest {

    private val tokenHandler: TokenHandler = mockk()
    private val authPaths = listOf("/auth", "/login", "/register")
    private val filter = AuthorizationFilter(tokenHandler, authPaths)

    @Test
    fun `filter should proceed for non-authentication requests`() {
        // Given
        val exchange = mockk<ServerWebExchange>()
        val chain = mockk<WebFilterChain>()
        val request = mockk<ServerHttpRequest>(relaxed = true)
        val authorization = mockk<com.csforever.app.auth.model.UserAuthorizationContext>(relaxed = true)

        // Mocking request and response for non-auth request
        every { request.method.name() } returns "GET"
        every { request.path.value() } returns "/home"  // Non-auth path
        every { exchange.request } returns request
        every { exchange.attributes } returns mutableMapOf<String, Any>()  // Mocking attributes
        every { chain.filter(exchange) } returns Mono.empty()

        // Mock TokenHandler.extractToken to return a dummy authorization context
        coEvery { tokenHandler.extractToken(any()) } returns authorization

        // When
        val result = filter.filter(exchange, chain)

        // Then
        StepVerifier.create(result)
            .expectComplete()
            .verify()

        // Verify that the chain proceeded without token extraction
        verify { chain.filter(exchange) }
    }

    @Test
    fun `filter should proceed when user is authorized`() {
        // Given
        val exchange = mockk<ServerWebExchange>()
        val chain = mockk<WebFilterChain>()
        val request = mockk<ServerHttpRequest>(relaxed = true)
        val authorization = mockk<com.csforever.app.auth.model.UserAuthorizationContext>(relaxed = true)

        every { request.method.name() } returns "GET"
        every { request.path.value() } returns "/auth"  // Auth path
        every { request.headers.getFirst("Authorization") } returns "Bearer validToken"
        every { exchange.request } returns request
        every { exchange.attributes } returns mutableMapOf<String, Any>()  // Mocking attributes
        every { chain.filter(exchange) } returns Mono.empty()
        coEvery { tokenHandler.extractToken(any()) } returns authorization
        every { authorization.user } returns mockk()  // User is logged in

        // When
        val result = filter.filter(exchange, chain)

        // Then
        StepVerifier.create(result)
            .expectComplete()
            .verify()

        // Verify that the chain proceeded after authorization
        coVerify { chain.filter(exchange) }
    }


}
