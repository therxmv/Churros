package com.therxmv.churros

import com.therxmv.churros.core.di.appModule
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Called from Swift AppDelegate/App to initialize Koin for the iOS target.
 *
 * TODO (Phase 2 iOS): Replace empty placeholder strings with real values read from
 *   xcconfig / Info.plist (see iosApp/Configuration/Config.xcconfig). Pattern:
 *   1. Add SUPABASE_URL and SUPABASE_ANON_KEY entries to Config.xcconfig (gitignored).
 *   2. Expose them in Info.plist.
 *   3. Read them in Swift via Bundle.main.infoDictionary and pass here.
 */
fun initKoin() {
    startKoin {
        modules(
            module {
                // TODO: Replace with credentials from xcconfig/Info.plist (Phase 2 iOS setup)
                single(named("supabaseUrl")) { "" }
                single(named("supabaseKey")) { "" }
            },
            appModule,
        )
    }
}
