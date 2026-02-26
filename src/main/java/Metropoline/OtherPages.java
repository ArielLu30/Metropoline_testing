package Metropoline;

import com.microsoft.playwright.*;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

public class OtherPages {

    private Page page;
    private Random random = new Random();

    // --- 1. INITIALIZATION & DIALOGS ---
    /**
     * Constructor
     */
    public OtherPages(Page page) {
        this.page = page;
    }

    /**
     * Close popup if it exists
     */
    public void closePopupIfExists() {
        Locator closePopup = page.locator("button#closeDialog");
        if (closePopup.count() > 0) {
            closePopup.click();
            System.out.println("Popup closed successfully.");
        } else {
            System.out.println("No popup appeared.");
        }
    }

    // --- 2. NAVIGATION & HOVER ACTIONS ---
    /**
     * Navigate to the 2025 Reform page via hover menu
     */
    public void openReformatTzedeqUnderPrices() {
        page.locator("a:has-text('תעריפים וכרטיסים')").first().hover();
        System.out.println("Hovered over first 'תעריפים וכרטיסים' link.");
        page.waitForTimeout(1000);
        page.locator("a:has(span:has-text('רפורמת צדק תחבורתי - אפריל 2025'))").first().hover();
        System.out.println("Hovered over 'רפורמת צדק תחבורתי - אפריל 2025' link.");
        page.locator("a:has(span:has-text('רפורמת צדק תחבורתי - אפריל 2025'))").first().click();
        System.out.println("Clicked 'רפורמת צדק תחבורתי - אפריל 2025' link.");
        page.waitForTimeout(2000);
    }

    /**
     * Navigate to Al-Hakav updates via hover menu
     */
    public void openAlHakavLink() {
        page.locator("a:has-text('רב קו')").first().hover();
        System.out.println("Hovered over 'רב קו' link.");
        page.waitForTimeout(500);
        page.locator("span:has-text('עמדות \"על הקו\" - מידע ועדכונים')").first().click();
        System.out.println("Clicked 'עמדות \"על הקו\" - מידע ועדכונים' link.");
    }

    // --- 3. RAV-KAV & ACCORDION DATA ---
    /**
     * Open Rav-Kav page, click a random section, and print its content
     */
    public void clickRavKavRandomSectionAndPrint() {
        // 1. Navigate directly to the Rav-Kav page
        page.navigate("https://www.metropoline.com/Pages/Questions.aspx?p=al_ha_kav");
        System.out.println("Navigated to Rav-Kav page.");

        // 2. Locate all expandable Rav-Kav sections
        Locator openers = page.locator("a.opener.summary");
        int count = openers.count();

        if (count == 0) {
            System.out.println("No Rav-Kav sections found.");
            return;
        }

        // 3. Pick a random section
        int randomIndex = random.nextInt(count);
        Locator randomOpener = openers.nth(randomIndex);
        String openerText = randomOpener.textContent().trim();

        // 🔑 Get the controlled panel ID BEFORE clicking
        String panelId = randomOpener.getAttribute("aria-controls");
        if (panelId == null || panelId.isEmpty()) {
            System.out.println("Panel ID not found for section: " + openerText);
            return;
        }

        // 4. Click the random section
        randomOpener.click();
        System.out.println("Random Rav-Kav section clicked: " + openerText);

        // 5. Locate the exact opened panel
        Locator panel = page.locator("#" + panelId);
        panel.waitFor();
    }

