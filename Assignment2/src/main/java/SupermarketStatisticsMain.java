import models.PurchaseTracker;

public class SupermarketStatisticsMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Supermarket Statistics processor\n");

        PurchaseTracker purchaseTracker = new PurchaseTracker();

        purchaseTracker.importProductsFromVault("/products.txt");

        purchaseTracker.importPurchasesFromVault("/purchases");

        // TODO provide the comparators that can order the purchases by specified criteria
        purchaseTracker.showTops(5, "worst sales volume",
                null
        );
        purchaseTracker.showTops(5, "best sales revenue",
                null
        );

        purchaseTracker.showTotals();
    }


}
