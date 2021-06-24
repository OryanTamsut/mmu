package com.mbj.server;

import java.io.Serializable;
import java.util.Map;

public class Request<T> implements Serializable {

    private Map<String, String> headers;
    private T body;

    /**
     * constructor
     * @param headers headers of the request
     * @param body the body of the request
     */
    public Request(Map<String, String> headers, T body) {
        this.headers = headers;
        this.body = body;

    }

    /**
     * get headers
     * @return the headers of the request
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * get body
     * @return the body of the request
     */
    public T getBody() {
        return body;
    }


    /**
     * set the body
     * @param body body to set
     */
    public void setBody(T body) {
        this.body = body;
    }

    /**
     * set headers
     * @param headers headers to set
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return string that represent the request
     */
    @Override
    public String toString() {

        return "{" +
                "headers:{" + headers +
                "},\n body:{" + body +
                "}\n}";
    }

}
