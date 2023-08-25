UPDATE block
SET tx_count = 0
WHERE tx_count IS NULL;
