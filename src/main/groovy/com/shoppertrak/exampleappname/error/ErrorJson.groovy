package com.shoppertrak.exampleappname.error

class ErrorJson {
    String timeStamp
    Integer status
    String error
    String message

    ErrorJson(int status, Map<String, Object> errorAttributes) {
        this.timeStamp = errorAttributes.get("timestamp").toString()
        this.status = status
        this.error = errorAttributes.get("error")
        this.message = errorAttributes.get("message")
    }
}
