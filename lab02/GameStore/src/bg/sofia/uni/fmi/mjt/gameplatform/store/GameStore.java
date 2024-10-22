package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GameStore implements StoreAPI {

    StoreItem[] availableItems;
    boolean[] appliedDiscount;

    public GameStore(StoreItem[] availableItems) {
        this.availableItems = availableItems;
        this.appliedDiscount = new boolean[availableItems.length];
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {

        StoreItem[] filteredItems = new StoreItem[availableItems.length];
        int filteredCount = 0;

        for (StoreItem item : availableItems) {
            boolean matchesAll = true;
            for (ItemFilter filter : itemFilters) {
                if (!filter.matches(item)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                filteredItems[filteredCount++] = item;
            }
        }

        StoreItem[] result = new StoreItem[filteredCount];
        System.arraycopy(filteredItems, 0, result, 0, filteredCount);

        return result;
    }

    @Override
    public void applyDiscount(String promoCode) {
        BigDecimal discount = BigDecimal.ONE;

        switch (promoCode) {
            case "VAN40" -> discount = new BigDecimal("0.60");
            case "100YO" -> discount = BigDecimal.ZERO;
        }
        for (int i = 0; i < availableItems.length; i++) {
            if (!appliedDiscount[i]) {
                StoreItem item = availableItems[i];
                BigDecimal newPrice = item.getPrice().multiply(discount).setScale(2, RoundingMode.HALF_UP);
                item.setPrice(newPrice);
                appliedDiscount[i] = true;
            }
        }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if (rating < 1 || rating > 5) {
            return false;
        }

        item.rate(rating);
        return true;
    }
}
