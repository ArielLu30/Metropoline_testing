package Metropoline;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

public class HomePageAccessibility {
    private Page page;

    public HomePageAccessibility(Page page) {
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

    /**
     * Click the accessibility menu button
     */
    public void clickAccessibilityMenu() {
        Locator accessibilityBtn = page.locator("img#menuAccessibilityImg");

        if (accessibilityBtn.isVisible()) {
            accessibilityBtn.click();
            System.out.println("Accessibility menu clicked!");
        } else {
            System.out.println("Accessibility menu button not visible.");
        }
    }

    public void clickScreenReaderToggle() {
        // Try to click either button: "קורא מסך" or "הסר קורא מסך"
        String[] buttonTexts = { "קורא מסך", "הסר קורא מסך" };

        for (int i = 0; i < 2; i++) {
            boolean clicked = false;

            for (String text : buttonTexts) {
                Locator button = page.locator("div[role='button']")
                        .filter(new Locator.FilterOptions().setHasText(text));

                if (button.count() > 0 && button.first().isVisible()) {
                    button.first().click();
                    System.out.println("Screen reader button clicked! Text: " + text + " | Click #" + (i + 1));
                    clicked = true;
                    break; // stop checking other text, we already clicked
                }
            }

            if (!clicked) {
                System.out.println("No screen reader button found on attempt #" + (i + 1));
            }

            // Wait a bit for the text to toggle
            page.waitForTimeout(500);
        }
    }

    public void clickKeyboardToggle() {
        // Try to click either button: "ניווט מקלדת" or "הסר ניווט מקלדת"
        String[] buttonTexts = { "ניווט מקלדת", "הסר ניווט מקלדת" };

        for (int i = 0; i < 2; i++) {
            boolean clicked = false;

            for (String text : buttonTexts) {
                Locator button = page.locator("div[role='button']").filter(new Locator.FilterOptions().setHasText(text));

                if (button.count() > 0 && button.first().isVisible()) {
                    button.first().click();
                    System.out.println("Keyboard button clicked! Text: " + text + " | Click #" + (i + 1));
                    clicked = true;
                    break; // stop checking other text, we already clicked
                }
            }

            if (!clicked) {
                System.out.println("No keyboard button found to click on attempt #" + (i + 1));
            }

            // Wait a bit for the text to toggle
            page.waitForTimeout(500);
        }
    }

    public void clickFlickerBlockToggle() {
        // Try to click the button: "חסימת הבהובים" or its toggled state if the text changes
        String[] buttonTexts = { "חסימת הבהובים", "הסר חסימת הבהובים" };

        for (int i = 0; i < 2; i++) {
            boolean clicked = false;

            for (String text : buttonTexts) {
                Locator button = page.locator("div[role='button']").filter(
                        new Locator.FilterOptions().setHasText(text)
                );

                if (button.count() > 0 && button.first().isVisible()) {
                    button.first().click();
                    System.out.println("Flicker block button clicked! Text: " + text + " | Click #" + (i + 1));
                    clicked = true;
                    break; // Stop checking other text
                }
            }

            if (!clicked) {
                System.out.println("No flicker block button found on attempt #" + (i + 1));
            }

            // Wait a bit for the text to toggle
            page.waitForTimeout(500);
        }
    }

    public void clickAccessibilityToggles(String[] buttonTexts) {
        for (String text : buttonTexts) {
            // Try clicking each button twice to toggle on/off
            for (int i = 0; i < 2; i++) {
                Locator button = page.locator("div[role='button']")
                        .filter(new Locator.FilterOptions().setHasText(text));

                if (button.count() > 0 && button.first().isVisible()) {
                    button.first().click();
                    System.out.println("Accessibility button clicked: " + text + " | Click #" + (i + 1));
                    page.waitForTimeout(500); // wait for toggle effect
                } else {
                    System.out.println("Button not visible for click #" + (i + 1) + ": " + text);
                    break; // stop trying if the button disappeared
                }
            }
        }
    }
    public void clickCancelAccessibility() {
        page.locator("div[role='button']").filter(new Locator.FilterOptions().setHasText("בטל נגישות")).first().click();
        System.out.println("Cancel accessibility button clicked!");
    }

    public void clickAccessibilityStatement() {
        page.locator("span").filter(new Locator.FilterOptions().setHasText("הצהרת נגישות")).first().click();
        System.out.println("Accessibility Statement clicked!");
    }
    public void clickSendFeedback() {
        page.locator("span").filter(new Locator.FilterOptions().setHasText("שלח משוב")).first().click();
        System.out.println("Send Feedback clicked!");
    }
}

