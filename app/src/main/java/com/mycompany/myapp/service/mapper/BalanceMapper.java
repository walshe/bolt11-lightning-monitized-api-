package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.BalanceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Balance} and its DTO {@link BalanceDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface BalanceMapper extends EntityMapper<BalanceDTO, Balance> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    BalanceDTO toDto(Balance s);
}
