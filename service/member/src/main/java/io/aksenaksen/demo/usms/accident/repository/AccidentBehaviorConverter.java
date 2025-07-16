package io.aksenaksen.demo.usms.accident.repository;


import jakarta.persistence.AttributeConverter;

public class AccidentBehaviorConverter implements AttributeConverter<AccidentBehavior, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccidentBehavior attribute) {

        return attribute.getCode();
    }

    @Override
    public AccidentBehavior convertToEntityAttribute(Integer code) {

        return AccidentBehavior.valueOfCode(code);
    }
}
