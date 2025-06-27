package services;

import constants.MessageKey;
import daos.ProductDAO;
import daos.PromotionDAO;
import dtos.Promotion;
import dtos.ProductViewModel;
import dtos.PromotionViewModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static utils.ServiceUtils.isNullOrEmptyString;

public class PromotionService {
    private final PromotionDAO pdao = new PromotionDAO();
    private final ProductDAO productDAO = new ProductDAO();

    // Create a new promotion
    public String create(String name, float discount, Date startDate, Date endDate, String status) throws SQLException {
        if (isNullOrEmptyString(name) || discount < 0 || discount > 100 || isNullOrEmptyString(status)) {
            return MessageKey.INVALID_PROMOTION_FORMAT;
        }

        LocalDate today = LocalDate.now();
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (startLocalDate.isBefore(today)) {
            return MessageKey.START_DATE_FUTURE;
        }

        if (startDate.after(endDate)) {
            return MessageKey.INVALID_PROMOTION_DATE;
        }

        if (pdao.isExist(name)) {
            return MessageKey.PROMOTION_EXISTED;
        }

        int rows = pdao.create(name, discount, startDate, endDate, status);
        return rows == 1 ? MessageKey.CREATE_PROMOTION_SUCCESS : MessageKey.CREATE_PROMOTION_FAILED;
    }

    // Update an existing promotion
    public String update(int id, String name, float discount, Date startDate, Date endDate, String status) throws SQLException {
        if (isNullOrEmptyString(name) || discount < 0 || discount > 100 || isNullOrEmptyString(status)) {
            return MessageKey.INVALID_PROMOTION_FORMAT;
        }

        LocalDate today = LocalDate.now();
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (startLocalDate.isBefore(today)) {
            return MessageKey.INVALID_PROMOTION_DATE;
        }

        if (startDate.after(endDate)) {
            return MessageKey.INVALID_PROMOTION_DATE;
        }

        Promotion existing = pdao.searchByID(id);
        if (existing == null) {
            return MessageKey.PROMOTION_NOT_FOUND;
        }

        int rows = pdao.update(id, name, discount, startDate, endDate, status);
        return rows == 1 ? MessageKey.UPDATE_PROMOTION_SUCCESS : MessageKey.UPDATE_PROMOTION_FAILED;
    }

    // Delete a promotion by ID
    public String delete(int id) throws SQLException {
        int rows = pdao.delete(id);
        return rows == 1 ? MessageKey.DELETE_PROMOTION_SUCCESS : MessageKey.DELETE_PROMOTION_FAILED;
    }

    // Search promotion by ID and include associated products
    public PromotionViewModel searchByID(int id) throws SQLException {
        Promotion promo = pdao.searchByID(id);
        if (promo == null) {
            return null; // or throw custom NotFoundException
        }
        List<ProductViewModel> products = productDAO.getProductsByPromoID(promo.getPromoID());
        return new PromotionViewModel(
                promo.getPromoID(),
                promo.getName(),
                promo.getDiscountPercent(),
                promo.getStartDate(),
                promo.getEndDate(),
                promo.getStatus(),
                products
        );
    }

    // Search promotions by name (partial match)
    public List<PromotionViewModel> searchByName(String name) throws SQLException {
        List<PromotionViewModel> result = new ArrayList<>();
        for (Promotion promo : pdao.searchByName(name)) {
            List<ProductViewModel> products = productDAO.getProductsByPromoID(promo.getPromoID());
            result.add(new PromotionViewModel(
                    promo.getPromoID(),
                    promo.getName(),
                    promo.getDiscountPercent(),
                    promo.getStartDate(),
                    promo.getEndDate(),
                    promo.getStatus(),
                    products
            ));
        }
        return result;
    }

    // Get all promotions with their products
    public List<PromotionViewModel> getAll() throws SQLException {
        List<PromotionViewModel> result = new ArrayList<>();
        for (Promotion promo : pdao.getAll()) {
            List<ProductViewModel> products = productDAO.getProductsByPromoID(promo.getPromoID());
            result.add(new PromotionViewModel(
                    promo.getPromoID(),
                    promo.getName(),
                    promo.getDiscountPercent(),
                    promo.getStartDate(),
                    promo.getEndDate(),
                    promo.getStatus(),
                    products
            ));
        }
        return result;
    }
}
