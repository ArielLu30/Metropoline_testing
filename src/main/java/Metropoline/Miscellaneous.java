package Metropoline;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.KeyboardModifier;
import com.microsoft.playwright.options.LoadState;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.BufferedInputStream;
import java.net.URL;
import com.microsoft.playwright.Download; // Add this import
import java.net.URLConnection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Arrays;

public class Miscellaneous {
    private Page page;

    // --- 1. INITIALIZATION & DIALOGS ---
    // Constructor
    public Miscellaneous(Page page) {
        this.page = page;
    }

    public void closePopupIfExists() {
        Locator closePopup = page.locator("button#closeDialog");
        if (closePopup.count() > 0) {
            closePopup.click();
            System.out.println("Popup closed successfully.");
        } else {
            System.out.println("No popup appeared.");
        }
    }

    // --- 2. REGIONAL UPDATES & COMPANY INFO ---
    public void clickRandomUpdateByRegion(String regionCode) {
        // 1. Navigate to the dynamic URL based on the regionCode parameter
        String targetUrl = "https://www.metropoline.com/Pages/Questions.aspx?p=" + regionCode;
        page.navigate(targetUrl);
        System.out.println("Navigated to: " + targetUrl);

        // 2. Locate all 'opener' links (the update headers)
        Locator updateLinks = page.locator("a.opener.summary");

        // Ensure elements are loaded
        try {
            updateLinks.first().waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (Exception e) {
            System.out.println("No updates found for region: " + regionCode);
            return;
        }

        int totalUpdates = updateLinks.count();

        if (totalUpdates > 0) {
            // 3. Pick a random index
            int randomIndex = (int) (Math.random() * totalUpdates);
            Locator selectedUpdate = updateLinks.nth(randomIndex);

            System.out.println("Randomly selected update: " + selectedUpdate.innerText().trim());

            // 4. Click to expand the details
            selectedUpdate.click();

            // 5. Locate the content inside the parent <li> of the clicked link
            Locator detailContent = selectedUpdate.locator("xpath=parent::li").locator("div.ng-scope").first();

            // Wait for visibility to ensure animation finished
            detailContent.waitFor();

            String infoText = detailContent.innerText().trim();

            System.out.println("--- UPDATE DETAILS (" + regionCode + ") ---");
            System.out.println(infoText);
            System.out.println("----------------------");
        }
    }

    public void printAboutCompanyContent() {
        // 1. Navigate directly to the About page
        page.navigate("https://www.metropoline.com/Pages/contentA.aspx?p=About");

        // 2. Wait for the specific article content to load
        Locator aboutContent = page.locator("article.col-left");
        aboutContent.waitFor();

        // 3. Extract and print the content
        System.out.println("--- METROPOLINE: ABOUT THE COMPANY ---");

        String text = aboutContent.innerText().trim();

        if (!text.isEmpty()) {
            System.out.println(text);
        } else {
            System.out.println("Error: Could not retrieve text from the About page.");
        }
        System.out.println("---------------------------------------");
    }

    // --- 3. SITE NAVIGATION & LEGAL POLICIES ---
    public void printSiteMapContent() {
        // 1. Click the "Maps" link in the footer
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("מפת אתר").setExact(true)).click();

        // 2. Wait for the site map page to load
        page.waitForSelector("nav[role='navigation']");

        // 3. Print the "Top Menu" (תפריט עליון)
        System.out.println("--- תפריט עליון ---");
        Locator topMenuItems = page.locator("nav.pull-right").first().locator("li");
        for (int i = 0; i < topMenuItems.count(); i++) {
            String text = topMenuItems.nth(i).innerText().trim();
            if (!text.isEmpty()) {
                System.out.println("- " + text.replace("\n", " > "));
            }
        }

        // 4. Print the "Bottom Menu" (תפריט תחתון)
        System.out.println("\n--- תפריט תחתון ---");
        Locator bottomMenuItems = page.locator("nav.pull-right").last().locator("li");
        for (int i = 0; i < bottomMenuItems.count(); i++) {
            String text = bottomMenuItems.nth(i).innerText().trim();
            if (!text.isEmpty()) {
                System.out.println("- " + text);
            }
        }
    }

    public void printAccessibilityStatement() {
        // 1. Specifically look for the link INSIDE the footer
        page.locator("footer a:has-text('הצהרת נגישות')").first().click();

        // 2. Locate the article container by class
        Locator contentArea = page.locator("article.col-left");

        // 3. Extract and print the text
        contentArea.waitFor();
        String statementText = contentArea.innerText();

        System.out.println("--- Accessibility Statement ---");
        System.out.println(statementText);
        System.out.println("-------------------------------");
    }

