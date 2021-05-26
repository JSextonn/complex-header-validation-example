package com.sexton.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sexton.example.filter.BaseHeaderRequirement;
import com.sexton.example.filter.HeaderRequirementFilter;
import com.sexton.example.util.RequestUtils;
import lombok.Builder;
import lombok.Data;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeaderRequirementFilterTests {
    private OncePerRequestFilter filter;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private FilterChain mockChain;

    @Mock
    private PrintWriter mockWriter;

    @BeforeEach
    void setup() {
        val requirements = List.of(new DummyHeaderRequirement());
        val objectMapper = new ObjectMapper();
        filter = new HeaderRequirementFilter(requirements, objectMapper);
    }

    @Test
    void shouldContinueWithChainWhenNoViolationsExist() throws ServletException, IOException {
        when(mockRequest.getHeader("X-Dummy-Header")).thenReturn("any");

        filter.doFilter(mockRequest, mockResponse, mockChain);

        verify(mockChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    void shouldRespondWithBadRequestWhenViolationsExist() throws ServletException, IOException {
        when(mockRequest.getHeader("X-Dummy-Header")).thenReturn(null);
        when(mockResponse.getWriter()).thenReturn(mockWriter);

        filter.doFilter(mockRequest, mockResponse, mockChain);

        verify(mockChain, never()).doFilter(mockRequest, mockResponse);

        verify(mockResponse).setContentType(MediaType.APPLICATION_JSON.toString());
        verify(mockResponse).setStatus(HttpStatus.BAD_REQUEST.value());
        verify(mockResponse).getWriter();

        verify(mockWriter).write("{" +
                "\"code\":400," +
                "\"status\":\"Bad Request\"," +
                "\"message\":\"Given inputs were incorrect. Consult the below details for more information: Header X-Dummy-Header must not be empty\"" +
                "}");
    }

    private static class DummyHeaderRequirement extends BaseHeaderRequirement<DummyHeaderRequirementSpec> {
        @Override
        protected DummyHeaderRequirementSpec buildValidationSpec(HttpServletRequest request) {
            return DummyHeaderRequirementSpec.builder()
                    .dummyHeader(RequestUtils.getHeader("X-Dummy-Header", request).orElse(""))
                    .build();
        }
    }

    @Data
    @Builder
    private static class DummyHeaderRequirementSpec {
        @NotEmpty(message = "Header X-Dummy-Header must not be empty")
        private final String dummyHeader;
    }
}
