package be.kdg.integration3.presentation.converters;

import be.kdg.integration3.domain.UserAccount.*;
import org.springframework.core.convert.converter.Converter;

public class StringToUseCaseConverter  implements Converter<String, UseCaseType> {
    @Override
    public UseCaseType convert(String useCase) {
        return UseCaseType.valueOf(useCase);
    }
}
