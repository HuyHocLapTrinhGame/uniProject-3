package constants;

public class MessageKey {
    // System
    public static final String SYSTEM_ERROR = "system.error";
    public static final String UNAUTHENTICATED = "unauthenticated";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String CONTROLLER_NOT_FOUND = "controller.not.found";
    public static final String ALL_FIELDS_REQUIRED = "all.fields.required";
    public static final String INPUT_POSITIVE_NUMBER = "input.positive.number";
    public static final String SUCCESS = "success";

    // User
    public static final String PASSWORD_NOT_MATCH = "password.not.match";
    public static final String PASSWORD_AND_CONFIRM_REQUIRED = "password.confirm.required";
    public static final String OLD_PASSWORD_REQUIRED = "old.password.required";
    public static final String INVALID_PHONE_FORMAT = "invalid.phone.format";
    public static final String ROLE_ID_NOT_FOUND = "role.id.not.found";
    public static final String REGISTER_USER_SUCCESS = "register.user.success";
    public static final String REGISTER_USER_FAILED = "register.user.fail";
    public static final String CREATE_USER_SUCCESS = "create.user.success";
    public static final String CREATE_USER_FAILED = "create.user.fail";
    public static final String USER_ID_EXISTED = "user.id.existed";
    public static final String UPDATE_USER_SUCCESS = "update.user.success";
    public static final String UPDATE_USER_FAILED = "update.user.fail";
    public static final String DELETE_USER_SUCCESS = "delete.user.success";
    public static final String USER_NOT_FOUND = "user.not.found";
    public static final String INVALID_USER_OR_PASSWORD = "invalid.user.or.pass";

    // Invoice
    public static final String CREATE_INVOICE_FAILED = "create.invoice.fail";
    public static final String CREATE_INVOICE_SUCCESS = "create.invoice.success";
    public static final String UPDATE_INVOICE_SUCCESS = "update.invoice.success";
    public static final String UPDATE_INVOICE_FAILED = "update.invoice.fail";
    public static final String DELETE_INVOICE_SUCCESS = "delete.invoice.success";
    public static final String INVOICE_NOT_FOUND = "invoice.not.found";
    public static final String PRODUCT_NOT_EXIST = "product.not.exist";
    public static final String USER_NOT_EXIST = "user.not.exist";
    public static final String INVOICE_NOT_CREATOR = "invoice.not.creator";

    public static final String CREATE_INVOICE_DETAIL_FAILED = "create.invoice.detail.fail";
    public static final String CREATE_INVOICE_DETAIL_SUCCESS = "create.invoice.detail.success";
    public static final String UPDATE_INVOICE_DETAIL_SUCCESS = "update.invoice.detail.success";
    public static final String UPDATE_INVOICE_DETAIL_FAILED = "update.invoice.detail.fail";
    public static final String DELETE_INVOICE_DETAIL_SUCCESS = "delete.invoice.detail.success";
    public static final String INVOICE_DETAIL_NOT_FOUND = "invoice.detail.not.found";
    public static final String INVOICE_EMPTY = "invoice.empty";
    public static final String INVOICE_ADD_ERROR = "invoice.add.error";
    public static final String INVOICE_REMOVE_ERROR = "invoice.remove.error";
            
    // Product
    public static final String PRODUCT_NOT_FOUND = "product.not.found";
    public static final String PRODUCT_INACTIVE_OR_OUT = "product.inactive.or.out";
    public static final String NO_MATCHING_PRODUCTS = "no.matching.products";
    public static final String INVALID_QUANTITY = "invalid.quantity";
    public static final String INVALID_PRICE_OR_QUANTITY = "invalid.price.or.quantity";
    public static final String USER_NOT_SELLER = "user.not.seller";
    public static final String INVALID_STATUS = "invalid.status";
    public static final String CREATE_PRODUCT_SUCCESS = "create.product.success";
    public static final String CREATE_PRODUCT_FAILED = "create.product.fail";
    public static final String UPDATE_PRODUCT_SUCCESS = "update.product.success";
    public static final String UPDATE_PRODUCT_FAILED = "update.product.fail";
    public static final String DELETE_PRODUCT_SUCCESS = "delete.product.success";
    public static final String DELETE_PRODUCT_FAILED = "delete.product.fail";

