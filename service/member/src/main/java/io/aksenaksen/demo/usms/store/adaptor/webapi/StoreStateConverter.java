package io.aksenaksen.demo.usms.store.adaptor.webapi;

import io.aksenaksen.demo.usms.store.domain.StoreState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StoreStateConverter implements Converter<String, StoreState> {

    @Override
    public StoreState convert(String code) {

        try {
            return StoreState.valueOfCode(Integer.parseInt(code));
        }
        catch (Exception e) {
//            throw new NotExistingStreamKeyException();
            throw new RuntimeException();
        }
    }
}
