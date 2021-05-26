# Complex Header Validation Example

Example demonstrating how to validate complex header scenarios with Spring filters.

## How it Works

There are 3 main components:

1. `HeaderRequirementFilter`
2. `HeaderRequirement`
3. `HeaderRequirementSpec`

### The Filter

If you're not familiar with filters, you should probably check
out [this article](https://tutorialspoint.com/spring_boot/spring_boot_servlet_filter.htm) before reading any further.

The `HeaderRequirementFilter` is dependent on configured header requirements and makes use of these to determine if the
headers on the incoming request are valid or not. If the headers are valid the chain executes normally, otherwise
a `400 Bad Request` response is sent directly from the filter implementation.

### The Requirement

`HeaderRequirements` simply accept a `HttpServletRequest` and produce a `Set<ConstraintViolation<T>>`. The `T` in this
case is the `spec` that will be used to validate the header values against using a `javax.Validator`. This solution also
comes with a `BaseHeaderRequirement` that comes with the common validation functionality out of the box. Making your
own `HeaderRequirement` might look like the below:

```java
import com.sexton.example.filter.BaseHeaderRequirement;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ExampleHeaderRequirement extends BaseHeaderRequirement<ExampleHeaderRequirementSpec> {
    @Override
    protected ExampleHeaderRequirementSpec buildValidationSpec(HttpServletRequest request) {
        return ExampleHeaderRequirementSpec.builder()
                .name(request.getHeader("X-Example-Header"))
                .build();
    }
}
```

Make sure the `HeaderRequirement` is decorated with `@Component` so that it is automatically configured with Spring and
injected into the filter.

### The Spec

The `spec` is the POJO object that the headers will be mapped too and validated by hibernate. Below is a simple example:

```java
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

// Spec includes example headers that an API might require
@Data
@Builder
public class ExampleHeaderRequirementSpec {
    @NotEmpty
    private final String example;
}
```

## Why This Solution?

In a single project this solution is probably a good example of over engineering unless you really just want to make use
of hibernate when validating incoming headers.

This solutions really shines as a reusable pattern due to header requirements being decoupled from the filter, and the
ability to make use of the entire hibernate validation ecosystem.