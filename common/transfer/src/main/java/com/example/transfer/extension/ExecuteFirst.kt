package com.example.transfer.extension

class ExecuteFirstDataHolder(private val busyTime: Long) {

    var thresholdTime: Long = System.currentTimeMillis()
    var actionRunning: Boolean = false

    companion object {

        suspend fun executeFirstAsync(holder: ExecuteFirstDataHolder, action: suspend () -> Unit) = with(holder) {
            val currentTime = System.currentTimeMillis()

            if (thresholdTime < currentTime && actionRunning)
                actionRunning = false

            if (!actionRunning) {
                actionRunning = true
                thresholdTime = currentTime + (busyTime)
                action()
            }
        }

        fun executeFirst(holder: ExecuteFirstDataHolder, action: () -> Unit) = with(holder) {
            val currentTime = System.currentTimeMillis()

            if (thresholdTime < currentTime && actionRunning)
                actionRunning = false

            if (!actionRunning) {
                actionRunning = true
                thresholdTime = currentTime + (busyTime)
                action()
            }
        }
    }
}
