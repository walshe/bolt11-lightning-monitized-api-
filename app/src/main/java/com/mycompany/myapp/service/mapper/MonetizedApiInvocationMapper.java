package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MonetizedApiInvocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonetizedApiInvocation} and its DTO {@link MonetizedApiInvocationDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface MonetizedApiInvocationMapper extends EntityMapper<MonetizedApiInvocationDTO, MonetizedApiInvocation> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    MonetizedApiInvocationDTO toDto(MonetizedApiInvocation s);
}
