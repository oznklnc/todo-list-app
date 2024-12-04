package com.demo.todo.list.app.security.filter;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.constant.TodoListConstants;
import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.security.TodoListUserDetails;
import com.demo.todo.list.app.security.jwt.JwtHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JWTAuthFilterTest extends UnitTest {

    @Mock
    JwtHelper jwtHelper;
    @Mock
    UserDetailsService userDetailsService;
    @InjectMocks
    JWTAuthFilter jwtAuthFilter;
    static final List<String> NOT_FILTERED_URLS = List.of("/api/v1/register", "/api/v1/login", "/api-docs/**");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtAuthFilter, "notFilteredUrls", NOT_FILTERED_URLS);
    }

    @Test
    void should_do_filter_internal_without_authorization() throws Exception {
        // Given
        MockHttpServletRequest request = mockHttpServletRequest("/api/v1/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        //when
        jwtAuthFilter.doFilter(request, response, filterChain);

        //then
        verifyNoInteractions(jwtHelper, userDetailsService);
    }

    @Test
    void should_do_filter_internal() throws Exception {
        // Given
        String username = "john.doe@example.com";
        String token = "token";
        String AUTHORIZATION = TodoListConstants.BEARER_PREFIX + token;
        MockHttpServletRequest request = mockHttpServletRequest("/api/v1/test");
        request.addHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        when(jwtHelper.validateToken(token)).thenReturn(true);
        when(jwtHelper.getUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails(username));

        //when
        jwtAuthFilter.doFilter(request, response, filterChain);

        //then
        InOrder inOrder = Mockito.inOrder(jwtHelper, userDetailsService);
        inOrder.verify(jwtHelper).validateToken(token);
        inOrder.verify(jwtHelper).getUsername(token);
        inOrder.verify(userDetailsService).loadUserByUsername(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isInstanceOf(TodoListUserDetails.class)
                .extracting("username").isEqualTo(username);
    }

    @Test
    void should_not_filter() {
        // Given
        MockHttpServletRequest request = mockHttpServletRequest("/api/v1/register");

        // When
        boolean result = jwtAuthFilter.shouldNotFilter(request);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void should_not_filter_for_complex_url_pattern() {
        // Given
        MockHttpServletRequest request = mockHttpServletRequest("/api-docs/api-config");

        // When
        boolean result = jwtAuthFilter.shouldNotFilter(request);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void should_filter() {
        // Given
        MockHttpServletRequest request = mockHttpServletRequest("/api/v1/test");

        // When
        boolean result = jwtAuthFilter.shouldNotFilter(request);

        // Then
        assertThat(result).isFalse();
    }

    private MockHttpServletRequest mockHttpServletRequest(String path) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath(path);
        return request;
    }

    private UserDetails mockUserDetails(String username) {
        UserDocument userDocument = UserDocument.builder()
                .email(username)
                .build();
        return new TodoListUserDetails(userDocument);
    }
}