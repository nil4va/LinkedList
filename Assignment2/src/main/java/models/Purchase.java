package models;

import java.util.List;
import java.util.regex.Pattern;

public class Purchase {
    private final Product product;
    private int count;

    public Purchase(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    /**
     * parses purchase summary information from a textLine with format: barcode, amount
     * @param textLine
     * @param products  a list of products ordered and searchable by barcode
     *                  (i.e. the comparator of the ordered list shall consider only the barcode when comparing products)
     * @return  a new Purchase instance with the provided information
     *          or null if the textLine is corrupt or incomplete
     */
    public static Purchase fromLine(String textLine, List<Product> products) {

        // TODO convert the information in the textLine to a new Purchase instance
        //  use the products.indexOf to find the product that is associated with the barcode of the purchase
        boolean textLineRegex = Pattern.matches("([0-9]{13}|[0-9]{8})[,][0-9]+", textLine);

        if (!textLineRegex) {
            return null;
        }

        String[] parts = textLine.split(",");
        long barcode = Long.parseLong(parts[0]);
        String amount = parts[2];

        Product product = null;

        for (int i = 0; i < products.size(); i++) {
            if (i == products.indexOf(barcode)){
                product = new Product(barcode);
            }
        }

        return new Purchase(product, Integer.parseInt(amount));
    }

    /**
     * add a delta amount to the count of the purchase summary instance
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
        return product + ", " + count;
    }

    // TODO add public and private methods as per your requirements
}