package com.astrika.stywis_staff.master_controller.sync

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskIOThreadExecutor : Executor {

    private var mDiskIO: Executor = Executors.newSingleThreadExecutor()

    override fun execute(command: Runnable) {
        mDiskIO.execute(command)
    }

}