import { test, expect } from '@playwright/test';

test.describe('View | Student Landing Page', () => {
  test('Title', async ({page}) => {
    await page.goto('localhost');

    const h2 = page.locator('h2');
    await expect(h2).toHaveText(/Escape Room/);
  });

  test('Join Button', async ({ page }) => {
    await page.goto('http://localhost');

    const joinButton = page.locator('button:text("Join")');
    await expect(joinButton).toBeVisible();
  });

});
