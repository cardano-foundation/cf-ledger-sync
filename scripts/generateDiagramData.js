const path = require("path");
const fs = require("fs");
const puppeteer = require("puppeteer");

async function extractMermaidGraphData(diagramText) {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();

  // Inject mermaid and create the HTML page
  await page.setContent(`
    <!DOCTYPE html>
    <html>
    <head>
      <script src="https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"></script>
    </head>
    <body>
      <div id="output"></div>
    </body>
    </html>
  `);
  await page.waitForFunction('typeof mermaid !== "undefined"');

  const result = await page.evaluate(async (text) => {
    mermaid.initialize({ startOnLoad: false });
    try {
      const diagram = await mermaid.mermaidAPI.getDiagramFromText(text);
      const parser = diagram.getParser().parser?.yy;

      const vertices = parser.getVertices();
      const edges = parser.getEdges();

      return {
        vertices: Array.from(vertices.entries()),
        edges: edges,
      };
    } catch (error) {
      return { error: error.message };
    }
  }, diagramText);

  await browser.close();

  // Check if there was an error during extraction
  if (result.error) {
    throw new Error(`Error in browser context: ${result.error}`);
  }

  return result;
}

async function generateDiagramData() {
  const diagram = fs.readFileSync("questionnaire.mmd", "utf8");
  if (!diagram) {
    throw new Error("No diagram found in questionnaire.md");
  }
  try {
    const { vertices, edges } = await extractMermaidGraphData(diagram);

    const output = {
      vertices,
      edges,
      __generatedAt: Date.now(),
    };
    const json = JSON.stringify(output, null, 2);
    const outputDir = path.join(__dirname, "../public/data");
    if (!fs.existsSync(outputDir)) {
      fs.mkdirSync(outputDir, { recursive: true });
    }

    fs.writeFileSync(path.join(outputDir, "generated-data.json"), json);
  } catch (error) {
    throw new Error("Error extracting graph data: " + error.message);
  }
}

generateDiagramData()
  .then(() => {
    console.log("Diagram data generated successfully.");
  })
  .catch((error) => {
    console.error("Error generating diagram data:", error);
  });
