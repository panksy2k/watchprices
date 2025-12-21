/**
 * Script to generate Open Graph image
 * 
 * Install dependencies first:
 * npm install puppeteer
 * 
 * Then run:
 * node generate-og-image.js
 */

import puppeteer from 'puppeteer';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const htmlContent = `
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            width: 1200px;
            height: 630px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            position: relative;
            overflow: hidden;
        }
        body::before {
            content: '';
            position: absolute;
            width: 400px;
            height: 400px;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.1);
            top: -100px;
            right: -100px;
        }
        body::after {
            content: '';
            position: absolute;
            width: 300px;
            height: 300px;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.08);
            bottom: -80px;
            left: -80px;
        }
        .content {
            text-align: center;
            color: white;
            z-index: 1;
        }
        .icon {
            font-size: 120px;
            margin-bottom: 30px;
            filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.2));
        }
        h1 {
            font-size: 72px;
            font-weight: 800;
            margin: 0 0 20px 0;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
            text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
        }
        p {
            font-size: 36px;
            margin: 0;
            font-weight: 400;
            opacity: 0.95;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }
        .badge {
            display: inline-block;
            background: rgba(255, 255, 255, 0.2);
            padding: 12px 30px;
            border-radius: 30px;
            margin-top: 30px;
            font-size: 24px;
            font-weight: 600;
            backdrop-filter: blur(10px);
        }
    </style>
</head>
<body>
    <div class="content">
        <div class="icon">⌚</div>
        <h1>Sports Watch Manager</h1>
        <p>Track & Manage Your Collection</p>
        <div class="badge">watchprices.de</div>
    </div>
</body>
</html>
`;

async function generateOGImage() {
    console.log('🚀 Starting OG image generation...');
    
    const browser = await puppeteer.launch();
    const page = await browser.newPage();
    
    await page.setViewport({
        width: 1200,
        height: 630,
        deviceScaleFactor: 2 // For retina display quality
    });
    
    await page.setContent(htmlContent);
    
    const outputPath = path.join(__dirname, 'public', 'og-image.jpg');
    
    // Ensure public directory exists
    if (!fs.existsSync(path.join(__dirname, 'public'))) {
        fs.mkdirSync(path.join(__dirname, 'public'));
    }
    
    await page.screenshot({
        path: outputPath,
        type: 'jpeg',
        quality: 90
    });
    
    await browser.close();
    
    console.log('✅ OG image generated successfully!');
    console.log(`📁 Saved to: ${outputPath}`);
    console.log('📊 Size: 1200x630px');
    console.log('\n🔧 Next steps:');
    console.log('1. Update your HTML meta tags with the new image path');
    console.log('2. Test with: https://www.opengraph.xyz/');
}

generateOGImage().catch(console.error);