    /**
     * Locate the 'עמדות טעינה אוטומטיות' section and print it as a formatted table
     */
    public void printRavKavSectionAsTable() {
        // Locate all expandable Rav-Kav sections
        Locator openers = page.locator("a.opener.summary");
        int count = openers.count();

        for (int i = 0; i < count; i++) {
            String sectionTitle = openers.nth(i).textContent().trim();

            if (sectionTitle.equals("עמדות טעינה אוטומטיות")) {
                // Get panel ID
                String panelId = openers.nth(i).getAttribute("aria-controls");
                Locator panel = page.locator("#" + panelId);

                // Click the section to open it
                openers.nth(i).click();
                panel.waitFor();

                // Locate rows
                Locator rows = panel.locator("tbody tr");
                int rowCount = rows.count();

                // First, collect all rows and columns
                String[][] table = new String[rowCount][];
                int[] maxColWidths = null;

                for (int r = 0; r < rowCount; r++) {
                    Locator cells = rows.nth(r).locator("td");
                    int cellCount = cells.count();
                    if (maxColWidths == null) maxColWidths = new int[cellCount];

                    table[r] = new String[cellCount];
                    for (int c = 0; c < cellCount; c++) {
                        String text = cells.nth(c).textContent().trim();
                        table[r][c] = text;
                        maxColWidths[c] = Math.max(maxColWidths[c], text.length());
                    }
                }

                // Print rows with padding
                System.out.println("Charging stations table content:");
                for (String[] row : table) {
                    StringBuilder sb = new StringBuilder();
                    for (int c = 0; c < row.length; c++) {
                        sb.append(String.format("%-" + (maxColWidths[c] + 2) + "s", row[c]));
                    }
                    System.out.println(sb);
                }

                break; // stop after printing this section
            }
        }
    }

    // --- 4. REGIONAL UPDATES & MESSAGES ---
    /**
     * Extract a random message from the Elad region updates
     */
    public void PrintRandomELadUpdate() {
        // 0. Navigate directly to the Elad messages page
        page.navigate("https://www.metropoline.com/Pages/Messages.aspx?p=neweladmessages#/17008");
        System.out.println("Navigated to Elad messages page.");

        page.waitForTimeout(1000);

        // 1. Locate all message openers
        Locator openers = page.locator("ul.accordion li a.opener.summary");
        int count = openers.count();

        if (count == 0) {
            System.out.println("No messages found on the page.");
            return;
        }

        // 2. Pick a random opener
        int randomIndex = random.nextInt(count);
        Locator randomOpener = openers.nth(randomIndex);
        String title = randomOpener.textContent().trim();

        // 3. Get the controlled panel ID
        String panelId = randomOpener.getAttribute("aria-controls");
        if (panelId == null || panelId.isEmpty()) {
            System.out.println("No panel linked to: " + title);
            return;
        }

        // 4. Click the random opener
        randomOpener.click();
        System.out.println("Clicked message: " + title);

        page.waitForTimeout(500);

        // 5. Locate the panel and wait for it
        Locator panel = page.locator("#" + panelId);
        panel.waitFor();

        // 6. Print all <p> text inside the panel
        Locator paragraphs = panel.locator("p");
        int pCount = paragraphs.count();

        System.out.println("Message content:");
        for (int i = 0; i < pCount; i++) {
            String text = paragraphs.nth(i).innerText().trim();
            if (!text.isEmpty()) {
                System.out.println(text.replaceAll("\\u00A0", " ")); // replace &nbsp; with space
            }
        }
    }

    /**
     * Extract a random message from the Sharon region updates
     */
    public void PrintRandomSharonUpdate() {
        // 0. Navigate directly to Sharon messages
        page.navigate("https://www.metropoline.com/Pages/Messages.aspx?p=sharonMessages");
        System.out.println("Navigated to Sharon messages page.");

        page.waitForTimeout(1000);

        // 1. Locate all message openers
        Locator openers = page.locator("ul.accordion li a.opener.summary");
        int count = openers.count();

        if (count == 0) {
            System.out.println("No messages found on the Sharon page.");
            return;
        }

        // 2. Pick a random opener
        int randomIndex = random.nextInt(count);
        Locator randomOpener = openers.nth(randomIndex);
        String title = randomOpener.textContent().trim();

        // 3. Get the controlled panel ID
        String panelId = randomOpener.getAttribute("aria-controls");
        if (panelId == null || panelId.isEmpty()) {
            System.out.println("No panel linked to: " + title);
            return;
        }

        // 4. Click the random opener
        randomOpener.click();
        System.out.println("Clicked message: " + title);

        page.waitForTimeout(500);

        // 6. Locate the panel and wait for it
        Locator panel = page.locator("#" + panelId);
        panel.waitFor();

        // 7. Print all <p> text inside the panel
        Locator paragraphs = panel.locator("p");
        int pCount = paragraphs.count();

        System.out.println("Message content:");
        for (int i = 0; i < pCount; i++) {
            String text = paragraphs.nth(i).innerText().trim();
            if (!text.isEmpty()) {
                System.out.println(text.replaceAll("\\u00A0", " "));
            }
        }
    }