    // Cart
    public static final String CART_EXISTED = "cart.existed";
    public static final String CART_NOT_FOUND = "cart.not.found";
    public static final String CART_DETAIL_NOT_FOUND = "cart.detail.not.found";
    public static final String QUANTITY_EXCEEDS_AVAILABLE = "quantity.exceeds";
    public static final String CREATE_CART_SUCCESS = "create.cart.success";
    public static final String CREATE_CART_FAILED = "create.cart.fail";
    public static final String UPDATE_CART_SUCCESS = "update.cart.success";
    public static final String UPDATE_CART_FAILED = "update.cart.fail";
    public static final String DELETE_CART_SUCCESS = "delete.cart.success";
    public static final String DELETE_CART_FAILED = "delete.cart.fail";
    public static final String ADD_TO_CART_SUCCESS = "add.cart.success";
    public static final String ADD_TO_CART_FAILED = "add.cart.fail";
    public static final String DELETE_ITEMS_FROM_CART_SUCCESS = "delete.items.success";
    public static final String DELETE_ITEMS_FROM_CART_FAILED = "delete.items.fail";
    public static final String CLEAR_CART_SUCCESS = "clear.cart.success";
    public static final String CLEAR_CART_FAILED = "clear.cart.fail";
    public static final String NO_ITEMS_SELECTED = "no.items.selected";
    public static final String CART_EMPTY = "cart.empty";
    public static final String CART_REMOVE_ERROR = "cart.remove.error";

    // Category
    public static final String CATEGORY_EXISTED = "category.existed";
    public static final String CREATE_CATEGORY_SUCCESS = "create.category.success";
    public static final String CREATE_CATEGORY_FAILED = "create.category.fail";
    public static final String CATEGORY_NOT_FOUND = "category.not.found";
    public static final String UPDATE_CATEGORY_SUCCESS = "update.category.success";
    public static final String UPDATE_CATEGORY_FAILED = "update.category.fail";
    public static final String DELETE_CATEGORY_SUCCESS = "delete.category.success";
    public static final String INVALID_CATEGORY_FORMAT = "invalid.category.format";

    // Promotion
    public static final String PROMOTION_EXISTED = "promotion.existed";
    public static final String CREATE_PROMOTION_SUCCESS = "create.promotion.success";
    public static final String CREATE_PROMOTION_FAILED = "create.promotion.fail";
    public static final String PROMOTION_NOT_FOUND = "promotion.not.found";
    public static final String UPDATE_PROMOTION_SUCCESS = "update.promotion.success";
    public static final String UPDATE_PROMOTION_FAILED = "update.promotion.fail";
    public static final String DELETE_PROMOTION_SUCCESS = "delete.promotion.success";
    public static final String DELETE_PROMOTION_FAILED = "delete.promotion.fail";
    public static final String INVALID_PROMOTION_FORMAT = "invalid.promotion.format";
    public static final String INVALID_PROMOTION_DATE = "invalid.promotion.date";
    public static final String START_DATE_FUTURE = "start.date.future";
    public static final String INVALID_FORMAT = "invalid.format";

    // Customer care
    public static final String CUSTOMERCARE_EXISTED = "customercare.existed";
    public static final String CREATE_CUSTOMERCARE_SUCCESS = "create.customercare.success";
    public static final String CREATE_CUSTOMERCARE_FAILED = "create.customercare.fail";
    public static final String CUSTOMERCARE_NOT_FOUND = "customercare.not.found";
    public static final String UPDATE_CUSTOMERCARE_SUCCESS = "update.customercare.success";
    public static final String UPDATE_CUSTOMERCARE_FAILED = "update.customercare.fail";
    public static final String DELETE_CUSTOMERCARE_SUCCESS = "delete.customercare.success";
    public static final String DELETE_CUSTOMERCARE_FAILED = "delete.customercare.fail";
    public static final String INVALID_CUSTOMERCARE_FORMAT = "invalid.customercare.format";

    // Delivery
    public static final String CREATE_DELIVERY_SUCCESS = "create.delivery.success";
    public static final String CREATE_DELIVERY_FAILED = "create.delivery.fail";
    public static final String UPDATE_DELIVERY_SUCCESS = "update.delivery.success";
    public static final String UPDATE_DELIVERY_FAILED = "update.delivery.fail";
    public static final String DELETE_DELIVERY_SUCCESS = "delete.delivery.success";
    public static final String DELIVERY_NOT_FOUND = "delivery.not.found";

    // Return
    public static final String CREATE_RETURN_SUCCESS = "create.return.success";
    public static final String UPDATE_RETURN_SUCCESS = "update.return.success";
    public static final String UPDATE_RETURN_FAILED = "update.return.fail";
    public static final String DELETE_RETURN_SUCCESS = "delete.return.success";
    public static final String RETURN_NOT_FOUND = "return.not.found";
    
    //History
    public static final String CREATE_HISTORY = "New history updated";
}