    public void printPrivacyPolicy() {
        // 1. Click the 'Privacy Policy' link inside the footer
        page.locator("footer a:has-text('מדיניות פרטיות')").first().click();

        // 2. Locate the specific article container for the privacy text
        Locator privacyContent = page.locator("article.col-left");

        // 3. Wait for the Angular content and print the text
        privacyContent.waitFor();
        String text = privacyContent.innerText();

        System.out.println("--- Privacy Policy ---");
        System.out.println(text);
        System.out.println("----------------------");
    }

    // --- 4. PDF HANDLING & TEXT EXTRACTION ---
    // Pass the specific PDF URL into the method when you call it
    public void clickAndPrintPdf(String targetUrl) {
        page.navigate("https://www.metropoline.com/Pages/contentA.aspx?p=air_pollution");

        // We added .first() here to solve the "strict mode" error
        Locator reportLink = page.locator("a[href='" + targetUrl + "']").first();

        System.out.println("Targeting PDF: " + targetUrl);

        Download download = page.waitForDownload(() -> {
            reportLink.click(new Locator.ClickOptions().setModifiers(Arrays.asList(KeyboardModifier.ALT)));
        });

        try (PDDocument document = PDDocument.load(download.path().toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            System.out.println("\n--- START PDF ---");
            System.out.println(stripper.getText(document));
            System.out.println("--- END PDF ---\n");
        } catch (Exception e) {
            System.err.println("Error reading PDF: " + e.getMessage());
        }
    }

    // --- 5. VISUAL CAPTURE & SCREENSHOTS ---
    public void capturePdfScreenshots(String targetUrl, String baseName, int pageCount) {
        // 1. Set the tall viewport once for the whole session
        page.setViewportSize(1280, 1600);
        page.navigate(targetUrl);

        // Give the PDF viewer time to load
        page.waitForTimeout(5000);

        // 2. Apply the zoom out once to fit the page width/height
        page.keyboard().press("Control+-");
        System.out.println("Perspective adjusted. Target: " + baseName + " (" + pageCount + " pages)");

        page.waitForTimeout(2000);

        // 3. The Adaptive Loop
        for (int i = 1; i <= pageCount; i++) {
            // Unique name for every page
            String fileName = baseName + "_Page" + i + ".png";

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(fileName)));

            System.out.println("Successfully snatched: " + fileName);

            // Scroll down to the next page
            if (i < pageCount) {
                page.mouse().wheel(0, 1050);
                page.waitForTimeout(1500);
            }
        }

        System.out.println("Mission accomplished. " + pageCount + " images are waiting in your folder.");
    }

    public void clickAndCapture(String url, String fileName) {
        // 1. Navigate to the specific kingdom
        page.navigate(url);

        // 2. Wait for the page to actually finish its animations/loading
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        // 3. Take the trophy
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(fileName + ".png"))
                .setFullPage(true)); // Capture the whole landing page top-to-bottom

        System.out.println("Snatched the landing page: " + fileName);
    }

    public void captureSupplierForm() {
        // 1. Navigate to the Suppliers page
        page.navigate("https://www.metropoline.com/Pages/contentA.aspx?p=Suppliers");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // 2. Locate and click the button "טופס פתיחת ספק"
        page.locator("input[name='טופס פתיחת ספק']").click();
        System.out.println("Button clicked. PDF target acquired.");

        // 3. Use your proven 'Double-Zoom' sniper logic for the PDF
        String pdfUrl = "https://www.metropoline.com/WS/PDF/טופס%20פתיחת%20ספק%20חדש.pdf";
        String baseName = "New_Supplier_Form";

        // Reuse your winning viewport logic
        page.setViewportSize(1280, 1600);
        page.navigate(pdfUrl);
        page.waitForTimeout(4000);

        // Zoom out for that perfect fit
        page.keyboard().press("Control+-");
        page.waitForTimeout(2000);

        // Capture the trophy
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(baseName + ".png")));

        System.out.println("The Supplier Form has been snatched and saved as: " + baseName + ".png");
    }

    public void captureOrafimText() {
        // 1. Navigate to the Advertising (Orafim) page
        page.navigate("https://www.metropoline.com/Pages/contentA.aspx?p=Orafim");

        // 2. Wait for the Angular content to compile and render in the article
        page.waitForSelector("article.col-left");

        // 3. Extract the text from the article element
        String advertisingText = page.locator("article.col-left").innerText();

        // 4. Print the "Decree" to the console
        System.out.println("--- EXTRACTED ADVERTISING TEXT ---");
        System.out.println(advertisingText);
        System.out.println("----------------------------------");
    }
}