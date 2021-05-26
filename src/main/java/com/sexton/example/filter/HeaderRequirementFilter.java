package com.sexton.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sexton.example.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HeaderRequirementFilter extends OncePerRequestFilter {
    private final List<? extends HeaderRequirement<?>> headerRequirements;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        val violations = getViolations(request);

        if (violations.isEmpty()) {
            chain.doFilter(request, response);
        } else {
            badRequest(response, violations);
        }
    }

    private Set<ConstraintViolation<?>> getViolations(HttpServletRequest request) {
        return headerRequirements.stream()
                .map(requirement -> requirement.satisfied(request))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void badRequest(HttpServletResponse response, Set<ConstraintViolation<?>> violations) throws IOException {
        val resp = ErrorResponse.badRequest(violations);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().write(objectMapper.writeValueAsString(resp));
    }
}
