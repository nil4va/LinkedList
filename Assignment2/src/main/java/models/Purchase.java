package models;

import java.util.List;

public class Purchase {
    private final Product product;
    private int count;

    public Purchase(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    /**
     * parses purchase summary information from a textLine with format: barcode, amount
     *
     * @param textLine
     * @param products a list of products ordered and searchable by barcode
     *                 (i.e. the comparator of the ordered list shall consider only the barcode when comparing products)
     * @return a new Purchase instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Purchase fromLine(String textLine, List<Product> products) {
        String[] parts = textLine.split(", ");
        long barcode = Long.parseLong(parts[0]);
        int amount = Integer.parseInt(parts[1]);


        Product foundProduct = null;
        for (Product product : products) {
            if (product.getBarcode() == barcode) {
                foundProduct = product;
                return new Purchase(foundProduct, amount);
            }
        }
        return null;
    }

    /**
     * add a delta amount to the count of the purchase summary instance
     *
     * @param delta
     */
    public void addCount(int delta) {
        this.count += delta;
    }

    public long getBarcode() {
        return this.product.getBarcode();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return product.getBarcode() + "/" + product.getTitle() + "/" + count + "/" + String.format("%.2f", count * product.getPrice());
    }

    // TODO add public and private methods as per your requirements
}
