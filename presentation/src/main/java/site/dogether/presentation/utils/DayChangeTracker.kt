package site.dogether.presentation.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.LocalDate

interface DayChangeTracker {
    val dayFlow: SharedFlow<LocalDate>
}

class AndroidDayChangeTracker(context: Context) : DayChangeTracker {
    private val _dayFlow = MutableSharedFlow<LocalDate>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val dayFlow: SharedFlow<LocalDate> = _dayFlow.asSharedFlow()

    private var lastDate: LocalDate = LocalDate.now()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val currentDate = LocalDate.now()
            if (currentDate != lastDate) {
                lastDate = currentDate
                _dayFlow.tryEmit(currentDate)
            }
        }
    }

    init {
        _dayFlow.tryEmit(lastDate)
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        context.registerReceiver(receiver, filter)
    }
}
