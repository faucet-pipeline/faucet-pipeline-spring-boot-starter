let targetBaseDir = "./target/generated-resources/faucet/static"
const path = require('path');

module.exports = {      
      js: [{
          source: "./src/main/assets/index.js",
          target: targetBaseDir + "/app.js"
      }],
      sass: [{
          source: "./src/main/assets/index.scss",
          target: targetBaseDir + "/stylesheets/app.css"
      }],
/*      static: [{
          source: "./src/main/assets/images",
          target: targetBaseDir + "/images"
      }],
      */
      manifest: {
              file: "./target/generated-resources/faucet/manifest.json",
              key: (f, targetDir) => path.relative(targetBaseDir, f),
              webRoot: targetBaseDir
          }
  };