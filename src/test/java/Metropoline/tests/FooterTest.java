package Metropoline.tests;

import org.testng.annotations.Test;
import Metropoline.Miscellaneous;

public class FooterTest extends BaseTest {

    private Miscellaneous miscellaneous;

    @Test
    public void testRandomFooterLinkClick() {
        // Initialize the single object we need
        miscellaneous = new Miscellaneous(page);

        // 1. Open Metropoline homepage
        page.navigate("https://www.metropoline.com");

        // 2. Wait and clear the screen
        page.waitForTimeout(2000);
        miscellaneous.closePopupIfExists();

        // 3. Ensure the footer is in view
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        page.waitForTimeout(1000);

        // --- FOOTER AND REGIONAL UPDATES ---
        // 4. EXECUTE: Click a random link
        //miscellaneous.clickRandomFooterLink();

        // Observe the final page
        //page.waitForTimeout(3000);
        //System.out.println("Finished Footer Test. Final URL: " + page.url());
        //miscellaneous.printSiteMapContent();

        // Check Sharon updates
        //miscellaneous.clickRandomUpdateByRegion("sharon_1");

        // Check South (Darom) updates
        //miscellaneous.clickRandomUpdateByRegion("darom_1");
        //miscellaneous.printAboutCompanyContent();
        //miscellaneous.clickRandomUpdateByRegion("Prices_tickets#");
        //miscellaneous.printAccessibilityStatement();
        //miscellaneous.printPrivacyPolicy();

        // --- ENVIRONMENTAL AND PUBLIC REPORTS (PDF) ---
        // 1. Air Pollution Report
        //miscellaneous.clickAndPrintPdf("https://www.metropoline.com/WS/PDF/%D7%93%D7%99%D7%95%D7%95%D7%97%20%D7%A4%D7%A8%D7%98%D7%A0%D7%992025.pdf");

        // 2. Smoke/Public Complaints Report
        //miscellaneous.clickAndPrintPdf("https://www.metropoline.com/WS/PDF/%D7%A4%D7%A0%D7%99%D7%95%D7%AA%20%D7%91%D7%A0%D7%95%D7%A9%D7%90%202025%D7%A2%D7%A9%D7%9F.pdf");

        // 3. Economical Driving Report
        //miscellaneous.clickAndPrintPdf("https://www.metropoline.com/WS/PDF/%D7%A0%D7%94%D7%99%D7%92%D7%94%202025%D7%97%D7%A1%D7%9B%D7%95%D7%A0%D7%99%D7%AA.pdf");

        // 4. Public Complaints Summary
        //miscellaneous.clickAndPrintPdf("https://www.metropoline.com/WS/PDF/%D7%A4%D7%A0%D7%99%D7%95%D7%AA%20%D7%91%D7%A0%D7%95%D7%A9%D7%90%202025%D7%A2%D7%A9%D7%9F.pdf");

        // --- ISO CERTIFICATION AND EXTRACTS ---
        //miscellaneous.captureAllPdfPages(
        //        "https://www.metropoline.com/WS/PDF/%D7%A7%D7%91%D7%A6%D7%99%20%D7%90%D7%99%D7%96%D7%95%20%D7%9E%D7%A2%D7%95%D7%93%D7%9B%D7%A0%D7%99%D7%9D%202026.pdf",
        //        "ISO_Certificate_2026"
        //);

        // Target 1: The ISO Certificate (4 pages)
        //miscellaneous.capturePdfScreenshots(
        //        "https://www.metropoline.com/WS/PDF/%D7%A7%D7%91%D7%A6%D7%99%20%D7%90%D7%99%D7%96%D7%95%20%D7%9E%D7%A2%D7%95%D7%93%D7%9B%D7%A0%D7%99%D7%9D%202026.pdf",
        //        "ISO_2026",
        //        4
        //);

        // Target 2: The Extract Pages document (also 4 pages)
        //miscellaneous.capturePdfScreenshots(
        //        "https://www.metropoline.com/WS/PDF/Untitled%20Extract%20Pages.pdf",
        //        "Extract_Doc",
        //        2
        //);

        // --- SUPPLIERS, ADVERTISING AND DRIVER CAMPAIGNS ---
        //miscellaneous.clickAndCapture("https://cdn.activated.digital/Metropolin/TheBestForDrivers/NL/", "Drivers_Landing_Page");
        //miscellaneous.captureSupplierForm();
        //miscellaneous.captureOrafimText();

        // --- LEGAL AND SETTLEMENT AGREEMENTS ---
        // Target: The Settlement Agreement - Full 31-page Capture
        //miscellaneous.capturePdfScreenshots(
        //        "https://www.metropoline.com/WS/PDF/%D7%A4%D7%A1%D7%A7%20%D7%94%D7%93%D7%99%D7%9F%20%D7%95%D7%94%D7%A1%D7%9B%D7%9D%20%D7%94%D7%A4%D7%A9%D7%A8%D7%94.pdf",
        //        "Settlement_Agreement_Full",
        //        33
        //);
        //page.waitForTimeout(2000);

        // Using the "Easy Task" method we established for the CDN landing pages
        //miscellaneous.clickAndCapture(
        //        "https://cdn.activated.digital/Metropolin/TheBestForDrivers/L/",
        //        "Drivers_Landing_Page_L"
        //);

    }
}