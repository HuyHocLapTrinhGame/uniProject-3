package daos;

import dtos.PriceHistoryTracking;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriceHistoryTrackingDAO {

    private static final String QUERY_GET_BY_PRODUCT_ID =
        "SELECT * FROM [dbo].[tblPriceHistory] WHERE [productID] = ? ORDER BY changeDate DESC";

    private static final String QUERY_UPDATE_NOTE =
        "UPDATE [dbo].[tblPriceHistory] SET note = ? WHERE historyID = ?";

    private static final String QUERY_DELETE_OLD =
        "DELETE FROM [dbo].[tblPriceHistory] " +
        "WHERE historyID IN (SELECT historyID FROM [dbo].[tblPriceHistory] WHERE changeDate < DATEADD(MONTH, -1, GETDATE()))";

    private static final String QUERY_GET_ALL =
        "SELECT * FROM [dbo].[tblPriceHistory] ORDER BY changeDate DESC";

    /**
     * Deletes all price history records older than one month.
     * 
     * @return the number of records deleted
     * @throws SQLException if a database access error occurs
     */
    public int deleteOldHistoriesByID() throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_DELETE_OLD)) {
            return ps.executeUpdate();
        }
    }

    /**
     * Updates the note field for a specific history record.
     * 
     * @param historyID the ID of the history record
     * @param note the new note content
     * @return the number of rows affected
     * @throws SQLException if a database access error occurs
     */
    public int updateNote(int historyID, String note) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_UPDATE_NOTE)) {
            ps.setString(1, note);
            ps.setInt(2, historyID);
            return ps.executeUpdate();
        }
    }

    /**
     * Retrieves all price history records for a specific product ID.
     * 
     * @param productID the product ID to filter by
     * @return a list of matching PriceHistoryTracking records
     * @throws SQLException if a database access error occurs
     */
    public List<PriceHistoryTracking> getHistoryByProductID(int productID) throws SQLException {
        List<PriceHistoryTracking> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_GET_BY_PRODUCT_ID)) {
            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PriceHistoryTracking history = new PriceHistoryTracking();
                history.setHistoryID(rs.getInt("historyID"));
                history.setProductID(rs.getInt("productID"));
                history.setChangeDate(rs.getTimestamp("changeDate"));
                history.setOldPrice(rs.getDouble("oldPrice"));
                history.setNewPrice(rs.getDouble("newPrice"));
                history.setNote(rs.getString("note"));
                list.add(history);
            }
        }
        return list;
    }

    /**
     * Retrieves all price history records in the database.
     * 
     * @return a list of all PriceHistoryTracking records
     * @throws SQLException if a database access error occurs
     */
    public List<PriceHistoryTracking> getAllHistory() throws SQLException {
        List<PriceHistoryTracking> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_GET_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PriceHistoryTracking history = new PriceHistoryTracking();
                history.setHistoryID(rs.getInt("historyID"));
                history.setProductID(rs.getInt("productID"));
                history.setChangeDate(rs.getTimestamp("changeDate"));
                history.setOldPrice(rs.getDouble("oldPrice"));
                history.setNewPrice(rs.getDouble("newPrice"));
                history.setNote(rs.getString("note"));
                list.add(history);
            }
        }
        return list;
    }
}
