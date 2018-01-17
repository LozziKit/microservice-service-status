package io.lozzikit.servicestatus.checker;

import io.lozzikit.servicestatus.api.dto.Status;
import org.springframework.http.HttpStatus;

/**
 * Simple utilitarian class used to match integer HTTP response code
 * and StatusEnum HTTP status code
 */
public class StatusCodeMatcher {

    /**
     * Matches an integer HTTP response code with a StatusEnum
     * @param httpStatusCode An HTTP response code
     * @return The appropriate StatusEnum based on internal logic
     */
    public static Status.StatusEnum match(int httpStatusCode) throws IllegalArgumentException{

        if(httpStatusCode<100 || httpStatusCode > 527)
            throw new IllegalArgumentException("Illegal HTTP status code : "+httpStatusCode);

        int beginningDigit = httpStatusCode/100;

        switch(beginningDigit){

            case 2:
                return Status.StatusEnum.AVAILABLE;
            case 5:
                return Status.StatusEnum.DOWN;
            default:
                return Status.StatusEnum.UNAVAILABLE;
        }
    }
}
