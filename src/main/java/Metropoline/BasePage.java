package Metropoline;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * BasePage contains all common methods for interacting with pages.
 * All specific page classes will extend this class.
 */
public class BasePage {

    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    // Navigate to a specific URL
    public void goTo(String url) {
        page.navigate(url);
    }

    // Click an element by selector
    protected void click(String selector) {
        page.locator(selector).click();
    }

    // Type text into input field
    protected void type(String selector, String text) {
        page.locator(selector).fill(text);
    }

    // Get text from an element
    protected String getText(String selector) {
        return page.locator(selector).innerText();
    }

    // Check if element is visible
    protected boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    // Wait for element to be visible
    protected void waitForVisible(String selector) {
        page.locator(selector).waitFor();
    }

    // Optional: Click element with timeout
    protected void clickWithTimeout(String selector, int timeoutMs) {
        page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
        page.locator(selector).click();
    }
}
