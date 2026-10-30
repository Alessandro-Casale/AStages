package com.alessandro.astages.api.manager.registry;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.restriction.AClientRestriction;

@NotNullMethodsReturn
public interface AClientMinimalRegistry<R extends AClientRestriction<?, ?, ?>> extends ACommonRegistry<R> { }