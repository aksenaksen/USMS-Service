package io.aksenaksen.demo.usms.accident.controller;

import io.aksenaksen.demo.usms.accident.repository.AccidentBehavior;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccidentBehaviorConverter implements Converter<String, AccidentBehavior> {

    @Override
    public AccidentBehavior convert(String source) {

        try {
            return AccidentBehavior.valueOfCode(Integer.parseInt(source));
        }
        catch (Exception e) {
            return null;
        }
    }
}
