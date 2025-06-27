package services;

import daos.PriceHistoryTrackingDAO;
import dtos.PriceHistoryTracking;

import java.util.List;

public class PriceHistoryService {
    private final PriceHistoryTrackingDAO historyDAO = new PriceHistoryTrackingDAO();

    /**
     * Get all price history records by product ID.
     */
    public List<PriceHistoryTracking> getHistoryByProductID(int productID) {
        try {
            return historyDAO.getHistoryByProductID(productID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all price history records in the database.
     */
    public List<PriceHistoryTracking> getAllHistory() {
        try {
            return historyDAO.getAllHistory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update the note field for a specific history record.
     * 
     * @param historyID ID of the history record
     * @param note      New note content
     * @return status message ("success", "fail", or "error")
     */
    public String updateNote(int historyID, String note) {
        try {
            int rowsAffected = historyDAO.updateNote(historyID, note);
            return rowsAffected > 0 ? "Note updated successfully." : "No record updated.";
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while updating the note.";
        }
    }

    /**
     * Delete history records older than 1 month.
     * 
     * @return deletion result message
     */
    public String deleteOldHistoriesByID() {
        try {
            int rowsDeleted = historyDAO.deleteOldHistoriesByID();
            return rowsDeleted > 0
                    ? "Successfully deleted " + rowsDeleted + " old record(s)."
                    : "No outdated history found to delete.";
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while deleting old records.";
        }
    }
}
