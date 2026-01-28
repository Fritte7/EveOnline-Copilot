package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fritte.eveonline.ui.model.VisitTimelineRow
import com.fritte.eveonline.ui.viewmodel.TimelineViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import com.fritte.eveonline.ui.nav.CommandEvent
import com.fritte.eveonline.ui.viewmodel.NavigationViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimelineScreen(
    vm: TimelineViewModel = koinViewModel(),
    navBus: NavigationViewModel = koinViewModel()
) {
    val entries = vm.timeline.collectAsState().value

    LaunchedEffect(Unit) {
        navBus.cmd.collect { ev ->
            when (ev) {
                CommandEvent.TimelineClear -> vm.clearTimeline()
                is CommandEvent.TimelineLimit -> vm.setLimit(ev.limit)
                else -> Unit
            }
        }
    }

    TerminalScaffold {
        TerminalPanel {
            TerminalLine("Recents visits")
            TerminalRow("Count", entries.size.toString())
        }

        Spacer(Modifier.height(12.dp))

        if (entries.isEmpty()) {
            TerminalPanel {
                TerminalRow("Status", "No visits recorded")
            }
            return@TerminalScaffold
        }

        // List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = entries,
                key = { it.visitedId }
            ) { e ->
                TimelineEntryCard(
                    entry = e,
                )
            }
        }
    }


}

@Composable
private fun TimelineEntryCard(
    entry: VisitTimelineRow,
) {
    val dateText = remember(entry.visitedAt) {
        Instant.ofEpochMilli(entry.visitedAt)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd.MM.yy - HH:mm", Locale.getDefault()))
    }

    TerminalPanel(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TerminalRow("Visited Date", dateText)
        TerminalRow("System", entry.systemName)
    }
}
