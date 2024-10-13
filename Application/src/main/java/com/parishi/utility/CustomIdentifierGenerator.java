package com.parishi.utility;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import javax.persistence.Query;
import java.io.Serializable;
import java.time.LocalDate;

public class CustomIdentifierGenerator implements IdentifierGenerator
{
    private static final String PREFIX = "Batch-";
    private static final String SEPARATOR = "_";

    @Override
    public synchronized Serializable generate(SharedSessionContractImplementor session, Object object) {
        // Get the current financial year range (e.g., 2023-2024)
        String currentFinancialYear = getCurrentFinancialYearRange();

        // Query the current batch number and financial year from the database
        Query query = session.createNativeQuery("SELECT last_batch_number, financial_year FROM batch_number_sequence WHERE id = 1 FOR UPDATE");
        Object[] result = (Object[]) query.getSingleResult();

        int currentBatchNumber = ((Number) result[0]).intValue();
        String lastFinancialYear = (String) result[1];

        // Check if the financial year has changed
        if (!currentFinancialYear.equals(lastFinancialYear))
        {
            // If the financial year has changed, reset the batch number
            currentBatchNumber = 1;
            lastFinancialYear = currentFinancialYear;
        }
        else
        {
            // Increment the batch number
            currentBatchNumber++;
        }

        // Create the custom ID using the current batch number and financial year
        String id = PREFIX + currentBatchNumber + SEPARATOR + currentFinancialYear;

        // Update the batch number and financial year in the database
        Query updateQuery = session.createNativeQuery(
                "UPDATE batch_number_sequence SET last_batch_number = :batchNumber, financial_year = :financialYear WHERE id = 1"
        );
        updateQuery.setParameter("batchNumber", currentBatchNumber);
        updateQuery.setParameter("financialYear", lastFinancialYear);
        updateQuery.executeUpdate();

        // Return the generated ID
        return id;
    }

    private static String getCurrentFinancialYearRange() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int startYear = now.getMonthValue() < 4 ? year - 1 : year;
        int endYear = startYear + 1;
        return startYear + "-" + endYear;
    }
}
