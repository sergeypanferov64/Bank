package ru.spanferov.interview.test.bank.controller.rest.util;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;
import ru.spanferov.interview.test.bank.exception.BankException;

import java.util.Map;

public class RestErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> attrs = super.getErrorAttributes(requestAttributes, includeStackTrace);
        Throwable error = getError(requestAttributes);
        if (error instanceof BankException) {
            attrs.put("business_error_message", error.getMessage());
        }
        return attrs;
    }
}
