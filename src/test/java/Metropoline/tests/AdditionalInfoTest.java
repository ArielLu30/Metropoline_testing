package Metropoline.tests;

import com.microsoft.playwright.Locator;
import org.testng.annotations.Test;
import Metropoline.AdditionalInfoPages;

public class AdditionalInfoTest extends BaseTest {

    private AdditionalInfoPages additionalInfoPages;

    @Test
    public void testOrderSeatsFlow() {
        additionalInfoPages = new AdditionalInfoPages(page);

        // --- INITIALIZATION ---
        page.navigate("https://www.metropoline.com");

        // Wait for the UI to settle
        page.waitForTimeout(2000);

        // Run your methods
        additionalInfoPages.closePopupIfExists();

        // --- SEAT RESERVATION FLOW (KOL KASHER) ---
        //page.navigate("https://metropline.kolkasher.co.il/");
        //additionalInfoPages.clickRandomRoute();
        //page.waitForTimeout(2000);
        //additionalInfoPages.handleOneWaySelection();
        //page.waitForTimeout(2000);
        //additionalInfoPages.handleRoundTripSelection();
        //page.waitForTimeout(2000);

        // --- PERSONAL DETAILS & ORDER SUMMARY ---
        // 2. Use the new class instance to fill the form
        //additionalInfoPages.fillPersonalDetails(
        //        "ישראלי",
        //        "ישראל",
        //        "ההגנה 10",
        //        "חיפה",
        //        "0520000000",
        //        "test@metropoline.com"
        //);
        //additionalInfoPages.printOrderSummary();

        // --- FORMS & CUSTOMER SERVICE ---
        //additionalInfoPages.fillContactForm("477", "ישראל ישראלי", "test@email.com", "0501234567");
        //additionalInfoPages.selectRandomFAQAndPrint();
        //System.out.println("Test finished successfully.");

        // --- RECRUITMENT & RENTALS ---
        //additionalInfoPages.handleJobsSection();
        //additionalInfoPages.fillFullRentalForm(
        //        "ישראל ישראלי",    // fullName
        //        "0501234567",       // phone
        //        "test@gmail.com",    // email
        //        "תל אביב",          // from
        //        "ירושלים",          // to
        //        "50"                // passengers
        //);

        // --- INFORMATION & PAYMENTS ---
        //additionalInfoPages.printLoadThenRideContent();
        //additionalInfoPages.verifyExternalMobilePaymentLink();
    }
}