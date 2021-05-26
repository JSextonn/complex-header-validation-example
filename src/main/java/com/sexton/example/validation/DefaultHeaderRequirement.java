package com.sexton.example.validation;

import com.sexton.example.filter.BaseHeaderRequirement;
import com.sexton.example.util.RequestUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class DefaultHeaderRequirement extends BaseHeaderRequirement<DefaultHeaderRequirementSpec> {
    @Override
    protected DefaultHeaderRequirementSpec buildValidationSpec(HttpServletRequest request) {
        return DefaultHeaderRequirementSpec.builder()
                .name(RequestUtils.getHeader("X-Name", request).orElse(""))
                .build();
    }
}
