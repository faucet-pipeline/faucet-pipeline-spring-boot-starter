let targetBaseDir = "./target/classes/static"
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
              file: "./target/classes/manifest.json",
              //key: 'short',
              key: (f, targetDir) => path.relative(targetBaseDir, f),
              webRoot: targetBaseDir
          }
  };