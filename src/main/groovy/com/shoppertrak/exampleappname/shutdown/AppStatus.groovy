package com.shoppertrak.exampleappname.shutdown

class AppStatus {
    static final enum STATUS {
        UP, DOWN, SHUTTING_DOWN
    }

    static CURRENT_STATUS = STATUS.UP
}
