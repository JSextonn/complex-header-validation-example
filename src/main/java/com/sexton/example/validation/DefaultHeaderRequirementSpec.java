package com.sexton.example.validation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class DefaultHeaderRequirementSpec {
    @NotEmpty(message = "Header X-Name must not be empty")
    private final String name;
}
