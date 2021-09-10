package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MonetizedApiDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonetizedApi} and its DTO {@link MonetizedApiDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MonetizedApiMapper extends EntityMapper<MonetizedApiDTO, MonetizedApi> {}
