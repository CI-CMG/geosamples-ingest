module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '@contextRoot@' : '/',
  configureWebpack: {
    devtool: 'source-map',
  },
  devServer: {
    https: true,
    proxy: {
      '^/api': {
        target: process.env.PROXY,
        secure: false,
      },
    },
  },
};
