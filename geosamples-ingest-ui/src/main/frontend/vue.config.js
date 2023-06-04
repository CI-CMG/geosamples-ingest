const contextRoot = process.env.NODE_ENV === 'production' ? '@contextRoot@' : `${process.env.VUE_APP_BASE_URL}`;
const proxy = {};
proxy[`^${process.env.VUE_APP_BASE_URL}/api`] = {
  target: process.env.PROXY,
  secure: false,
};
proxy[`^${process.env.VUE_APP_BASE_URL}/docs`] = {
  target: process.env.PROXY,
  secure: false,
};

module.exports = {
  publicPath: contextRoot,
  configureWebpack: {
    devtool: process.env.NODE_ENV === 'production' ? 'source-map' : 'eval-source-map',
  },
  devServer: {
    https: true,
    proxy,
  },
};
