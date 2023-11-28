package org.cardanofoundation.ledgersync.explorerconsumer.listeners;

import com.bloxbean.cardano.yaci.store.core.domain.Cursor;
import com.bloxbean.cardano.yaci.store.core.service.CursorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.BlockRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LedgerSyncEventListener {
    private final CursorService cursorService;
    private final BlockRepository blockRepository;

    @EventListener
    public void initialize(ApplicationReadyEvent applicationReadyEvent) {
        long slotHeight = blockRepository.getSlotHeight().orElse(0L);

        if (slotHeight == 0) {
            return;
        }

        Cursor currentCursor = cursorService.getCursor().orElse(null);
        if (currentCursor != null && currentCursor.getSlot() > slotHeight) {
            cursorService.rollback(slotHeight);
        }
    }
}
