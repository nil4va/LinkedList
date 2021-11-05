import models.Purchase;
import models.PurchaseTracker;

import java.util.Comparator;

public class SupermarketStatisticsMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Supermarket Statistics processor\n");

        PurchaseTracker purchaseTracker = new PurchaseTracker();

        purchaseTracker.importProductsFromVault("/products.txt");
        purchaseTracker.importPurchasesFromVault("/purchases");

        Comparator<Purchase> ranker = (o1, o2) -> 0;

        purchaseTracker.showTops(5, "worst sales volume",
                ranker

        );
        purchaseTracker.showTops(5, "best sales revenue",
                ranker
        );

        purchaseTracker.showTotals();
    }


}
