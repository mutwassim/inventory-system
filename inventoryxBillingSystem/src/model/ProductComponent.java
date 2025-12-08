package model;

/**
 * [PATTERN: COMPOSITE]
 * Reason: To treat individual Products and ProductBundles uniformly.
 * Both implement getPrice() and showDetails().
 */
public interface ProductComponent extends Cloneable {
    double getPrice();

    String getName();

    void showDetails();

    // [PATTERN: PROTOTYPE]
    // Reason: To copy expensive-to-create product objects.
    ProductComponent clone();
}
