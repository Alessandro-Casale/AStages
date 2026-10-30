package com.alessandro.astages.api.manager.registry;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.restriction.ARestriction;

@NotNullMethodsReturn
public interface AMinimalRegistry<R extends ARestriction<?, ?, ?>> extends ACommonRegistry<R> { }