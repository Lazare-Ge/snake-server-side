const path = require('path');

module.exports = {
  entry: {
    request_stream: './front/game-client.js',
  },
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, 'dist'),
  },
  mode: 'development', // Use 'production' for production builds
  target: 'web', // Target the web platform
};