    /**
     * Extract random messages based on dynamic string categories
     */
    public void printRandomMessagesByCategories(String... categories) {
        // 1. Define categories and URLs
        Map<String, String> categoryUrls = new HashMap<>();
        categoryUrls.put("elad", "https://www.metropoline.com/Pages/Messages.aspx?p=neweladmessages#/17008");
        categoryUrls.put("sharon", "https://www.metropoline.com/Pages/Messages.aspx?p=sharonMessages");
        categoryUrls.put("darom", "https://www.metropoline.com/Pages/Messages.aspx?p=daromMessages");
        categoryUrls.put("information and lost stuff", "https://www.metropoline.com/Pages/Messages.aspx?p=information_Lost");

        // 2. Loop over provided categories
        for (String category : categories) {
            String url = categoryUrls.get(category.toLowerCase());
            if (url == null) {
                System.out.println("Category not found: " + category);
                continue;
            }

            page.navigate(url);
            System.out.println("\nNavigated to " + category + " messages page.");
            page.waitForTimeout(1000);

            Locator openers = page.locator("ul.accordion li a.opener.summary");
            int count = openers.count();
            if (count == 0) {
                System.out.println("No messages found on the " + category + " page.");
                continue;
            }

            int randomIndex = random.nextInt(count);
            Locator randomOpener = openers.nth(randomIndex);
            String title = randomOpener.textContent().trim();

            String panelId = randomOpener.getAttribute("aria-controls");
            if (panelId == null || panelId.isEmpty()) {
                System.out.println("No panel linked to: " + title);
                continue;
            }

            randomOpener.click();
            System.out.println("Clicked message: " + title);

            page.waitForTimeout(500);
            Locator panel = page.locator("#" + panelId);
            panel.waitFor();

            Locator paragraphs = panel.locator("p");
            int pCount = paragraphs.count();
            System.out.println("Message content:");
            for (int i = 0; i < pCount; i++) {
                String text = paragraphs.nth(i).innerText().trim();
                if (!text.isEmpty()) {
                    System.out.println(text.replaceAll("\\u00A0", " "));
                }
            }
        }
    }

    // --- 5. INSURANCE & LOST AND FOUND ---
    /**
     * Print the main content of the Insurance page
     */
    public void printInsuranceMessage() {
        page.navigate("https://www.metropoline.com/Pages/contentA.aspx?p=Insurance");
        System.out.println("Navigated to Insurance page.");
        page.waitForTimeout(1000);

        Locator article = page.locator("div.update-area article.col-left");
        article.waitFor();

        String text = article.innerText().trim().replaceAll("\\u00A0", " ");
        System.out.println("Insurance message content:\n" + text);
    }

    /**
     * Extract a random message from the Lost & Found page
     */
    public void printRandomLostAndFoundMessage() {
        page.navigate("https://www.metropoline.com/Pages/Questions.aspx?p=newLost");
        System.out.println("Navigated to Lost & Found page.");
        page.waitForTimeout(1000);

        Locator openers = page.locator("ul.accordion li a.opener.summary");
        int count = openers.count();

        if (count == 0) {
            System.out.println("No messages found on the Lost & Found page.");
            return;
        }

        int randomIndex = random.nextInt(count);
        Locator randomOpener = openers.nth(randomIndex);
        String title = randomOpener.textContent().trim();

        String panelId = randomOpener.getAttribute("aria-controls");
        if (panelId == null || panelId.isEmpty()) {
            System.out.println("No panel linked to: " + title);
            return;
        }

        randomOpener.click();
        System.out.println("Clicked message: " + title);

        page.waitForTimeout(500);
        Locator panel = page.locator("#" + panelId);
        panel.waitFor();

        // Print all text inside <div class="ng-scope">
        Locator messageDivs = panel.locator("div.ng-scope");
        int divCount = messageDivs.count();
        System.out.println("Lost & Found message content:");
        for (int i = 0; i < divCount; i++) {
            String text = messageDivs.nth(i).innerText().trim().replaceAll("\\u00A0", " ");
            if (!text.isEmpty()) {
                System.out.println(text);
            }
        }
    }
}