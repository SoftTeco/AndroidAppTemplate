package com.softteco.template.data.repository.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * supplies a custom [CoroutineScope] for async repository operations
 */
abstract class BaseRepository : CoroutineScope by MainScope()
