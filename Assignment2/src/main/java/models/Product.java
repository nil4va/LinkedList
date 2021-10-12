package models;

import java.util.regex.Pattern;

public class Product {
    private final long barcode;
    private String title;
    private double price;

    public Product(long barcode) {
        this.barcode = barcode;
    }
    public Product(long barcode, String title, double price) {
        this(barcode);
        this.title = title;
        this.price = price;
    }

    /**
     * parses product information from a textLine with format: barcode, title, price
     * @param textLine
     * @return  a new Product instance with the provided information
     *          or null if the textLine is corrupt or incomplete
     */
    public static Product fromLine(String textLine) {
        boolean textLineRegex = Pattern.matches("([0-9]{13}|[0-9]{8})[,]\\s.+[,]\\s[0-9]+.[0-9]{2}", textLine);

        if (!textLineRegex) {
            return null;
        }

        String[] parts = textLine.split(",");
        String barcode = parts[0];
        String title = parts[1];
        String price = parts[2];

        return new Product(Long.parseLong(barcode), title, Double.parseDouble(price));
    }

    public long getBarcode() {
        return barcode;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Product)) return false;
        return this.getBarcode() == ((Product)other).getBarcode();
    }

    @Override
    public String toString() {
        return barcode + ", " + title + ", " + price ;
    }

    // TODO add public and private methods as per your requirements
}
