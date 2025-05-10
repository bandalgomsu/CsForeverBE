package com.csforever.app.common.scope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


object CustomScope {
    val fireAndForget =
        kotlinx.coroutines.CoroutineScope(Dispatchers.IO + SupervisorJob())
}
