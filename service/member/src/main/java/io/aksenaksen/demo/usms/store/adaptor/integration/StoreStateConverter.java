package io.aksenaksen.demo.usms.store.adaptor.integration;


import io.aksenaksen.demo.usms.store.domain.StoreState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public class StoreStateConverter implements AttributeConverter<StoreState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StoreState attribute) {
        return attribute.getCode();
    }

    @Override
    public StoreState convertToEntityAttribute(Integer dbData) {
        return StoreState.valueOfCode(dbData);
    }
}
