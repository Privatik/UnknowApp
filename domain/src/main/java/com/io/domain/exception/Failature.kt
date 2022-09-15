package com.io.domain.exception

sealed class Fail: Throwable(){
    class AuthFail(): Throwable(message = "Not Auth")
    class ForbiddenFail(): Throwable(message = "Not Auth")
    class GlobalFail(throwable: Throwable): Throwable(message = throwable.message)

}
