package io.shirohoo.membercrud.web.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpResponse<T> {
    private String resCode;
    private String resMsg;
    private T resBody;

    @SuppressWarnings("unchecked")
    private HttpResponse(final String resCode, final String resMsg) {
        this(resCode, resMsg, (T) "");
    }

    private HttpResponse(final String resCode, final String resMsg, final T resBody) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.resBody = resBody;
    }

    public static <T> HttpResponse<T> of(final String resCode, final String resMsg) {
        return new HttpResponse<>(resCode, resMsg);
    }

    public static <T> HttpResponse<T> of(final HttpStatus httpStatus, final String resMsg) {
        return new HttpResponse<>(parseStr(httpStatus.value()), resMsg);
    }

    public static <T> HttpResponse<T> of(final HttpStatus httpStatus) {
        return new HttpResponse<>(parseStr(httpStatus.value()), httpStatus.getReasonPhrase());
    }

    public static <T> HttpResponse<T> of(final String resCode, final String resMsg,
        final T resBody) {
        return new HttpResponse<>(resCode, resMsg, resBody);
    }

    private static String parseStr(final int number) {
        return String.valueOf(number);
    }
}
